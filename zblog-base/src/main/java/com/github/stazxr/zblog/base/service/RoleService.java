package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import com.github.stazxr.zblog.log.domain.vo.LogVo;

import java.util.List;

/**
 * 角色服务层
 *
 * @author SunTao
 * @since 2020-11-16
 */
public interface RoleService extends IService<Role> {
    /**
     * 查询用户角色列表（包含被禁用的角色）
     *
     * @param userId 用户序列
     * @return Roles
     */
    List<Role> queryRolesByUserId(Long userId);

    /**
     * 查询权限对应的角色详情
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    PageInfo<RoleVo> queryPermRole(RoleQueryDto queryDto);
}
