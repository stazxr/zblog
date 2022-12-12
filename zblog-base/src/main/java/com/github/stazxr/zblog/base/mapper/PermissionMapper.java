package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.dto.query.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.PermCodeVo;
import com.github.stazxr.zblog.base.domain.vo.PermissionVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 权限数据持久层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 查询权限列表
     *
     * @param queryDto 查询参数
     * @return permsList
     */
    List<PermissionVo> selectPermList(PermissionQueryDto queryDto);

    /**
     * 查询权限详情
     *
     * @param permId 权限ID
     * @return PermissionVo
     */
    PermissionVo selectPermDetail(@Param("permId") Long permId);

    /**
     * 查询所有的菜单
     *
     * @return 所有的菜单列表
     */
    List<Permission> selectAllMenu();

    /**
     * 查询用户对应的菜单
     *
     * @param userId 用户序列
     * @return 启用的菜单列表
     */
    List<Permission> selectMenuByUserId(@Param("userId") Long userId);

    /**
     * 查询所有的权限编码
     *
     * @param searchKey 查询条件
     * @return PermCodeVo
     */
    List<PermCodeVo> selectPermCodes(@Param("searchKey") String searchKey);

    /**
     * 根据权限名称查询权限信息
     *
     * @param permName 权限名称
     * @return Permission
     */
    Permission selectByPermName(@Param("permName") String permName);

    /**
     * 根据组件名称查询权限信息
     *
     * @param componentName 组件名称
     * @return Permission
     */
    Permission selectByComponentName(@Param("componentName") String componentName);

    /**
     * 查询用户的权限列表（权限编码）
     *
     * @param userId 用户序列
     * @return permCodes
     */
    Set<String> selectUserPerms(@Param("userId") Long userId);

    /**
     * 更新 Permission 信息
     *
     * @param permission 权限信息
     * @return rows
     */
    int updatePermission(Permission permission);
}
