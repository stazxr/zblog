package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.MenuVo;

import java.util.List;

/**
 * 权限服务层
 *
 * @author SunTao
 * @since 2020-11-16
 */
public interface PermissionService extends IService<Permission> {
    /**
     * 查询用户权限列表
     *
     * @param userId 用户序列
     * @return Permissions
     */
    List<Permission> queryPermsByUserId(Long userId);

    /**
     * 构造前端菜单模型
     *
     * @param userId 用户序列
     * @return menuTree
     */
    List<MenuVo> buildMenus(Long userId);
}
