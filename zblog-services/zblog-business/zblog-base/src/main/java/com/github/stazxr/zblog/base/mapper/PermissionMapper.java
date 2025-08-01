package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.bas.router.Resource;
import com.github.stazxr.zblog.base.domain.dto.query.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;
import com.github.stazxr.zblog.base.domain.vo.PermCodeVo;
import com.github.stazxr.zblog.base.domain.vo.PermissionVo;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 权限存储
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 查询权限列表
     *
     * @param queryDto 查询参数
     * @return PermissionVoList
     */
    List<PermissionVo> selectPermList(PermissionQueryDto queryDto);

    /**
     * 查询权限详情
     *
     * @param permId 权限id
     * @return PermissionVo
     */
    PermissionVo selectPermDetail(@Param("permId") Long permId);

    /**
     * 查询权限接口列表
     *
     * @param permId 权限id
     * @return InterfaceVoList
     */
    List<InterfaceVo> selectPermInterfaces(@Param("permId") Long permId);

    /**
     * 查询权限角色列表
     *
     * @param permId 权限id
     * @param blurry 模糊查询：角色名称，角色编码
     * @return RoleVoList
     */
    List<RoleVo> selectPermRoles(@Param("permId") Long permId, @Param("blurry") String blurry);

    /**
     * 分页查询权限日志列表
     *
     * @param permId 查询参数
     * @param blurry 模糊查询
     * @return LogVoList
     */
    List<LogVo> selectPermLogs(@Param("permId") Long permId, @Param("blurry") String blurry);

    /**
     * 查询权限编码列表
     *
     * @param searchKey 查询条件
     * @return PermCodeVoList
     */
    List<PermCodeVo> selectPermCodes(@Param("searchKey") String searchKey);

    /**
     * 根据权限编码查询资源信息
     *
     * @param permCode 权限编码
     * @return Resource
     */
    Resource selectResourceByPermCode(@Param("permCode") String permCode);



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
