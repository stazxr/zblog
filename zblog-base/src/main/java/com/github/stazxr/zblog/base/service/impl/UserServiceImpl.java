package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.dto.UserUpdateDto;
import com.github.stazxr.zblog.base.domain.dto.UserUpdatePassDto;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.entity.UserPassLog;
import com.github.stazxr.zblog.base.domain.enums.Gender;
import com.github.stazxr.zblog.base.mapper.UserMapper;
import com.github.stazxr.zblog.base.mapper.UserPassLogMapper;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.RegexUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

/**
 * 用户管理 - 业务实现层
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 新密码不能与前几次的密码相同
     */
    private static final int OLD_PASS_COUNT = 3;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserPassLogMapper userPassLogMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return User
     */
    @Override
    public User queryUserByUsername(String username) {
        Assert.notNull(username, "用户名不能为空");
        return userMapper.selectOne(queryBuild().eq(User::getUsername, username.toUpperCase(Locale.ROOT)));
    }

    /**
     * 修改个人头像
     *
     * @param updateDto 用户信息
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserHeadImg(UserUpdateDto updateDto) {
        String username = updateDto.getUsername();
        Assert.notNull(username, "用户名不能为空");

        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
        wrapper.set(User::getHeadImgUrl, updateDto.getHeadImg());
        wrapper.eq(User::getUsername, username);
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
    public boolean updateUserBaseInfo(UserUpdateDto updateDto) {
        Long userId = updateDto.getId();
        Assert.notNull(userId, "用户编码不能为空");

        Integer gender = updateDto.getGender();
        Assert.notNull(Gender.of(gender), "用户性别参数错误");

        String telephone = updateDto.getTelephone();
        if (StringUtils.isNotBlank(telephone) && !RegexUtils.match(telephone, RegexUtils.Const.PHONE_REGEX)) {
            throw new IllegalArgumentException("手机号格式不正确");
        }

        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
        wrapper.set(User::getGender, gender);
        wrapper.set(User::getNickname, updateDto.getNickname());
        wrapper.set(User::getSignature, updateDto.getSignature());
        wrapper.set(User::getTelephone, telephone);
        wrapper.eq(User::getId, userId);
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
        // 非空校验
        String oldPass = passDto.getOldPass();
        Assert.notNull(oldPass, "修改失败，旧密码不能为空");
        String newPass = passDto.getNewPass();
        Assert.notNull(newPass, "新密码不能为空");
        String ensurePass = passDto.getConfirmPass();
        Assert.isTrue(!newPass.equals(ensurePass), () -> {
            throw new ServiceException(ResultCode.PASSWORD_IS_DIFFERENT);
        });

        // 获取用户信息
        User user = queryUserByUsername(SecurityUtils.getLoginUsername());
        Assert.notNull(user, "修改失败，用户不存在");

        // 密码校验：旧密码是否正确 -> 新旧密码是否一致 ->
        Assert.isTrue(!passwordEncoder.matches(oldPass, user.getPassword()), () -> {
            throw new ServiceException(ResultCode.OLD_PASSWORD_IS_ERROR);
        });
        Assert.isTrue(passwordEncoder.matches(newPass, user.getPassword()), () -> {
            throw new ServiceException(ResultCode.PASSWORD_IS_SAME);
        });

        // 密码历史校验
        Long userId = user.getId();
        List<String> oldPassList = userPassLogMapper.selectUserOldPass(userId, OLD_PASS_COUNT);
        oldPassList.forEach(pass -> {
            if (passwordEncoder.matches(newPass, pass)) {
                throw new ServiceException(ResultCode.PASSWORD_IS_OLD_SAME, "新密码不能与历史近三次使用过的密码相同");
            }
        });

        // 密码复杂度校验
        Assert.isTrue(newPass.contains(user.getUsername()), () -> {
            throw new ServiceException(ResultCode.PASSWORD_IS_VALID, "密码不能包含用户名");
        });
        Assert.isTrue(!RegexUtils.match(newPass, RegexUtils.Const.PWD_REGEX), () -> {
            throw new ServiceException(ResultCode.PASSWORD_IS_SIMPLE);
        });

        // 插入日志
        String updateTime = DateUtils.formatNow();
        UserPassLog passLog = UserPassLog.builder().id(GenerateIdUtils.getId()).userId(userId).password(newPass).updateTime(updateTime).build();
        Assert.isTrue(userPassLogMapper.insertUserPassLog(passLog) != 1, "密码修改失败，日志入库失败");

        // 数据入库
        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(userMapper);
        wrapper.set(User::getPassword, passwordEncoder.encode(newPass));
        wrapper.set(User::getChangePwd, false);
        wrapper.set(User::getChangePwdTime, updateTime);
        wrapper.eq(User::getId, userId);
        Assert.isTrue(!wrapper.update(), "密码修改失败，更新用户密码信息失败");
        return true;
    }

    private LambdaQueryWrapper<User> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
