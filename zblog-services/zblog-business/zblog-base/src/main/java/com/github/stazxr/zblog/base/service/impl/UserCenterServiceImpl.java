package com.github.stazxr.zblog.base.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.stazxr.zblog.bas.encryption.util.RsaUtils;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.notify.mail.MailService;
import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateHeadImgDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdatePassDto;
import com.github.stazxr.zblog.base.domain.entity.FileRelation;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.entity.UserPassLog;
import com.github.stazxr.zblog.base.domain.enums.BasUploadBusinessType;
import com.github.stazxr.zblog.base.mapper.FileMapper;
import com.github.stazxr.zblog.base.mapper.FileRelationMapper;
import com.github.stazxr.zblog.base.mapper.UserMapper;
import com.github.stazxr.zblog.base.mapper.UserPassLogMapper;
import com.github.stazxr.zblog.base.service.UserCenterService;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.RegexUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;

import java.util.List;
import java.util.Locale;

/**
 * 用户中心管理业务实现层
 *
 * @author SunTao
 * @since 2025-12-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCenterServiceImpl implements UserCenterService {
    private final UserMapper userMapper;

    private final UserPassLogMapper userPassLogMapper;

    private final FileMapper fileMapper;

    private final FileRelationMapper fileRelationMapper;

    private final PasswordEncoder passwordEncoder;

    private final SecurityExtProperties securityExtProperties;

    private final MailService mailService;

    private final TemplateEngine templateEngine;

    @Value("${zblog.security.PrivateKey}")
    private String secretKey;

    /**
     * 强制修改密码
     *
     * @param passDto 用户密码信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forceUpdatePass(UserUpdatePassDto passDto) {
        // 修改密码
        preDoUpdateUserPass(passDto);
    }

    /**
     * 修改个人密码
     *
     * @param passDto 用户密码信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPass(UserUpdatePassDto passDto) {
        // 获取当前登录用户信息
        User loginUser = SecurityUtils.getLoginUser();
        passDto.setUsername(loginUser.getUsername());
        // 修改密码
        preDoUpdateUserPass(passDto);
    }

    /**
     * 修改个人头像
     *
     * @param headImgDto 用户头像信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserHeadImg(UserUpdateHeadImgDto headImgDto) {
        Long userId = headImgDto.getUserId();
        Assert.notNull(userId, ExpMessageCode.of("valid.usercenter.userId.NotNull"));
        Long fileId = headImgDto.getFileId();
        Assert.notNull(fileId, ExpMessageCode.of("valid.usercenter.fileId.NotNull"));
        Assert.affectOneRow(userMapper.updateUserHeadImg(userId, headImgDto.getHeadImg()), ExpMessageCode.of("result.usercenter.headImg.failed"));
        // 删除原头像信息（物理文件不删除，通过跑批删除）
        fileMapper.deleteByBusinessInfo(userId, BasUploadBusinessType.HEADER_IMG);
        fileRelationMapper.deleteByBusinessInfo(userId, BasUploadBusinessType.HEADER_IMG);
        // 插入新的头像关联信息
        FileRelation fileRelation = new FileRelation();
        fileRelation.setFileId(fileId);
        fileRelation.setBusinessId(userId);
        fileRelation.setBusinessType(BasUploadBusinessType.HEADER_IMG);
        fileRelationMapper.insert(fileRelation);
        SecurityUserCache.remove(String.valueOf(userId));
    }

    private void preDoUpdateUserPass(UserUpdatePassDto passDto) {
        // 获取参数信息
        UserUpdatePassDto realPassDto;
        try {
            String jsonStr = RsaUtils.decryptByPrivateKey(secretKey, passDto.get_f());
            realPassDto = JSON.parseObject(jsonStr, UserUpdatePassDto.class);
        } catch (Exception e) {
            throw new ServiceException(ExpMessageCode.of("error.usercenter.password.decryptExp"));
        }

        // 空校验
        String username = realPassDto.getUsername();
        Assert.notBlank(username, ExpMessageCode.of("valid.usercenter.username.NotBlank"));
        String oldPass = realPassDto.getOldPass();
        Assert.notBlank(oldPass, ExpMessageCode.of("valid.usercenter.oldPass.NotBlank"));
        String newPass = realPassDto.getNewPass();
        Assert.notBlank(newPass, ExpMessageCode.of("valid.usercenter.newPass.NotBlank"));
        String confirmPass = realPassDto.getConfirmPass();
        Assert.notBlank(confirmPass, ExpMessageCode.of("valid.usercenter.confirmPass.NotBlank"));
        // 校验新密码输入是否一致
        Assert.failIfFalse(newPass.equals(confirmPass), ExpMessageCode.of("valid.usercenter.confirmPass.notMatch"));
        // 获取用户信息
        User user = userMapper.selectUserByUsername(username);
        Assert.notNull(user, ExpMessageCode.of("valid.usercenter.user.NotExist"));
        // 原密码是否正确
        boolean oldMatches = passwordEncoder.matches(oldPass, user.getPassword());
        Assert.failIfFalse(oldMatches, ExpMessageCode.of("valid.usercenter.oldPass.notMatch"));
        // 新旧密码不能一致
        boolean newMatches = passwordEncoder.matches(newPass, user.getPassword());
        Assert.failIfTrue(newMatches, ExpMessageCode.of("valid.usercenter.samePassword"));
        // 密码复杂度校验
        boolean containUsername = newPass.toLowerCase(Locale.ROOT).contains(username.toLowerCase(Locale.ROOT));
        Assert.failIfTrue(containUsername, ExpMessageCode.of("valid.usercenter.passwordContainUsername"));
        Assert.failIfFalse(RegexUtils.match(newPass, RegexUtils.Regex.NEW_PWD_REGEX), ExpMessageCode.of("valid.usercenter.passwordSimple"));
        // 密码历史校验
        int passwordHistoryCount = securityExtProperties.getPasswordHistoryCount();
        if (passwordHistoryCount > 0) {
            List<String> historyPassList = userPassLogMapper.selectUserHistoryPass(user.getId(), passwordHistoryCount);
            for (String historyPass : historyPassList) {
                boolean historyMatches = passwordEncoder.matches(newPass, historyPass);
                Assert.failIfTrue(historyMatches, ExpMessageCode.of("valid.usercenter.matchHistoryPass", passwordHistoryCount));
            }
        }
        // 修改密码
        doUpdateUserPass(user.getId(), newPass);
    }

    private void doUpdateUserPass(Long userId, String password) {
        // 记录用户密码修改记录
        String changePwdTime = DateUtils.formatNow(DateUtils.YMD_HMS_PATTERN);
        UserPassLog userPassLog = new UserPassLog();
        userPassLog.setId(SequenceUtils.getId());
        userPassLog.setUserId(userId);
        userPassLog.setPassword(passwordEncoder.encode(password));
        userPassLog.setChangePwdTime(changePwdTime);
        Assert.affectOneRow(userPassLogMapper.insert(userPassLog), ExpMessageCode.of("result.usercenter.password.failed"));
        // 数据入库
        String newPassword = passwordEncoder.encode(password);
        Assert.affectOneRow(userMapper.updateUserPassword(userId, newPassword, changePwdTime), ExpMessageCode.of("result.usercenter.password.failed"));
        SecurityUserCache.remove(String.valueOf(userId));
    }



//    private final LogMapper logMapper;
//
//    private final UserTokenStorageMapper userTokenStorageMapper;
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
}
