package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.dto.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色数据持久层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询用户角色列表（包含被禁用的角色）
     *
     * @param userId 用户序列
     * @return Roles
     */
    List<Role> queryRolesByUserId(@Param("userId") Long userId);

    /**
     * 查询资源角色列表（包含被禁用的角色）
     *
     * @param permissionId 权限序列
     * @return Roles
     */
    List<Role> queryRolesByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 查询角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    List<RoleVo> selectRoleList(RoleQueryDto queryDto);
}
