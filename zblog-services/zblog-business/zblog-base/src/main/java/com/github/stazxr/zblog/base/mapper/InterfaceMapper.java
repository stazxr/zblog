package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.base.domain.dto.query.InterfaceQueryDto;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;
import org.apache.ibatis.annotations.Param;

/**
 * 接口管理数据层
 *
 * @author SunTao
 * @since 2025-11-03
 */
public interface InterfaceMapper {
    /**
     * 查询接口列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<InterfaceVo>
     */
    IPage<InterfaceVo> selectInterfaceList(@Param("page") Page<InterfaceVo> page, @Param("query") InterfaceQueryDto queryDto);

    /**
     * 查询接口列表V2
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<RoleVo>
     */
    IPage<InterfaceVo> selectInterfaceListV2(@Param("page") Page<InterfaceVo> page, @Param("query") InterfaceQueryDto queryDto);
}
