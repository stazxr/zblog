package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.entity.Role;

import java.util.List;

/**
 * 角色服务层
 *
 * @author SunTao
 * @since 2020-11-16
 */
public interface RoleService extends IService<Role> {
    /**
     * 查询用户角色列表
     *
     * @param userId 用户序列
     * @return Roles
     */
    List<Role> queryRolesByUserId(Long userId);
}
