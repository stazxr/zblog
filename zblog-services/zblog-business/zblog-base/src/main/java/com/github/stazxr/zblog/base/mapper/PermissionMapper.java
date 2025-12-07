package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.bas.router.Resource;
import com.github.stazxr.zblog.base.domain.dto.query.PermissionQueryDto;
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
     * 根据权限id查询角色编码列表
     *
     * @param permId 权限id
     * @return 角色编码列表
     */
    List<String> selectRoleCodesByPermId(@Param("permId") Long permId);

    /**
     * 根据权限id查询用户id列表
     *
     * @param permId 权限id
     * @return 用户id列表
     */
    List<Long> selectUserIdsByPermId(@Param("permId") Long permId);
}
