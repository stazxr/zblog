package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.dto.query.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
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
     * 查询角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    List<RoleVo> selectRoleList(RoleQueryDto queryDto);

    /**
     * 查询角色详情
     *
     * @param roleId 角色序列
     * @return RoleVo
     */
    RoleVo selectRoleDetail(@Param("roleId") Long roleId);

    /**
     * 根据角色名称查询角色信息
     *
     * @param roleName 角色名称
     * @return Role
     */
    Role selectByRoleName(@Param("roleName") String roleName);

    /**
     * 根据角色编码查询角色信息
     *
     * @param roleCode 角色编码
     * @return Role
     */
    Role selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询可以访问权限的角色列表
     *
     * @param permId 权限序列
     * @param blurry 模糊查询：角色名称，角色编码
     * @return roleList
     */
    List<RoleVo> selectPermRoles(@Param("permId") Long permId, @Param("blurry") String blurry);

    /**
     * 查询角色对应的用户列表
     *
     * @param queryDto 查询参数
     * @return userList
     */
    List<UserVo> selectUsersByRoleId(UserQueryDto queryDto);

    /**
     * 查询用户角色列表（包含被禁用的角色）
     *
     * @param userId 用户序列
     * @return Roles
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 查询允许访问资源的角色列表
     *
     * @param uri 请求地址
     * @param method 请求方式
     * @return Roles
     */
    List<Role> selectRolesByUriAndMethod(@Param("uri") String uri, @Param("method") String method);
}
