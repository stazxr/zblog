package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.notify.mail.MailReceiver;
import com.github.stazxr.zblog.bas.notify.mail.MailService;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.core.UserStatus;
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
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.base.mapper.UserLoginLogMapper;
import com.github.stazxr.zblog.base.mapper.UserMapper;
import com.github.stazxr.zblog.base.mapper.UserRoleMapper;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.RegexUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.UuidUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
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

    private final MailService mailService;

    private final TemplateEngine templateEngine;

    /**
     * 根据 {@code userId} 查询用户信息。
     *
     * @param userId 用户 ID
     * @return 用户的 {@link SecurityUser} 实例
     */
    @Override
    public SecurityUser findUserById(String userId) {
        // 根据用户ID查询用户信息
        Assert.notBlank(userId, ExpMessageCode.of("valid.user.id.NotNull"));
        User user = userMapper.selectUserById(Long.parseLong(userId));
        Assert.notNull(user, ExpMessageCode.of("valid.user.not.exist"));
        // 设置角色和权限信息
        setRoleAndPerm(user);
        return user;
    }

    /**
     * 根据用户名 {@code username} 查询用户信息。
     *
     * @param username 用户名
     * @return 用户的 {@link SecurityUser} 实例
     */
    @Override
    public SecurityUser findUserByUsername(String username) {
        // 根据用户ID查询用户信息
        Assert.notBlank(username, ExpMessageCode.of("valid.user.username.NotBlank"));
        User user = userMapper.selectUserByUsername(username);
        Assert.notNull(user, ExpMessageCode.of("valid.user.not.exist"));

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
        Assert.notNull(user, ExpMessageCode.of("valid.user.not.exist"));
        Long userId = user.getId();

        // 获取用户登录信息
        UserLoginLog loginLog = new UserLoginLog();
        String loginTime = DateUtils.formatNow(DateUtils.YMD_HMS_PATTERN);
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
            userMapper.updateUserLoginTime(userId, loginTime, 0);
        } else if (type == 2) {
            // 登录失败，使用了错误的密码
            loginLog.setLoginType(LoginType.PASSWORD.getType());
            loginLog.setIsSuccess(false);
            loginLog.setRemark("用户登录失败，使用了错误的密码");
            userMapper.updateUserLoginTime(userId, null, null);

            // TODO 如果用户登录失败超过了指定次数，则锁定用户，功能后续完善 at 2025/10/19 by SunTao
            int errorCount = userMapper.selectUserLoginErrorCount(userId);
            if (errorCount > 20) {
                userMapper.updateUserStatus(userId, UserStatus.LOCKED.getStatus());
            }
        } else if (type == 3) {
            // 登录失败，使用不存在的用户名，DO NOTHING
            return;
        } else {
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
    public PageInfo<UserVo> queryUserListByPage(UserQueryDto queryDto) {
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
        try (Page<UserVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<UserVo> dataList = userMapper.selectUserList(queryDto);
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }

    /**
     * 查询用户详情
     *
     * @param userId 用户序列
     * @return UserVo
     */
    @Override
    public UserVo queryUserDetail(Long userId) {
        Assert.notNull(userId, ExpMessageCode.of("valid.user.id.NotNull"));
        UserVo userVo = userMapper.selectUserDetail(userId);
        Assert.notNull(userVo, ExpMessageCode.of("valid.user.not.exist"));
        return userVo;
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
        Assert.isNull(user.getId(), ExpMessageCode.of("valid.user.add.idIsNull"));
        // 普通用户无法新增管理员用户
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        if (UserType.ADMIN_USER.getType().equals(user.getUserType())) {
            boolean isAdminUser = UserType.ADMIN_USER.getType().equals(loginUser.getUserType());
            Assert.isTrue(!isAdminUser, ExpMessageCode.of("valid.user.norForbidAddAdmin"));
        }
        // 设置用户默认值
        Long userId = SequenceUtils.getId();
        user.setId(userId);
        user.setGender(Gender.HIDE.getType());
        user.setNickname(user.getUsername());
        String password = UuidUtils.gen8BitUuidStr().toLowerCase(Locale.ROOT);
        user.setPassword(passwordEncoder.encode(password));
        // 用户信息检查
        checkUser(user);
        // 新增用户
        Assert.isTrue(userMapper.insert(user) != 1, ExpMessageCode.of("result.user.add.failed"));
        // 保存角色信息
        insertUserRoleData(userId, userDto.getRoleIds());
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
        Assert.notNull(dbUser, ExpMessageCode.of("valid.user.not.exist"));
        // 普通用户无法修改管理员用户
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        if (UserType.ADMIN_USER.getType().equals(user.getUserType())) {
            boolean isAdminUser = UserType.ADMIN_USER.getType().equals(loginUser.getUserType());
            Assert.isTrue(!isAdminUser, ExpMessageCode.of("valid.user.norForbidEditAdmin"));
        }
        // 判断用户名是否被修改
        Assert.isEquals(dbUser.getUsername(), user.getUsername(), ExpMessageCode.of("valid.user.username.notAllowedEdit"));
        // 用户信息检查
        checkUser(user);
        // 编辑用户
        Assert.isTrue(userMapper.updateById(user) != 1, ExpMessageCode.of("result.user.edit.failed"));
        // 保存角色信息
        insertUserRoleData(user.getId(), userDto.getRoleIds());
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
        Assert.notNull(dbUser, ExpMessageCode.of("valid.user.not.exist"));
        // 不允许删除自己
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        Assert.isNoEquals(userId, loginUser.getId(), ExpMessageCode.of("valid.user.forbidDelSelf"));
        // 普通用户无法删除管理员用户
        if (UserType.ADMIN_USER.getType().equals(dbUser.getUserType())) {
            boolean isAdminUser = UserType.ADMIN_USER.getType().equals(loginUser.getUserType());
            Assert.isTrue(!isAdminUser, ExpMessageCode.of("valid.user.norForbidDelAdmin"));
        }
        // 无法删除默认管理员用户
        Assert.isTrue(Constants.SUPER_USER_ID.equals(userId), ExpMessageCode.of("valid.user.forbidDelSuperAdmin"));
        // 删除用户
        Assert.isTrue(userMapper.deleteById(userId) != 1, ExpMessageCode.of("result.user.delete.failed"));
        // 删除角色信息
        insertUserRoleData(userId, null);
        // 清除用户缓存信息
        SecurityUserCache.remove(String.valueOf(userId));
    }

















//    private final UserRoleMapper userRoleMapper;
//
//    private final LogMapper logMapper;
//
//    private final UserPassLogMapper userPassLogMapper;
//
//    private final UserTokenStorageMapper userTokenStorageMapper;
//
//    /**
//     * 修改个人头像
//     *
//     * @param updateDto 用户信息
//     * @return boolean
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean updateUserHeadImg(UserDto updateDto) {
//        Long userId = updateDto.getId();
//        Assert.notNull(userId, "参数【id】不能为空");
//
//        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
//        wrapper.set(User::getHeadImgUrl, updateDto.getHeadImg());
//        wrapper.eq(User::getId, userId);
//        UserCacheHandler.remove(String.valueOf(userId));
//        return wrapper.update();
//    }
//
//    /**
//     * 修改个人基础信息
//     *
//     * @param updateDto 用户信息
//     * @return boolean
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean updateUserBaseInfo(UserDto updateDto) {
//        Long userId = updateDto.getId();
//        Assert.notNull(userId, "用户编码不能为空");
//
//        Integer gender = updateDto.getGender();
//        Assert.notNull(Gender.of(gender), "用户性别参数错误");
//
//        String telephone = updateDto.getTelephone();
//        DataValidated.isTrue(StringUtils.isNotBlank(telephone) && !RegexUtils.match(telephone, RegexUtils.Const.PHONE_REGEX), "手机号格式不正确");
//
//        // 检查昵称是否存在
//        User dbUser = userMapper.selectByNickname(updateDto.getNickname());
//        DataValidated.isTrue(dbUser != null && !dbUser.getId().equals(userId), "昵称已存在");
//
//        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
//        wrapper.set(User::getGender, gender);
//        wrapper.set(User::getNickname, updateDto.getNickname());
//        wrapper.set(User::getSignature, updateDto.getSignature());
//        wrapper.set(User::getWebsite, updateDto.getWebsite());
//        wrapper.set(User::getTelephone, telephone);
//        wrapper.eq(User::getId, userId);
//        UserCacheHandler.remove(String.valueOf(userId));
//        return wrapper.update();
//    }
//
//    /**
//     * 修改个人密码
//     *
//     * @param passDto 用户密码信息
//     * @return boolean
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean updateUserPass(UserUpdatePassDto passDto) {
//        passDto.setUsername(SecurityUtils.getLoginUsername());
//        preDoUpdateUserPass(passDto);
//        return true;
//    }
//
//    /**
//     * 强制修改密码
//     *
//     * @param passDto 用户密码信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void forceUpdatePass(UserUpdatePassDto passDto) {
//        preDoUpdateUserPass(passDto);
//    }
//
//    /**
//     * 修改个人邮箱
//     *
//     * @param emailDto 用户邮箱信息
//     * @return boolean
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean updateUserEmail(UserUpdateEmailDto emailDto) {
//        // 非空校验
//        String password = emailDto.getPass();
//        Assert.isTrue(StringUtils.isBlank(password), "修改失败，用户密码不能为空");
//        String email = emailDto.getEmail();
//        Assert.isTrue(StringUtils.isBlank(email), "新邮箱不能为空");
//        String code = emailDto.getCode();
//        Assert.isTrue(StringUtils.isBlank(code), "邮箱验证码不能为空");
//
//        // 获取用户信息
//        User user = queryUserByUsername(SecurityUtils.getLoginUsername());
//        Assert.notNull(user, "修改失败，用户不存在");
//
//        // 校验用户密码
//        DataValidated.isTrue(!passwordEncoder.matches(password, user.getPassword()), "密码不正确");
//
//        // 校验验证码
//        String cacheCode = (String) GlobalCache.get(emailDto.getUuid());
//        DataValidated.isTrue(!code.equalsIgnoreCase(cacheCode), "验证码不正确");
//
//        // 邮箱校验：相同校验 -> 格式校验 -> 存在性校验
//        Long userId = user.getId();
//        DataValidated.isTrue(email.equals(user.getEmail()), "新邮箱不能与旧邮箱相同");
//        DataValidated.isTrue(!RegexUtils.match(email, RegexUtils.Const.EMAIL_REGEX), "邮箱格式不正确");
//        DataValidated.isTrue(userMapper.exists(queryBuild().eq(User::getEmail, email).ne(User::getId, userId)), "邮箱已存在");
//
//        // 数据入库
//        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
//        wrapper.set(User::getEmail, email);
//        wrapper.eq(User::getId, userId);
//        Assert.isTrue(!wrapper.update(), "修改失败");
//        UserCacheHandler.remove(String.valueOf(userId));
//        return true;
//    }
//
//    /**
//     * 修改用户的登录信息
//     *
//     * @param request 请求信息
//     * @param userId  用户编号
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateUserLoginInfo(HttpServletRequest request, Long userId) {
//        // 获取登录用户信息
//        String username = SecurityUtils.getLoginUsername();
//        String dateTime = DateUtils.formatNow();
//
//        // 插入登录日志
//        Log loginLog = new Log();
//        loginLog.setId(SequenceUtils.getId());
//        loginLog.setLogType(LogType.OPERATE.getValue());
//        loginLog.setDescription("用户登录");
//        loginLog.setCostTime(null);
//        loginLog.setOperateUser(username);
//        loginLog.setEventTime(DateUtils.formatNow());
//        loginLog.setRequestInfo(request);
//        loginLog.setExecResult(true);
//        loginLog.setExecMessage("登录成功");
//        Assert.isTrue(logMapper.insert(loginLog) != 1, "插入登录日志失败");
//
//        // 数据入库
//        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
//        wrapper.set(User::getLoginTime, dateTime);
//        wrapper.eq(User::getUsername, username);
//        Assert.isTrue(!wrapper.update(), "更新用户登录信息失败");
//        UserCacheHandler.remove(String.valueOf(userId));
//    }
//
//    /**
//     * 记录用户令牌信息
//     *
//     * @param tokenStorage token
//     * @param flag 1: 登录；2：续签
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void storageUserToken(UserTokenStorage tokenStorage, int flag) {
//        int rowNum;
//        if (1 == flag) {
//            tokenStorage.setCreateTime(DateUtils.formatNow());
//            userTokenStorageMapper.deleteUserTokenStorage(tokenStorage.getUserId());
//            rowNum = userTokenStorageMapper.insertUserTokenStorage(tokenStorage);
//        } else {
//            tokenStorage.setUpdateTime(DateUtils.formatNow());
//            rowNum = userTokenStorageMapper.updateUserTokenStorage(tokenStorage);
//        }
//
//        Assert.isTrue(rowNum != 1, "持久化用户令牌信息失败");
//    }
//
//    /**
//     * 查询用户持久化的令牌信息
//     *
//     * @param userId 用户序列
//     * @return UserTokenStorage
//     */
//    @Override
//    public UserTokenStorage queryUserStorageToken(Long userId) {
//        return userTokenStorageMapper.selectUserTokenStorageByUserId(userId);
//    }
//
//    /**
//     * 清除用户持久化的令牌信息
//     *
//     * @param userId 用户序列
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void clearUserStorageToken(Long userId) {
//        userTokenStorageMapper.deleteUserTokenStorage(userId);
//    }
//
//    /**
//     * 用户注册
//     *
//     * @param registerDto 注册信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void userRegister(UserRegisterDto registerDto) {
//        // 验空
//        Assert.isTrue(StringUtils.isBlank(registerDto.getUsername()), "用户名不能为空");
//        Assert.isTrue(StringUtils.isBlank(registerDto.getEmail()), "邮箱不能为空");
//        Assert.isTrue(StringUtils.isBlank(registerDto.getCode()), "邮箱验证码不能为空");
//
//        // 邮箱验证码校验
//        String uuid = registerDto.getUuid();
//        Assert.isTrue(StringUtils.isBlank(uuid), "验证码错误");
//        String cacheCode = (String) GlobalCache.get(uuid);
//        DataValidated.isTrue(!registerDto.getCode().equalsIgnoreCase(cacheCode), "验证码不正确");
//
//        // 密码复杂度校验
//        String password = registerDto.getPassword().trim();
//        Assert.isTrue(StringUtils.isBlank(password), "密码不能为空");
//        DataValidated.isTrue(password.contains(registerDto.getUsername()), "密码不能包含用户名");
//        DataValidated.isTrue(!RegexUtils.match(password, RegexUtils.Const.PWD_REGEX), "密码复杂度太低");
//
//        // 设置用户信息，新增用户
//        User user = new User();
//        Long userId = SequenceUtils.getId();
//        user.setId(userId);
//        user.setUsername(registerDto.getUsername().trim());
//        user.setNickname("用户" + userId);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setEmail(registerDto.getEmail().trim());
//        user.setGender(Gender.HIDE.getType());
//        user.setEnabled(true);
//        user.setTemp(false);
//        user.setAdmin(false);
//        user.setBuildIn(false);
//        user.setLocked(false);
//        user.setChangePwd(false);
//        checkUser(user);
//        Assert.isTrue(userMapper.insert(user) != 1, "新增失败");
//
//        // 保存用户 - 角色信息
//        List<Long> roleIds = new ArrayList<>();
//        roleIds.add(Constants.DEFAULT_ROLE_ID);
//        insertUserRoleData(userId, roleIds);
//    }
//
//    /**
//     * 通过邮箱修改密码
//     *
//     * @param forgetPwdDto 密码信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateUserPwdByEmail(ForgetPwdDto forgetPwdDto) {
//        // 验空
//        Assert.isTrue(StringUtils.isBlank(forgetPwdDto.getEmail()), "邮箱不能为空");
//        Assert.isTrue(StringUtils.isBlank(forgetPwdDto.getCode()), "邮箱验证码不能为空");
//
//        // 通过邮箱查询用户信息
//        User user = userMapper.selectByEmail(forgetPwdDto.getEmail());
//        DataValidated.notNull(user, "邮箱未注册");
//
//        // 邮箱验证码校验
//        String uuid = forgetPwdDto.getUuid();
//        Assert.isTrue(StringUtils.isBlank(uuid), "验证码错误");
//        String cacheCode = (String) GlobalCache.get(uuid);
//        DataValidated.isTrue(!forgetPwdDto.getCode().equalsIgnoreCase(cacheCode), "验证码不正确");
//
//        // 密码校验
//        String password = forgetPwdDto.getPassword().trim();
//
//        try {
//            // 对密码进行解密
//            Resource resource = new ClassPathResource("pri.key");
//            String priKeyBase64 = FileUtils.readFileFromStream(resource.getInputStream());
//            password = RsaUtils.decryptByPrivateKey(priKeyBase64, password);
//        } catch (Exception e) {
//            throw new ServiceException("密码解析失败，请联系管理员", e);
//        }
//
//        // 密码复杂度校验
//        Assert.isTrue(StringUtils.isBlank(password), "密码不能为空");
//        DataValidated.isTrue(password.contains(user.getUsername()), "密码不能包含用户名");
//        DataValidated.isTrue(!RegexUtils.match(password, RegexUtils.Const.PWD_REGEX), "密码复杂度太低");
//
//        // 修改密码
//        doUpdateUserPass(user.getId(), password);
//    }
//
//    private void preDoUpdateUserPass(UserUpdatePassDto passDto) {
//        // 非空校验
//        Assert.isTrue(StringUtils.isBlank(passDto.getUsername()), "修改失败，用户名不能为空");
//        String oldPass = passDto.getOldPass();
//        Assert.isTrue(StringUtils.isBlank(oldPass), "修改失败，旧密码不能为空");
//        String newPass = passDto.getNewPass();
//        Assert.isTrue(StringUtils.isBlank(newPass), "新密码不能为空");
//        String ensurePass = passDto.getConfirmPass();
//        DataValidated.isTrue(!newPass.equals(ensurePass), "两次新密码设置不相同");
//
//        // 获取用户信息
//        User user = queryUserByUsername(passDto.getUsername());
//        Assert.notNull(user, "修改失败，用户不存在");
//
//        // 密码校验：旧密码是否正确 -> 新旧密码是否一致 ->
//        DataValidated.isTrue(!passwordEncoder.matches(oldPass, user.getPassword()), "旧密码错误");
//        DataValidated.isTrue(passwordEncoder.matches(newPass, user.getPassword()), "新密码与旧密码不能相同");
//
//        // 密码历史校验
//        Long userId = user.getId();
//        List<String> oldPassList = userPassLogMapper.selectUserOldPass(userId, OLD_PASS_COUNT);
//        oldPassList.forEach(pass -> DataValidated.isTrue(newPass.equals(pass), "新密码不能与历史近三次使用过的密码相同"));
//
//        // 密码复杂度校验
//        DataValidated.isTrue(newPass.contains(user.getUsername()), "密码不能包含用户名");
//        DataValidated.isTrue(!RegexUtils.match(newPass, RegexUtils.Const.PWD_REGEX), "密码复杂度太低");
//
//        // 修改密码
//        doUpdateUserPass(user.getId(), newPass);
//    }
//
//    private void doUpdateUserPass(Long userId, String password) {
//        // 修改用户密码
//        String updateTime = DateUtils.formatNow();
//        UserPassLog passLog = UserPassLog.builder().id(SequenceUtils.getId()).userId(userId).password(password).updateTime(updateTime).build();
//        Assert.isTrue(userPassLogMapper.insertUserPassLog(passLog) != 1, "修改失败");
//
//        // 数据入库
//        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
//        wrapper.eq(User::getId, userId);
//        wrapper.set(User::getPassword, passwordEncoder.encode(password));
//        wrapper.set(User::getChangePwdTime, updateTime);
//        wrapper.set(User::getChangePwd, false);
//        Assert.isTrue(!wrapper.update(), "修改失败");
//        UserCacheHandler.remove(String.valueOf(userId));
//    }
//

    private void checkUser(User user) {
        // 用户名检查
        String username = user.getUsername().trim();
        user.setUsername(username);
        Assert.isTrue(!RegexUtils.match(username, RegexUtils.Regex.USERNAME_REGEX), ExpMessageCode.of("valid.user.username.patternError"));
        Assert.isTrue(checkUsernameExist(user), ExpMessageCode.of("valid.user.username.exist"));

        // 邮箱检查
        String email = user.getEmail().trim();
        user.setEmail(email);
        Assert.isTrue(!RegexUtils.match(email, RegexUtils.Regex.EMAIL_REGEX), ExpMessageCode.of("valid.user.email.patternError"));
        Assert.isTrue(checkEmailExist(user), ExpMessageCode.of("valid.user.email.exist"));

        // 昵称检查
        AtomicInteger count = new AtomicInteger(1);
        while (checkNicknameExist(user)) {
            Assert.isTrue(count.get() < Byte.MAX_VALUE, () -> {
                String newNickname = user.getNickname() + count.getAndIncrement();
                user.setNickname(newNickname);
            }, ExpMessageCode.of("valid.user.nickname.exist"));
        }

        // 账号过期时间检查
        if (UserType.TEMP_USER.getType().equals(user.getUserType())) {
            Assert.notBlank(user.getExpiredTime(), ExpMessageCode.of("valid.user.expiredTime.NotBlank"));
        } else {
            user.setExpiredTime(null);
        }
    }

    private boolean checkUsernameExist(User user) {
        if (user.getUsername() != null) {
            LambdaQueryWrapper<User> queryWrapper = queryBuild().eq(User::getUsername, user.getUsername());
            if (user.getId() != null) {
                queryWrapper.ne(User::getId, user.getId());
            }
            return userMapper.exists(queryWrapper);
        }
        return false;
    }

    private boolean checkEmailExist(User user) {
        if (user.getUsername() != null) {
            LambdaQueryWrapper<User> queryWrapper = queryBuild().eq(User::getEmail, user.getEmail());
            if (user.getId() != null) {
                queryWrapper.ne(User::getId, user.getId());
            }
            return userMapper.exists(queryWrapper);
        }
        return false;
    }

    private boolean checkNicknameExist(User user) {
        if (user.getNickname() != null) {
            LambdaQueryWrapper<User> queryWrapper = queryBuild().eq(User::getNickname, user.getNickname());
            if (user.getId() != null) {
                queryWrapper.ne(User::getId, user.getId());
            }
            return userMapper.exists(queryWrapper);
        }
        return false;
    }

    private void insertUserRoleData(Long userId, List<Long> roleIds) {
        userRoleMapper.deleteByUserId(userId);
        if (roleIds != null && roleIds.size() > 0) {
            for (Long roleId : roleIds) {
                UserRoleRelation userRole = new UserRoleRelation();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }

    private void sendAddUserNoticeEmail(String email, String username, String password) {
        try {
            Context ctx = new Context();
            ctx.setVariable("username", username);
            ctx.setVariable("password", password);
            ctx.setVariable("year", DateUtils.formatNow("yyyy"));
            String emailContext = templateEngine.process("addUserNotice", ctx);
            MailReceiver receiver = MailReceiver.setReceive(email);
            mailService.sendHtmlMail(receiver, BaseConst.SYS_NAME, emailContext);
        } catch (Exception e) {
            throw new ServiceException(500, "邮件推送失败", e);
        }
    }

    private LambdaQueryWrapper<User> queryBuild() {
        return Wrappers.lambdaQuery();
    }

//    /**
//     * 新密码不能与前几次的密码相同
//     */
//    private static final int OLD_PASS_COUNT = 3;
}
