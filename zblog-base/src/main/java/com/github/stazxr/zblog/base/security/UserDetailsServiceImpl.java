package com.github.stazxr.zblog.base.security;

import com.github.stazxr.zblog.base.cache.UserRoleCache;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.base.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 继承UserDetailsService，重写登录认证方法
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库获取用户信息
        User user = userService.queryUserByUsername(username.toUpperCase());

        // 判断用户是否存在或允许登录
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 缓存用户对应的角色信息
        List<Role> roles = roleService.queryRolesByUserId(user.getId());
        UserRoleCache.put(user.getUsername(), roles);

        // 返回UserDetails对象进行认证
        return user;
    }
}
