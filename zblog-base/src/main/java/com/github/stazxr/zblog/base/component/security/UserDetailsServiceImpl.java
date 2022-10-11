package com.github.stazxr.zblog.base.component.security;

import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 自定义 UserDetailsService, 重写登录认证方法
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库获取用户信息
        User user = userService.queryUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 返回UserDetails对象进行认证
        return user;
    }
}
