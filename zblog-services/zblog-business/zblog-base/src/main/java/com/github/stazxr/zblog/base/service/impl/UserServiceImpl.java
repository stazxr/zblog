package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.notify.mail.MailReceiver;
import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.core.UserType;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.base.converter.UserConverter;
import com.github.stazxr.zblog.base.domain.dto.UserDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.entity.UserLoginLog;
import com.github.stazxr.zblog.base.domain.entity.UserRoleRelation;
import com.github.stazxr.zblog.base.domain.enums.Gender;
import com.github.stazxr.zblog.base.domain.enums.LoginType;
import com.github.stazxr.zblog.base.domain.enums.MailTemplate;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.base.mapper.UserLoginLogMapper;
import com.github.stazxr.zblog.base.mapper.UserMapper;
import com.github.stazxr.zblog.base.mapper.UserRoleMapper;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.base.util.MailTemplateSender;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.RegexUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.UuidUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户管理业务实现层
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;

    private final UserConverter userConverter;

    private final UserLoginLogMapper userLoginLogMapper;

    private final UserRoleMapper userRoleMapper;

    private final PasswordEncoder passwordEncoder;

    private final SecurityExtProperties securityExtProperties;

    private final MailTemplateSender mailTemplateSender;

    /**
     * 根据 {@code userId} 查询用户信息。
     *
     * @param userId 用户 ID
     * @return 用户的 {@link SecurityUser} 实例
     */
    @Override
    public SecurityUser findUserById(String userId) {
        // 根据用户ID查询用户信息
        Assert.notBlank(userId, ExpMessageCode.of("valid.common.id.required"));
        User user = userMapper.selectUserById(Long.parseLong(userId));
        Assert.notNull(user, ExpMessageCode.of("valid.user.data.notExist"));
        // 设置角色和权限信息
        setRoleAndPerm(user);
        return user;
    }

    /**
     * 根据用户名 {@code username} 登录并返回登录用户信息。
     *
     * @param username 用户名
     * @return 用户的 {@link SecurityUser} 实例
     */
    @Override
    public SecurityUser loginWithUsername(String username) {
        // 根据用户ID查询用户信息
        Assert.notBlank(username, ExpMessageCode.of("valid.user.username.required"));
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 系统用户被禁止登录
        if (UserType.SYSTEM_USER.getType().equals(user.getUserType())) {
            throw new AccessDeniedException("系统用户被禁止登录");
        }
        // 提前校验用户是否锁定，放在密码校验前
        if (!user.isAccountNonLocked()) {
            throw new LockedException("用户已锁定");
        }

        // 设置角色和权限信息
        setRoleAndPerm(user);
        return user;
    }

    /**
     * 更新用户的登录信息。
     *
     * @param username 用户名
     * @param userIp   用户的登录ip
     * @param type     用户登录类型
     * @param request  用户的请求信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserLoginInfo(String username, String userIp, int type, HttpServletRequest request) {
        // 获取用户信息
        User user = userMapper.selectUserByUsername(username);
        Assert.notNull(user, ExpMessageCode.of("valid.user.data.notExist"));
        Long userId = user.getId();
        // 获取用户登录信息
        UserLoginLog loginLog = new UserLoginLog();
        LocalDateTime loginTime = LocalDateTime.now();
        loginLog.setId(SequenceUtils.getId());
        loginLog.setUserId(userId);
        loginLog.setLoginTime(loginTime);
        loginLog.setLoginIp(userIp == null ? IpUtils.getIp(request) : userIp);
        loginLog.setLoginChan(request.getHeader("X-Client-Type"));
        loginLog.setLoginAddress(IpUtils.getIpLocation(userIp));
        loginLog.setUserAgent(IpUtils.getBrowser(request));
        loginLog.setCreateDate(DateUtils.formatNow(DateUtils.YMD_PATTERN));
        loginLog.setCreateTime(DateUtils.formatNow(DateUtils.YMD_HMS_PATTERN));
        // 记录用户登录信息
        if (type == 1) {
            // 密码登录成功
            loginLog.setLoginType(LoginType.PASSWORD.getType());
            loginLog.setIsSuccess(true);
            loginLog.setRemark("用户登录成功");
            userMapper.updateLoginInfoWhenSuccess(userId, loginTime);
        } else if (type == 2) {
            // 登录失败，使用了错误的密码
            loginLog.setLoginType(LoginType.PASSWORD.getType());
            loginLog.setIsSuccess(false);
            loginLog.setRemark("用户登录失败，使用了错误的密码");
            SecurityExtProperties.UserLockConfig lockConfig = securityExtProperties.getLockConfig();
            LocalDateTime lockedExpireTime = LocalDateTime.now().plus(lockConfig.getLockDuration());
            int maxFailCount = lockConfig.getMaxFailCount();
            userMapper.updateLoginInfoWhenFailed(userId, maxFailCount, lockedExpireTime);
        } else if (type == 3) {
            // 登录失败，使用不存在的用户名，DO NOTHING
            return;
        } else {
            // 有其他场景后续在扩展，目前默认为 UNKNOWN
            loginLog.setLoginType(LoginType.UNKNOWN.getType());
            loginLog.setIsSuccess(false);
        }

        // 数据入库
        userLoginLogMapper.insert(loginLog);
    }

    /**
     * 分页查询用户列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<UserVo>
     */
    @Override
    public IPage<UserVo> queryUserListByPage(UserQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getUsername())) {
            queryDto.setUsername(queryDto.getUsername().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getNickname())) {
            queryDto.setNickname(queryDto.getNickname().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getEmail())) {
            queryDto.setEmail(queryDto.getEmail().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getLoginAddress())) {
            queryDto.setLoginAddress(queryDto.getLoginAddress().trim());
        }
        // 分页查询
        Page<UserVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return userMapper.selectUserList(page, queryDto);
    }

    /**
     * 查询用户详情
     *
     * @param userId 用户序列
     * @return UserVo
     */
    @Override
    public UserVo queryUserDetail(Long userId) {
        Assert.notNull(userId, ExpMessageCode.of("valid.common.id.required"));
        UserVo userVo = userMapper.selectUserDetail(userId);
        Assert.notNull(userVo, ExpMessageCode.of("valid.common.data.notFound"));
        return userVo;
    }

    /**
     * 新增用户
     *
     * @param userDto 用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserDto userDto) {
        // 获取用户信息
        User user = userConverter.dtoToEntity(userDto);
        // 新增时，不允许传入 UserId
        Assert.isNull(user.getId(), ExpMessageCode.of("valid.common.add.idNotAllowed"));
        // 设置用户默认值
        Long userId = SequenceUtils.getId();
        user.setId(userId);
        user.setGender(Gender.HIDE.getType());
        user.setNickname(user.getUsername());
        String password = UuidUtils.gen8BitUuidStr().toLowerCase(Locale.ROOT);
        user.setPassword(passwordEncoder.encode(password));
        // 权限检查
        checkPermission(user);
        // 用户基本信息检查
        checkUser(user);
        // 新增用户
        Assert.affectOneRow(userMapper.insert(user), ExpMessageCode.of("result.common.add.failed"));
        // 保存角色信息
        insertUserRoleData(userId, userDto.getRoleIds(), false);
        // 邮件通知（邮件发送失败也回滚，因为没有人知道新用户密码是多少）
        sendAddUserNoticeEmail(user.getEmail(), user.getUsername(), password);
        log.info("add user {} success", user.getUsername());
    }

    /**
     * 编辑用户
     *
     * @param userDto 用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(UserDto userDto) {
        // 获取用户信息
        User user = userConverter.dtoToEntity(userDto);
        // 判断用户是否存在
        User dbUser = userMapper.selectUserById(user.getId());
        Assert.notNull(dbUser, ExpMessageCode.of("valid.common.data.notFound"));
        // 判断用户名是否被修改
        Assert.isEquals(dbUser.getUsername(), user.getUsername(), ExpMessageCode.of("valid.user.username.immutable"));
        // 权限检查
        checkPermission(dbUser);
        // 用户信息检查
        checkUser(user);
        // 编辑用户
        Assert.affectOneRow(userMapper.updateById(user), ExpMessageCode.of("result.common.edit.failed"));
        // 保存角色信息
        insertUserRoleData(user.getId(), userDto.getRoleIds(), false);
        // 清除用户缓存信息
        SecurityUserCache.remove(String.valueOf(user.getId()));
    }

    /**
     * 删除用户
     *
     * @param userId 用户序列
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        // 判断用户是否存在
        User dbUser = userMapper.selectUserById(userId);
        Assert.notNull(dbUser, ExpMessageCode.of("valid.common.data.notFound"));
        // 不允许删除自己
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        Assert.isNoEquals(userId, loginUser.getId(), ExpMessageCode.of("valid.user.delete.self"));
        // 权限检查
        checkPermission(dbUser);
        // 删除用户
        Assert.affectOneRow(userMapper.deleteById(userId), ExpMessageCode.of("result.common.delete.failed"));
        // 删除角色信息
        insertUserRoleData(userId, null, true);
        // 清除用户缓存信息
        SecurityUserCache.remove(String.valueOf(userId));
    }

    private void checkUser(User user) {
        // 用户名检查
        String username = user.getUsername().trim();
        user.setUsername(username);
        Assert.failIfFalse(RegexUtils.match(username, RegexUtils.Regex.USERNAME_REGEX), ExpMessageCode.of("valid.user.username.pattern"));
        Assert.failIfTrue(checkUsernameExist(user), ExpMessageCode.of("valid.user.username.exists"));

        // 邮箱检查
        String email = user.getEmail().trim();
        user.setEmail(email);
        Assert.failIfFalse(RegexUtils.match(email, RegexUtils.Regex.EMAIL_REGEX), ExpMessageCode.of("valid.user.email.pattern"));
        Assert.failIfTrue(checkEmailExist(user), ExpMessageCode.of("valid.user.email.exists"));

        // 昵称检查
        AtomicInteger count = new AtomicInteger(1);
        while (checkNicknameExist(user)) {
            Assert.doIfTrueElseThrow(count.get() < Byte.MAX_VALUE, () -> {
                String newNickname = user.getNickname() + count.getAndIncrement();
                user.setNickname(newNickname);
            }, ExpMessageCode.of("valid.user.nickname.exists"));
        }

        // 账号过期时间检查
        if (UserType.TEMP_USER.getType().equals(user.getUserType())) {
            Assert.notNull(user.getExpireTime(), ExpMessageCode.of("valid.user.expireTime.required"));
        } else {
            // BUGS: NULL字段不会被 mybatis 更新，暂不影响功能，忽略
            user.setExpireTime(null);
        }
    }

    private void checkPermission(User user) {
        // 获取当前登录用户信息
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        Integer newUserType = user.getUserType();
        // 普通用户：无法新增或编辑管理员，系统用户
        if (UserType.NORMAL_USER.getType().equals(loginUser.getUserType())) {
            if (UserType.ADMIN_USER.getType().equals(newUserType) || UserType.SYSTEM_USER.getType().equals(newUserType)) {
                throw new ServiceException(ExpMessageCode.of("valid.user.common.dataPerm"));
            }
        }
        // 临时用户：只能新增或编辑临时用户
        if (UserType.TEMP_USER.getType().equals(loginUser.getUserType())) {
            if (!UserType.TEMP_USER.getType().equals(newUserType)) {
                throw new ServiceException(ExpMessageCode.of("valid.user.common.dataPerm"));
            }
        }
        // 测试用户（匿名用户）：只能新增或编辑测试用户
        if (UserType.TEST_USER.getType().equals(loginUser.getUserType())) {
            if (!UserType.TEST_USER.getType().equals(newUserType)) {
                throw new ServiceException(ExpMessageCode.of("valid.user.common.dataPerm"));
            }
        }
    }

    private boolean checkEmailExist(User user) {
        if (user.getEmail() != null) {
            LambdaQueryWrapper<User> queryWrapper = queryBuild().eq(User::getEmail, user.getEmail());
            if (user.getId() != null) {
                queryWrapper.ne(User::getId, user.getId());
            }
            queryWrapper.eq(User::getDeleted, Boolean.FALSE);
            return userMapper.exists(queryWrapper);
        }
        return false;
    }

    private boolean checkUsernameExist(User user) {
        // 用户名全局唯一，不允许重复
        if (user.getUsername() != null) {
            LambdaQueryWrapper<User> queryWrapper = queryBuild().eq(User::getUsername, user.getUsername());
            if (user.getId() != null) {
                queryWrapper.ne(User::getId, user.getId());
            }
            return userMapper.existsIgnoreDeleted(queryWrapper);
        }
        return false;
    }

    private boolean checkNicknameExist(User user) {
        // 昵称全局唯一，不允许重复
        if (user.getNickname() != null) {
            LambdaQueryWrapper<User> queryWrapper = queryBuild().eq(User::getNickname, user.getNickname());
            if (user.getId() != null) {
                queryWrapper.ne(User::getId, user.getId());
            }
            return userMapper.existsIgnoreDeleted(queryWrapper);
        }
        return false;
    }

    private void insertUserRoleData(Long userId, List<Long> roleIds, boolean softDelete) {
        if (softDelete) {
            userRoleMapper.deleteByUserIdSoft(userId);
        } else {
            userRoleMapper.deleteByUserIdHard(userId);
        }
        if (roleIds != null && roleIds.size() > 0) {
            for (Long roleId : roleIds) {
                UserRoleRelation userRole = new UserRoleRelation();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setDeleted(Boolean.FALSE);
                userRoleMapper.insert(userRole);
            }
        }
    }

    private void sendAddUserNoticeEmail(String email, String username, String password) {
        try {
            MailTemplate emailCode = MailTemplate.ADD_USER_NOTICE;
            MailReceiver receiver = MailReceiver.setReceive(email);
            Map<String, Object> variables = new LinkedHashMap<>();
            variables.put("username", username);
            variables.put("password", password);
            mailTemplateSender.send(receiver, emailCode, variables);
        } catch (Exception e) {
            throw new ServiceException(ExpMessageCode.of("error.email.sendError"), e);
        }
    }

    private void setRoleAndPerm(User user) {
        // 查询用户角色列表
        List<Role> roles = userMapper.selectRolesByUserId(user.getId());
        user.setAuthorities(roles);

        // 查询用户权限列表
        if (UserType.ADMIN_USER.getType().equals(user.getUserType())) {
            user.setPerms(userMapper.selectAllMd5PermCodes());
        } else {
            user.setPerms(userMapper.selectMd5PermCodesByUserId(user.getId()));
        }
    }

    private LambdaQueryWrapper<User> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
