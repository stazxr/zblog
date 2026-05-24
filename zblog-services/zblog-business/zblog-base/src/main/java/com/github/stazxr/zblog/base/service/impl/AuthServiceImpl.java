package com.github.stazxr.zblog.base.service.impl;

import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.security.core.UserType;
import com.github.stazxr.zblog.base.domain.bo.LoginUser;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.MenuVo;
import com.github.stazxr.zblog.base.mapper.UserMapper;
import com.github.stazxr.zblog.base.service.AuthService;
import com.github.stazxr.zblog.base.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证管理业务实现层
 *
 * @author SunTao
 * @since 2022-07-24
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MenuService menuService;
    private final UserMapper userMapper;

    @Override
    public LoginUser currentUserDetail() {
        LoginUser loginUser = new LoginUser();

        // 获取用户信息
        User user = SecurityUtils.getLoginUser();
        loginUser.setUser(user);

        // 查询用户权限列表
        if (UserType.ADMIN_USER.getType().equals(user.getUserType())) {
            loginUser.setPerms(userMapper.selectAllMd5PermCodes());
        } else {
            loginUser.setPerms(userMapper.selectMd5PermCodesByUserId(user.getId()));
        }

        // 查询用户菜单列表
        List<MenuVo> menus = menuService.queryUserMenuTree();
        loginUser.setMenus(menus);
        return loginUser;
    }
}
