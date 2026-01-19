package com.github.stazxr.zblog.base.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.encryption.util.RsaUtils;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateEmailDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateHeadImgDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdatePassDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateSelfDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserLogQueryDto;
import com.github.stazxr.zblog.base.domain.entity.FileRelation;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.entity.UserPassLog;
import com.github.stazxr.zblog.base.domain.enums.BasUploadBusinessType;
import com.github.stazxr.zblog.base.domain.enums.Gender;
import com.github.stazxr.zblog.base.domain.vo.FileVo;
import com.github.stazxr.zblog.base.mapper.FileMapper;
import com.github.stazxr.zblog.base.mapper.FileRelationMapper;
import com.github.stazxr.zblog.base.mapper.UserMapper;
import com.github.stazxr.zblog.base.mapper.UserPassLogMapper;
import com.github.stazxr.zblog.base.service.UserCenterService;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import com.github.stazxr.zblog.util.RegexUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        // 获取参数信息
        UserUpdatePassDto realPassDto;
        try {
            String jsonStr = RsaUtils.decryptByPrivateKey(secretKey, passDto.get_f());
            realPassDto = JSON.parseObject(jsonStr, UserUpdatePassDto.class);
        } catch (Exception e) {
            throw new ServiceException(ExpMessageCode.of("error.usercenter.password.decryptExp"));
        }
        // 修改密码
        preDoUpdateUserPass(realPassDto);
    }

    /**
     * 修改个人密码
     *
     * @param passDto 用户密码信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPass(UserUpdatePassDto passDto) {
        // 获取参数信息
        UserUpdatePassDto realPassDto;
        try {
            String jsonStr = RsaUtils.decryptByPrivateKey(secretKey, passDto.get_f());
            realPassDto = JSON.parseObject(jsonStr, UserUpdatePassDto.class);
        } catch (Exception e) {
            throw new ServiceException(ExpMessageCode.of("error.usercenter.password.decryptExp"));
        }
        // 获取当前登录用户信息
        User loginUser = SecurityUtils.getLoginUser();
        realPassDto.setUsername(loginUser.getUsername());
        // 修改密码
        preDoUpdateUserPass(realPassDto);
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
        Long fileId = headImgDto.getFileId();
        FileVo fileVo = fileMapper.selectFileDetailById(fileId);
        Assert.notNull(fileVo, ExpMessageCode.of(""));
        Assert.affectOneRow(userMapper.updateUserHeadImg(userId, fileVo.getFileAccessUrl()), ExpMessageCode.of("result.usercenter.updateHeadImgFailed"));
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

    /**
     * 修改个人邮箱
     *
     * @param emailDto 用户邮箱信息
     */
    @Override
    public void updateUserEmail(UserUpdateEmailDto emailDto) {
        // 校验邮箱验证码
        String cacheCode = GlobalCache.get(emailDto.getUuid());
        Assert.notBlank(cacheCode, ExpMessageCode.of("valid.usercenter.updemail.codeExpired"));
        Assert.failIfFalse(emailDto.getCode().equals(cacheCode), ExpMessageCode.of("valid.usercenter.updemail.codeInvalid"));
        // 邮箱校验：相同校验 -> 格式校验 -> 存在性校验
        User loginUser = SecurityUtils.getLoginUser();
        boolean isSameOldEmail = emailDto.getEmail().equals(loginUser.getEmail());
        Assert.failIfTrue(isSameOldEmail, ExpMessageCode.of("valid.usercenter.updemail.sameOldEmail"));
        boolean emailValid = RegexUtils.match(emailDto.getEmail(), RegexUtils.Regex.EMAIL_REGEX);
        Assert.failIfFalse(emailValid, ExpMessageCode.of("valid.usercenter.updemail.emailPatternError"));
        Assert.failIfTrue(checkEmailExist(loginUser.getId(), emailDto.getEmail()), ExpMessageCode.of("valid.role.roleCode.exist"));
        // 数据入库
        Assert.affectOneRow(userMapper.updateUserEmail(loginUser.getId(), emailDto.getEmail()), ExpMessageCode.of("result.usercenter.updateEmailFailed"));
        SecurityUserCache.remove(String.valueOf(loginUser.getId()));
    }

    /**
     * 修改个人信息
     *
     * @param selfDto 用户个人信息
     */
    @Override
    public void updateUserSelfInfo(UserUpdateSelfDto selfDto) {
        // 参数校验
        Integer gender = selfDto.getGender();
        Assert.notNull(Gender.of(gender), ExpMessageCode.of("valid.usercenter.updeself.genderInvalid"));
        Assert.failIfTrue(checkNicknameExist(selfDto.getUserId(), selfDto.getNickname()), ExpMessageCode.of("valid.usercenter.updeself.nicknameExist"));
        // 数据入库
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        String updateTime = DateUtils.formatNow(DateUtils.YMD_HMS_PATTERN);
        int updateRows = userMapper.updateUserSelf(selfDto, loginUser.getId(), updateTime);
        Assert.affectOneRow(updateRows, ExpMessageCode.of("result.usercenter.updateInfoFailed"));
        SecurityUserCache.remove(String.valueOf(selfDto.getUserId()));
    }

    /**
     * 分页查询用户操作日志列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<LogVo>
     */
    @Override
    public PageInfo<LogVo> queryUserLogListByPage(UserLogQueryDto queryDto) {
        // 设置用户信息
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        queryDto.setUsername(loginUser.getUsername());
        // 参数检查
        queryDto.checkPage();
        // 分页查询
        try (Page<LogVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<LogVo> dataList = userMapper.selectUserLogList(queryDto);
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }

    private void preDoUpdateUserPass(UserUpdatePassDto passDto) {
        // 空校验
        String username = passDto.getUsername();
        Assert.notBlank(username, ExpMessageCode.of("valid.usercenter.username.NotBlank"));
        String oldPass = passDto.getOldPass();
        Assert.notBlank(oldPass, ExpMessageCode.of("valid.usercenter.oldPass.NotBlank"));
        String newPass = passDto.getNewPass();
        Assert.notBlank(newPass, ExpMessageCode.of("valid.usercenter.newPass.NotBlank"));
        String confirmPass = passDto.getConfirmPass();
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
        LocalDateTime changePasswordTime = LocalDateTime.now();
        UserPassLog userPassLog = new UserPassLog();
        userPassLog.setId(SequenceUtils.getId());
        userPassLog.setUserId(userId);
        userPassLog.setPassword(passwordEncoder.encode(password));
        userPassLog.setChangePasswordTime(changePasswordTime);
        Assert.affectOneRow(userPassLogMapper.insert(userPassLog), ExpMessageCode.of("result.usercenter.updatePasswordFailed"));
        // 数据入库
        String newPassword = passwordEncoder.encode(password);
        int passwordLimitedDay = securityExtProperties.getPasswordLimitedDay();
        LocalDateTime passwordExpireTime = changePasswordTime.plusDays(passwordLimitedDay);
        int updateRows = userMapper.updateUserPassword(userId, newPassword, changePasswordTime, passwordExpireTime);
        Assert.affectOneRow(updateRows, ExpMessageCode.of("result.usercenter.updatePasswordFailed"));
        SecurityUserCache.remove(String.valueOf(userId));
    }

    private boolean checkEmailExist(Long userId, String email) {
        LambdaQueryWrapper<User> queryWrapper = queryBuild().eq(User::getEmail, email);
        queryWrapper.ne(User::getId, userId);
        return userMapper.exists(queryWrapper);
    }

    private boolean checkNicknameExist(Long userId, String nickname) {
        LambdaQueryWrapper<User> queryWrapper = queryBuild().eq(User::getNickname, nickname);
        queryWrapper.ne(User::getId, userId);
        return userMapper.exists(queryWrapper);
    }

    private LambdaQueryWrapper<User> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
