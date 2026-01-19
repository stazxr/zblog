package com.github.stazxr.zblog.bas.security.authn.userpass;

import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * UserDetailsServiceImpl 是一个实现了 Spring Security UserDetailsService 接口的类，
 * 用于加载用户的详细信息。该类通过 {@link SecurityUserService} 获取用户信息，
 * 并将其封装成 {@link UserDetails} 实例。
 *
 * <p>
 * 此类在用户认证过程中被调用，根据用户名查找用户信息，若用户不存在则抛出 {@link UsernameNotFoundException}。
 * </p>
 *
 * @see UserDetailsService
 * @see SecurityUserService
 * @see UserDetails
 *
 * @author SunTao
 * @since 2024-11-10
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService, InitializingBean {
    private SecurityUserService securityUserService;

    /**
     * 根据用户名加载用户的详细信息。
     *
     * @param username 用户名
     * @return 包含用户详细信息的 UserDetails 实例
     * @throws UsernameNotFoundException 如果用户未找到时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser = securityUserService.loginWithUsername(username);
        if (securityUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return securityUser;
    }

    /**
     * 在初始化时检查是否注入了 {@link SecurityUserService}，如果未注入则抛出异常。
     *
     * @throws IllegalArgumentException 如果 {@link SecurityUserService} 未注入
     */
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(securityUserService, "A SecurityUserService must be set");
    }

    @Autowired
    public void setSecurityUserService(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }
}
