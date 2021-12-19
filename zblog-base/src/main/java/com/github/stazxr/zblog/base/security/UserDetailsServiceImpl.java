//package com.github.stazxr.zblog.base.security;
//
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
///**
// * 继承 UserDetailsService 重写登录逻辑
// *
// * @author SunTao
// * @since 2020-11-16
// */
//@Component
//@AllArgsConstructor
//public class UserDetailsServiceImpl implements UserDetailsService {
//    private final UserService userService;
//
//    private final RoleService roleService;
//
//    @Override
//    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//        // 从数据库获取用户信息
//        User user;
//        if (usernameOrEmail.contains(SymbolConst.AT_SYMBOL)) {
//            user = userService.selectUserByEmail(usernameOrEmail);
//        } else {
//            user = userService.selectUserByUsername(usernameOrEmail.toUpperCase());
//        }
//
//        // 判断用户是否存在或允许登录
//        if (user == null) {
//            throw new UsernameNotFoundException("用户不存在");
//        }
//        if (!user.getAllowLogin()) {
//            throw new UsernameNotFoundException("当前用户不允许登录");
//        }
//
//        // 获取用户对应的角色列表
//        List<Role> roleList = roleService.selectRolesByUserId(user.getId());
//        user.setRoles(roleList);
//
//        // 返回UserDetails对象进行认证
//        return user;
//    }
//}
