package com.github.stazxr.zblog.bas.security.service.impl;

import com.github.stazxr.zblog.bas.security.core.SecurityRole;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.core.UserStatus;
import com.github.stazxr.zblog.bas.security.core.UserType;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于管理系统中的安全用户服务接口的默认实现。
 *
 * @see org.springframework.boot.autoconfigure.security.SecurityProperties#getUser
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see com.github.stazxr.zblog.bas.security.core.SecurityUser
 *
 * @author SunTao
 * @since 2024-11-10
 */
@Slf4j
public class SecurityUserServiceImpl implements SecurityUserService, InitializingBean {
    private static final String DEFAULT_USER_ID = "1";

    private final SecurityProperties securityProperties;

    private final PasswordEncoder passwordEncoder;

    /**
     * 构造方法注入 {@link SecurityProperties}。
     *
     * @param securityProperties 配置类，包含用户默认信息
     * @param passwordEncoder 加密工具类
     */
    public SecurityUserServiceImpl(SecurityProperties securityProperties, PasswordEncoder passwordEncoder) {
        this.securityProperties = securityProperties;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 根据 {@code userId} 查询用户信息。
     *
     * @param userId 用户ID
     * @return 用户的 {@link SecurityUser} 实例；如果未找到用户，返回 {@code null}
     */
    @Override
    public SecurityUser findUserById(String userId) {
        if (DEFAULT_USER_ID.equals(userId)) {
            return createSecurityUser();
        }
        return null;
    }

    /**
     * 根据用户名 {@code username} 登录并返回登录用户信息。
     *
     * @param username 用户名
     * @return 用户的 {@link SecurityUser} 实例
     */
    @Override
    public SecurityUser loginWithUsername(String username) {
        SecurityProperties.User user = securityProperties.getUser();
        if (user.getName().equals(username)) {
            return createSecurityUser();
        }
        return null;
    }

    /**
     * 更新用户的登录信息。当前实现为空，可根据实际需求扩展。
     *
     * @param username 用户名
     * @param userIp   用户的登录ip
     * @param type     用户登录类型
     * @param request  用户的请求信息
     */
    @Override
    public void updateUserLoginInfo(String username, String userIp, int type, HttpServletRequest request) {
    }

    /**
     * 创建并返回一个默认的 {@link SecurityUser} 实例。
     * 包含用户名、密码、用户类型、状态及角色权限。
     *
     * @return 一个 {@link SecurityUser} 实例
     */
    private SecurityUser createSecurityUser() {
        SecurityProperties.User user = securityProperties.getUser();
        SecurityUser securityUser = new SecurityUser();
        securityUser.setId(Long.parseLong(DEFAULT_USER_ID));
        securityUser.setUsername(user.getName());
        securityUser.setPassword(passwordEncoder.encode(user.getPassword()));
        securityUser.setUserType(UserType.ADMIN_USER.getType());
        securityUser.setUserStatus(UserStatus.NORMAL.getStatus());

        // 设置角色权限
        List<SecurityRole> authorities = new ArrayList<>();
        for (String role : user.getRoles()) {
            SecurityRole securityRole = new SecurityRole();
            securityRole.setRoleCode(role);
            authorities.add(securityRole);
        }
        securityUser.setAuthorities(authorities);

        return securityUser;
    }

    @Override
    public void afterPropertiesSet() {
        SecurityProperties.User user = securityProperties.getUser();
        log.info("默认用户信息为：" + user.getName() + " / " + user.getPassword());
    }
}

