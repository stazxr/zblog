package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.dto.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.PermCodeVo;
import com.github.stazxr.zblog.base.domain.vo.PermissionVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限数据持久层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 查询所有的菜单
     *
     * @return 所有的菜单列表
     */
    List<Permission> selectMenu();

    /**
     * 查询用户对应的菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Permission> selectMenuByUserId(@Param("userId") Long userId);

    /**
     * 查询权限列表
     *
     * @param queryDto 查询参数
     * @return permsList
     */
    List<PermissionVo> queryPermList(PermissionQueryDto queryDto);

    /**
     * 查找所有注册的权限编码
     *
     * @return permCodes
     */
    List<PermCodeVo> queryPermCodes();

    /**
     * 根据权限名称查询权限信息
     *
     * @param permName 权限名称
     * @return Permission
     */
    Permission findByPermName(@Param("permName") String permName);

    /**
     * 根据组件名称查询权限信息
     *
     * @param componentName 组件名称
     * @return Permission
     */
    Permission findByComponentName(@Param("componentName") String componentName);

    /**
     * 查询权限详情
     *
     * @param permId 权限ID
     * @return PermissionVo
     */
    PermissionVo queryPermDetail(@Param("permId") Long permId);
}
