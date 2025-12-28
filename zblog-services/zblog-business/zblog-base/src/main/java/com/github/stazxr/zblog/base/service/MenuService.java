package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.domain.vo.MenuVo;

import java.util.List;

/**
 * 菜单管理业务层
 *
 * @author SunTao
 * @since 2024-12-01
 */
public interface MenuService {
    /**
     * 查询用户菜单列表（树）
     *
     * @return {@link List<MenuVo>} 返回构建的用户菜单树
     */
    List<MenuVo> queryUserMenuTree();
}

