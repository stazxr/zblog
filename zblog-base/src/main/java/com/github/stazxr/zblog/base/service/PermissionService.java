package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.entity.Permission;

/**
 * 权限服务层
 *
 * @author SunTao
 * @since 2020-11-16
 */
public interface PermissionService extends IService<Permission> {
    /**
     * 根据权限路径查找权限
     *
     * @param path 权限路径
     * @return Permission
     */
    Permission selectPermByPath(String path);
}
