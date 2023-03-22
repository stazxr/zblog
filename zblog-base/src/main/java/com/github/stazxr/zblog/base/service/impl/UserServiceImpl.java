package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.component.email.MailReceiveHandler;
import com.github.stazxr.zblog.base.component.email.MailService;
import com.github.stazxr.zblog.base.component.security.handler.UserCacheHandler;
import com.github.stazxr.zblog.base.converter.UserConverter;
import com.github.stazxr.zblog.base.domain.dto.*;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.entity.UserPassLog;
import com.github.stazxr.zblog.base.domain.entity.UserRoleRelation;
import com.github.stazxr.zblog.base.domain.entity.UserTokenStorage;
import com.github.stazxr.zblog.base.domain.enums.Gender;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.base.mapper.UserMapper;
import com.github.stazxr.zblog.base.mapper.UserPassLogMapper;
import com.github.stazxr.zblog.base.mapper.UserRoleMapper;
import com.github.stazxr.zblog.base.mapper.UserTokenStorageMapper;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.domain.enums.LogType;
import com.github.stazxr.zblog.log.mapper.LogMapper;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.RegexUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.UuidUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.secret.RsaUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理 - 业务实现层
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 新密码不能与前几次的密码相同
     */
    private static final int OLD_PASS_COUNT = 3;

    private final UserMapper userMapper;

    private final UserConverter userConverter;

    private final UserRoleMapper userRoleMapper;

    private final LogMapper logMapper;

    private final UserPassLogMapper userPassLogMapper;

    private final UserTokenStorageMapper userTokenStorageMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final TemplateEngine templateEngine;

    private final UserCacheHandler userCacheHandler;

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return User
     */
    @Override
    public User queryUserByUsername(String username) {
        Assert.isTrue(StringUtils.isBlank(username), "用户名不能为空");
        return userMapper.selectOne(queryBuild().eq(User::getUsername, username));
    }

    /**
     * 修改个人头像
     *
     * @param updateDto 用户信息
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserHeadImg(UserDto updateDto) {
        Long userId = updateDto.getId();
        Assert.notNull(userId, "参数【id】不能为空");

        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
        wrapper.set(User::getHeadImgUrl, updateDto.getHeadImg());
        wrapper.eq(User::getId, userId);
        userCacheHandler.removeUserFromCache(userId);
        return wrapper.update();
    }

    /**
     * 修改个人基础信息
     *
     * @param updateDto 用户信息
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserBaseInfo(UserDto updateDto) {
        Long userId = updateDto.getId();
        Assert.notNull(userId, "用户编码不能为空");

        Integer gender = updateDto.getGender();
        Assert.notNull(Gender.of(gender), "用户性别参数错误");

        String telephone = updateDto.getTelephone();
        DataValidated.isTrue(StringUtils.isNotBlank(telephone) && !RegexUtils.match(telephone, RegexUtils.Const.PHONE_REGEX), "手机号格式不正确");

        // 检查昵称是否存在
        User dbUser = userMapper.selectByNickname(updateDto.getNickname());
        DataValidated.isTrue(dbUser != null && !dbUser.getId().equals(userId), "昵称已存在");

        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
        wrapper.set(User::getGender, gender);
        wrapper.set(User::getNickname, updateDto.getNickname());
        wrapper.set(User::getSignature, updateDto.getSignature());
        wrapper.set(User::getWebsite, updateDto.getWebsite());
        wrapper.set(User::getTelephone, telephone);
        wrapper.eq(User::getId, userId);
        userCacheHandler.removeUserFromCache(userId);
        return wrapper.update();
    }

    /**
     * 修改个人密码
     *
     * @param passDto 用户密码信息
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserPass(UserUpdatePassDto passDto) {
        passDto.setUsername(SecurityUtils.getLoginUsername());
        preDoUpdateUserPass(passDto);
        return true;
    }

    /**
     * 强制修改密码
     *
     * @param passDto 用户密码信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forceUpdatePass(UserUpdatePassDto passDto) {
        preDoUpdateUserPass(passDto);
    }

    /**
     * 修改个人邮箱
     *
     * @param emailDto 用户邮箱信息
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserEmail(UserUpdateEmailDto emailDto) {
        // 非空校验
        String password = emailDto.getPass();
        Assert.isTrue(StringUtils.isBlank(password), "修改失败，用户密码不能为空");
        String email = emailDto.getEmail();
        Assert.isTrue(StringUtils.isBlank(email), "新邮箱不能为空");
        String code = emailDto.getCode();
        Assert.isTrue(StringUtils.isBlank(code), "邮箱验证码不能为空");

        // 获取用户信息
        User user = queryUserByUsername(SecurityUtils.getLoginUsername());
        Assert.notNull(user, "修改失败，用户不存在");

        // 校验用户密码
        DataValidated.isTrue(!passwordEncoder.matches(password, user.getPassword()), "密码不正确");

        // 校验验证码
        String cacheCode = CacheUtils.get(emailDto.getUuid());
        DataValidated.isTrue(!code.equalsIgnoreCase(cacheCode), "验证码不正确");

        // 邮箱校验：相同校验 -> 格式校验 -> 存在性校验
        Long userId = user.getId();
        DataValidated.isTrue(email.equals(user.getEmail()), "新邮箱不能与旧邮箱相同");
        DataValidated.isTrue(!RegexUtils.match(email, RegexUtils.Const.EMAIL_REGEX), "邮箱格式不正确");
        DataValidated.isTrue(userMapper.exists(queryBuild().eq(User::getEmail, email).ne(User::getId, userId)), "邮箱已存在");

        // 数据入库
        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
        wrapper.set(User::getEmail, email);
        wrapper.eq(User::getId, userId);
        Assert.isTrue(!wrapper.update(), "修改失败");
        userCacheHandler.removeUserFromCache(userId);
        return true;
    }

    /**
     * 修改用户的登录信息
     *
     * @param request 请求信息
     * @param userId  用户编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserLoginInfo(HttpServletRequest request, Long userId) {
        // 获取登录用户信息
        String username = SecurityUtils.getLoginUsername();
        String dateTime = DateUtils.formatNow();

        // 插入登录日志
        Log loginLog = new Log();
        loginLog.setId(GenerateIdUtils.getId());
        loginLog.setLogType(LogType.OPERATE.getValue());
        loginLog.setDescription("用户登录");
        loginLog.setCostTime(null);
        loginLog.setOperateUser(username);
        loginLog.setEventTime(DateUtils.formatNow());
        loginLog.setRequestInfo(request);
        loginLog.setExecResult(true);
        loginLog.setExecMessage("登录成功");
        Assert.isTrue(logMapper.insert(loginLog) != 1, "插入登录日志失败");

        // 数据入库
        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
        wrapper.set(User::getLoginTime, dateTime);
        wrapper.eq(User::getUsername, username);
        Assert.isTrue(!wrapper.update(), "更新用户登录信息失败");
        userCacheHandler.removeUserFromCache(userId);
    }

    /**
     * 记录用户令牌信息
     *
     * @param tokenStorage token
     * @param flag 1: 登录；2：续签
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void storageUserToken(UserTokenStorage tokenStorage, int flag) {
        int rowNum;
        if (1 == flag) {
            tokenStorage.setCreateTime(DateUtils.formatNow());
            userTokenStorageMapper.deleteUserTokenStorage(tokenStorage.getUserId());
            rowNum = userTokenStorageMapper.insertUserTokenStorage(tokenStorage);
        } else {
            tokenStorage.setUpdateTime(DateUtils.formatNow());
            rowNum = userTokenStorageMapper.updateUserTokenStorage(tokenStorage);
        }

        Assert.isTrue(rowNum != 1, "持久化用户令牌信息失败");
    }

    /**
     * 查询用户持久化的令牌信息
     *
     * @param userId 用户序列
     * @return UserTokenStorage
     */
    @Override
    public UserTokenStorage queryUserStorageToken(Long userId) {
        return userTokenStorageMapper.selectUserTokenStorageByUserId(userId);
    }

    /**
     * 查询用户列表
     *
     * @param queryDto 查询参数
     * @return userList
     */
    @Override
    public PageInfo<UserVo> queryUserListByPage(UserQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(userMapper.selectUserList(queryDto));
    }

    /**
     * 查询用户详情
     *
     * @param userId 用户序列
     * @return UserVo
     */
    @Override
    public UserVo queryUserDetail(Long userId) {
        Assert.notNull(userId, "参数【userId】不能为空");
        return userMapper.selectUserDetail(userId);
    }

    /**
     * 新增用户
     *
     * @param userDto 用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserDto userDto) {
        userDto.setId(null);
        User user = userConverter.dtoToEntity(userDto);
        checkUser(user);

        // 设置默认值
        Long userId = GenerateIdUtils.getId();
        user.setId(userId);
        user.setAdmin(false);
        user.setBuildIn(false);
        user.setLocked(false);
        user.setChangePwd(true);
        user.setGender(Gender.HIDE.getType());
        user.setNickname("用户" + userId);
        String password = UuidUtils.generateShortUuid();
        user.setPassword(passwordEncoder.encode(password));

        // 保存用户 - 角色信息
        Assert.isTrue(userMapper.insert(user) != 1, "新增失败");
        insertUserRoleData(userId, userDto.getRoleIds());

        // 邮件通知（邮件发送失败也回滚，因为没有人知道新用户密码是多少）
        sendAddUserNoticeEmail(user.getEmail(), user.getUsername(), password);
        log.info("新增用户[{}]成功，初始密码为：{}", user.getUsername(), password);
    }

    /**
     * 编辑用户
     *
     * @param userDto 用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(UserDto userDto) {
        Assert.notNull(userDto.getId(), "参数【id】不能为空");
        User user = userConverter.dtoToEntity(userDto);
        checkUser(user);

        // 保存用户 - 角色信息
        user.setUsername(null);
        Assert.isTrue(userMapper.updateById(user) != 1, "修改失败");
        insertUserRoleData(user.getId(), userDto.getRoleIds());
        userCacheHandler.removeUserFromCache(user.getId());
    }

    /**
     * 删除用户
     *
     * @param userId 用户序列
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        // 不允许删除自己
        Assert.notNull(userId, "参数【userId】不能为空");
        Long loginId = SecurityUtils.getLoginId();
        DataValidated.isTrue(userId.equals(loginId), "自己无法删除自己");

        // 管理员、测试用户不允许删除
        DataValidated.isTrue(Constants.USER_ADMIN_ID.equals(userId), "无法删除管理员");
        DataValidated.isTrue(Constants.USER_TEST_ID.equals(userId), "无法删除测试用户");

        User user = userMapper.selectById(userId);
        Assert.notNull(user, "查询用户信息失败，用户【" + userId + "】不存在");
        DataValidated.notNull(user.getBuildIn(), "用户【" + user.getUsername() + "】为内置用户，不允许删除");
        Assert.isTrue(userMapper.deleteById(userId) != 1, "删除失败");
        userRoleMapper.deleteByUserId(userId);
        userCacheHandler.removeUserFromCache(userId);
    }

    /**
     * 更新用户状态
     *
     * @param user 用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(UserDto user) {
        Assert.notNull(user.getId(), "参数【id】不能为空");
        Assert.notNull(user.getEnabled(), "参数【enabled】不能为空");
        boolean update = lambdaUpdate().set(User::isEnabled, user.getEnabled()).eq(User::getId, user.getId()).update();
        Assert.isTrue(!update, "修改失败");
        userCacheHandler.removeUserFromCache(user.getId());
    }

    /**
     * 用户注册
     *
     * @param registerDto 注册信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userRegister(UserRegisterDto registerDto) {
        // 验空
        Assert.isTrue(StringUtils.isBlank(registerDto.getUsername()), "用户名不能为空");
        Assert.isTrue(StringUtils.isBlank(registerDto.getEmail()), "邮箱不能为空");
        Assert.isTrue(StringUtils.isBlank(registerDto.getCode()), "邮箱验证码不能为空");

        // 邮箱验证码校验
        String uuid = registerDto.getUuid();
        Assert.isTrue(StringUtils.isBlank(uuid), "验证码错误");
        String cacheCode = CacheUtils.get(uuid);
        DataValidated.isTrue(!registerDto.getCode().equalsIgnoreCase(cacheCode), "验证码不正确");

        // 密码复杂度校验
        String password = registerDto.getPassword().trim();
        Assert.isTrue(StringUtils.isBlank(password), "密码不能为空");
        DataValidated.isTrue(password.contains(registerDto.getUsername()), "密码不能包含用户名");
        DataValidated.isTrue(!RegexUtils.match(password, RegexUtils.Const.PWD_REGEX), "密码复杂度太低");

        // 设置用户信息，新增用户
        User user = new User();
        Long userId = GenerateIdUtils.getId();
        user.setId(userId);
        user.setUsername(registerDto.getUsername().trim());
        user.setNickname("用户" + userId);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(registerDto.getEmail().trim());
        user.setGender(Gender.HIDE.getType());
        user.setEnabled(true);
        user.setTemp(false);
        user.setAdmin(false);
        user.setBuildIn(false);
        user.setLocked(false);
        user.setChangePwd(false);
        checkUser(user);
        Assert.isTrue(userMapper.insert(user) != 1, "新增失败");

        // 保存用户 - 角色信息
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(Constants.DEFAULT_ROLE_ID);
        insertUserRoleData(userId, roleIds);
    }

    /**
     * 通过邮箱修改密码
     *
     * @param forgetPwdDto 密码信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPwdByEmail(ForgetPwdDto forgetPwdDto) {
        // 验空
        Assert.isTrue(StringUtils.isBlank(forgetPwdDto.getEmail()), "邮箱不能为空");
        Assert.isTrue(StringUtils.isBlank(forgetPwdDto.getCode()), "邮箱验证码不能为空");

        // 通过邮箱查询用户信息
        User user = userMapper.selectByEmail(forgetPwdDto.getEmail());
        DataValidated.notNull(user, "邮箱未注册");

        // 邮箱验证码校验
        String uuid = forgetPwdDto.getUuid();
        Assert.isTrue(StringUtils.isBlank(uuid), "验证码错误");
        String cacheCode = CacheUtils.get(uuid);
        DataValidated.isTrue(!forgetPwdDto.getCode().equalsIgnoreCase(cacheCode), "验证码不正确");

        // 密码校验
        String password = forgetPwdDto.getPassword().trim();

        try {
            // 对密码进行解密
            Resource resource = new ClassPathResource("pri.key");
            String priKeyBase64 = FileUtils.readFileFromStream(resource.getInputStream());
            password = RsaUtils.decryptByPrivateKey(priKeyBase64, password);
        } catch (Exception e) {
            throw new ServiceException("密码解析失败，请联系管理员", e);
        }

        // 密码复杂度校验
        Assert.isTrue(StringUtils.isBlank(password), "密码不能为空");
        DataValidated.isTrue(password.contains(user.getUsername()), "密码不能包含用户名");
        DataValidated.isTrue(!RegexUtils.match(password, RegexUtils.Const.PWD_REGEX), "密码复杂度太低");

        // 修改密码
        doUpdateUserPass(user.getId(), password);
    }

    private void preDoUpdateUserPass(UserUpdatePassDto passDto) {
        // 非空校验
        Assert.isTrue(StringUtils.isBlank(passDto.getUsername()), "修改失败，用户名不能为空");
        String oldPass = passDto.getOldPass();
        Assert.isTrue(StringUtils.isBlank(oldPass), "修改失败，旧密码不能为空");
        String newPass = passDto.getNewPass();
        Assert.isTrue(StringUtils.isBlank(newPass), "新密码不能为空");
        String ensurePass = passDto.getConfirmPass();
        DataValidated.isTrue(!newPass.equals(ensurePass), "两次新密码设置不相同");

        // 获取用户信息
        User user = queryUserByUsername(passDto.getUsername());
        Assert.notNull(user, "修改失败，用户不存在");

        // 密码校验：旧密码是否正确 -> 新旧密码是否一致 ->
        DataValidated.isTrue(!passwordEncoder.matches(oldPass, user.getPassword()), "旧密码错误");
        DataValidated.isTrue(passwordEncoder.matches(newPass, user.getPassword()), "新密码与旧密码不能相同");

        // 密码历史校验
        Long userId = user.getId();
        List<String> oldPassList = userPassLogMapper.selectUserOldPass(userId, OLD_PASS_COUNT);
        oldPassList.forEach(pass -> DataValidated.isTrue(newPass.equals(pass), "新密码不能与历史近三次使用过的密码相同"));

        // 密码复杂度校验
        DataValidated.isTrue(newPass.contains(user.getUsername()), "密码不能包含用户名");
        DataValidated.isTrue(!RegexUtils.match(newPass, RegexUtils.Const.PWD_REGEX), "密码复杂度太低");

        // 修改密码
        doUpdateUserPass(user.getId(), newPass);
    }

    private void doUpdateUserPass(Long userId, String password) {
        // 修改用户密码
        String updateTime = DateUtils.formatNow();
        UserPassLog passLog = UserPassLog.builder().id(GenerateIdUtils.getId()).userId(userId).password(password).updateTime(updateTime).build();
        Assert.isTrue(userPassLogMapper.insertUserPassLog(passLog) != 1, "修改失败");

        // 数据入库
        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
        wrapper.eq(User::getId, userId);
        wrapper.set(User::getPassword, passwordEncoder.encode(password));
        wrapper.set(User::getChangePwdTime, updateTime);
        wrapper.set(User::getChangePwd, false);
        Assert.isTrue(!wrapper.update(), "修改失败");
        userCacheHandler.removeUserFromCache(userId);
    }

    private void insertUserRoleData(Long userId, List<Long> roleIds) {
        userRoleMapper.deleteByUserId(userId);
        if (roleIds != null && roleIds.size() > 0) {
            for (Long roleId : roleIds) {
                UserRoleRelation userRole = new UserRoleRelation();
                userRole.setId(GenerateIdUtils.getId());
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
            MailReceiveHandler handler = MailReceiveHandler.setReceive(email);
            mailService.sendHtmlMail(handler, BaseConst.SYS_NAME, emailContext);
        } catch (Exception e) {
            throw new ServiceException(500, "邮件推送失败", e);
        }
    }

    private void checkUser(User user) {
        DataValidated.notNull(user.getUsername(), "用户名不能为空");
        DataValidated.notNull(user.getEmail(), "用户邮箱不能为空");
        DataValidated.notNull(user.isEnabled(), "用户状态不能为空");
        DataValidated.notNull(user.getTemp(), "用户类型不能为空");

        // 用户名格式检查
        String username = user.getUsername();
        DataValidated.isTrue(!RegexUtils.match(username, RegexUtils.Const.USERNAME_REGEX), "用户名只允许包含数字和字母, 且长度范围要求为[4, 20]");

        // 邮箱格式检查
        String email = user.getEmail();
        DataValidated.isTrue(!RegexUtils.match(email, RegexUtils.Const.EMAIL_REGEX), "邮箱格式不正确");

        // 检查邮箱是否存在
        LambdaQueryWrapper<User> wrapper = queryBuild().eq(User::getEmail, email);
        if (user.getId() != null) {
            wrapper.ne(User::getId, user.getId());
        }
        DataValidated.isTrue(userMapper.exists(wrapper), "邮箱已存在");

        // 检查用户名是否存在（包含删除的，用户名全局不允许重复）
        user.setUsername(user.getUsername().trim());
        User dbUser = userMapper.selectByUsername(user.getUsername());
        DataValidated.isTrue(dbUser != null && !dbUser.getId().equals(user.getId()), "用户名已存在");

        // 正式用户需要置空账号过期时间
        if (!user.getTemp()) {
            user.setExpiredTime(null);
        }
    }

    private LambdaQueryWrapper<User> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
