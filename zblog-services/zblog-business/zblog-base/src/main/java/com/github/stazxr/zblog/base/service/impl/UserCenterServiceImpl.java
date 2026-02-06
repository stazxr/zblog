package com.github.stazxr.zblog.base.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.encryption.util.RsaUtils;
import com.github.stazxr.zblog.bas.exception.SystemException;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateEmailDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateHeadImgDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdatePassDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateProfileDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserLogQueryDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserLoginLogQueryDto;
import com.github.stazxr.zblog.base.domain.entity.FileRelation;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.entity.UserPassLog;
import com.github.stazxr.zblog.base.domain.enums.BasUploadBusinessType;
import com.github.stazxr.zblog.base.domain.enums.Gender;
import com.github.stazxr.zblog.base.domain.error.UserCenterErrorCode;
import com.github.stazxr.zblog.base.domain.vo.FileVo;
import com.github.stazxr.zblog.base.domain.vo.UserLoginLogVo;
import com.github.stazxr.zblog.base.mapper.*;
import com.github.stazxr.zblog.base.service.UserCenterService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import com.github.stazxr.zblog.util.RegexUtils;
import com.github.stazxr.zblog.util.StringUtils;
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

    private final UserLoginLogMapper userLoginLogMapper;

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
            throw new SystemException(BaseErrorCode.SCOREC002, e);
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
            throw new SystemException(BaseErrorCode.SCOREC002, e);
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
        ThrowUtils.throwSystemIf(fileVo == null, UserCenterErrorCode.SUSERB005);
        int updateRows = userMapper.updateUserHeadImg(userId, fileVo.getFileAccessUrl());
        ThrowUtils.when(updateRows != 1).system(UserCenterErrorCode.SUSERB002);
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
        ThrowUtils.throwIfBlank(cacheCode, UserCenterErrorCode.EUSERB010);
        ThrowUtils.throwIf(!emailDto.getCode().equals(cacheCode), UserCenterErrorCode.EUSERB011);
        // 邮箱校验：相同校验 -> 格式校验 -> 存在性校验
        User loginUser = SecurityUtils.getLoginUser();
        boolean isSameOldEmail = emailDto.getEmail().equals(loginUser.getEmail());
        ThrowUtils.throwIf(isSameOldEmail, UserCenterErrorCode.EUSERB012);
        boolean emailValid = RegexUtils.match(emailDto.getEmail(), RegexUtils.Regex.EMAIL_REGEX);
        ThrowUtils.throwIf(!emailValid, UserCenterErrorCode.EUSERB013);
        ThrowUtils.throwIf(checkEmailExist(loginUser.getId(), emailDto.getEmail()), UserCenterErrorCode.EUSERB014);
        // 数据入库
        int updateRows = userMapper.updateUserEmail(loginUser.getId(), emailDto.getEmail());
        ThrowUtils.when(updateRows != 1).system(UserCenterErrorCode.SUSERB003);
        SecurityUserCache.remove(String.valueOf(loginUser.getId()));
    }

    /**
     * 修改个人信息
     *
     * @param profileDto 用户个人信息
     */
    @Override
    public void updateUserProfileInfo(UserUpdateProfileDto profileDto) {
        // 参数校验
        Integer gender = profileDto.getGender();
        ThrowUtils.throwIfNull(Gender.of(gender), UserCenterErrorCode.EUSERB016);
        boolean nicknameExist = checkNicknameExist(profileDto.getUserId(), profileDto.getNickname());
        ThrowUtils.throwIf(nicknameExist, UserCenterErrorCode.EUSERB015);
        // 数据入库
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        String updateTime = DateUtils.formatNow(DateUtils.YMD_HMS_PATTERN);
        int updateRows = userMapper.updateUserSelf(profileDto, loginUser.getId(), updateTime);
        ThrowUtils.when(updateRows != 1).system(UserCenterErrorCode.SUSERB004);
        SecurityUserCache.remove(String.valueOf(profileDto.getUserId()));
    }

    /**
     * 分页查询用户操作日志列表
     *
     * @param queryDto 查询参数
     * @return IPage<LogVo>
     */
    @Override
    public IPage<LogVo> queryUserLogListByPage(UserLogQueryDto queryDto) {
        // 设置用户信息
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        queryDto.setUsername(loginUser.getUsername());
        // 参数检查
        queryDto.checkPage();
        // 分页查询
        Page<LogVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return userMapper.selectUserLogList(page, queryDto);
    }

    /**
     * 分页查询用户登录日志列表
     *
     * @param queryDto 查询参数
     * @return IPage<UserLoginLogVo>
     */
    @Override
    public IPage<UserLoginLogVo> pageUserLoginLogList(UserLoginLogQueryDto queryDto) {
        // 设置用户信息
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        queryDto.setUsername(loginUser.getUsername());
        // 参数检查
        queryDto.checkPage();
        // 分页查询
        Page<UserLoginLogVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return userLoginLogMapper.selectUserLoginLogList(page, queryDto);
    }

    private void preDoUpdateUserPass(UserUpdatePassDto passDto) {
        // 空校验
        String username = passDto.getUsername();
        ThrowUtils.when(StringUtils.isBlank(username)).system(BaseErrorCode.SCOREB003);
        String oldPass = passDto.getOldPass();
        ThrowUtils.throwIfBlank(oldPass, UserCenterErrorCode.EUSERB000);
        String newPass = passDto.getNewPass();
        ThrowUtils.throwIfBlank(oldPass, UserCenterErrorCode.EUSERB001);
        String confirmPass = passDto.getConfirmPass();
        ThrowUtils.throwIfBlank(oldPass, UserCenterErrorCode.EUSERB002);
        // 校验新密码输入是否一致
        ThrowUtils.throwIf(!newPass.equals(confirmPass), UserCenterErrorCode.EUSERB003);
        // 获取用户信息
        User user = userMapper.selectUserByUsername(username);
        ThrowUtils.throwIfNull(user, UserCenterErrorCode.EUSERB004);
        // 原密码是否正确
        ThrowUtils.throwIf(!passwordEncoder.matches(oldPass, user.getPassword()), UserCenterErrorCode.EUSERB005);
        // 新旧密码不能一致
        ThrowUtils.throwIf(passwordEncoder.matches(newPass, user.getPassword()), UserCenterErrorCode.EUSERB006);
        // 密码复杂度校验
        boolean containUsername = newPass.toLowerCase(Locale.ROOT).contains(username.toLowerCase(Locale.ROOT));
        ThrowUtils.throwIf(containUsername, UserCenterErrorCode.EUSERB007);
        ThrowUtils.throwIf(!RegexUtils.match(newPass, RegexUtils.Regex.NEW_PWD_REGEX), UserCenterErrorCode.EUSERB008);
        // 密码历史校验
        int passwordHistoryCount = securityExtProperties.getPasswordHistoryCount();
        if (passwordHistoryCount > 0) {
            List<String> historyPassList = userPassLogMapper.selectUserHistoryPass(user.getId(), passwordHistoryCount);
            for (String historyPass : historyPassList) {
                boolean historyMatches = passwordEncoder.matches(newPass, historyPass);
                ThrowUtils.throwIf(historyMatches, UserCenterErrorCode.EUSERB009, passwordHistoryCount);
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
        ThrowUtils.when(userPassLogMapper.insert(userPassLog) != 1).system(UserCenterErrorCode.SUSERB001);
        // 数据入库
        String newPassword = passwordEncoder.encode(password);
        int passwordLimitedDay = securityExtProperties.getPasswordLimitedDay();
        LocalDateTime passwordExpireTime = changePasswordTime.plusDays(passwordLimitedDay);
        int updateRows = userMapper.updateUserPassword(userId, newPassword, changePasswordTime, passwordExpireTime);
        ThrowUtils.when(updateRows != 1).system(UserCenterErrorCode.SUSERB001);
        SecurityUserCache.remove(String.valueOf(userId));
    }

    private boolean checkEmailExist(Long userId, String email) {
        LambdaQueryWrapper<User> queryWrapper = queryBuild().eq(User::getEmail, email);
        queryWrapper.ne(User::getId, userId);
        queryWrapper.ne(User::getDeleted, Boolean.FALSE);
        return userMapper.exists(queryWrapper);
    }

    private boolean checkNicknameExist(Long userId, String nickname) {
        // 全局不能重复
        LambdaQueryWrapper<User> queryWrapper = queryBuild().eq(User::getNickname, nickname);
        queryWrapper.ne(User::getId, userId);
        return userMapper.existsIgnoreDeleted(queryWrapper);
    }

    private LambdaQueryWrapper<User> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
