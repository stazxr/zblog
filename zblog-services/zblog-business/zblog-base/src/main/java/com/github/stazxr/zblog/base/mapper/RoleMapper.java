package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.base.domain.dto.query.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * 角色管理数据层
 *
 * @author SunTao
 * @since 2020-11-15
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询角色列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<RoleVo>
     */
    IPage<RoleVo> selectRoleList(@Param("page") Page<RoleVo> page, @Param("query") RoleQueryDto queryDto);

    /**
     * 查询角色详情
     *
     * @param roleId 角色序列
     * @return RoleVo
     */
    RoleVo selectRoleDetail(@Param("roleId") Long roleId);

    /**
     * 获取指定资源的允许访问角色集合。
     *
     * @param requestUri    请求的 URL 路径，表示要访问的资源地址。
     * @param requestMethod 请求的 HTTP 方法类型（例如 GET、POST 等）
     * @return 角色编码集合。
     */
    Set<String> selectResourceRoles(@Param("requestUri") String requestUri, @Param("requestMethod") String requestMethod);

    /**
     * 判断是否存在，忽略逻辑删除
     *
     * @param queryWrapper 查询条件
     * @return boolean
     */
    @Select("SELECT IF(EXISTS (SELECT 1 FROM role ${ew.customSqlSegment}), 1, 0)")
    boolean existsIgnoreDeleted(@Param(Constants.WRAPPER) LambdaQueryWrapper<Role> queryWrapper);
}
