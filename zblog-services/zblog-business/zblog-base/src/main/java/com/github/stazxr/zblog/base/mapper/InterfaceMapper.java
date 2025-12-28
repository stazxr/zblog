package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.dto.query.InterfaceQueryDto;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;

import java.util.List;

/**
 * 接口管理数据层
 *
 * @author SunTao
 * @since 2025-11-03
 */
public interface InterfaceMapper {
    /**
     * 查询字典列表
     *
     * @param queryDto 查询参数
     * @return List<InterfaceVo>
     */
    List<InterfaceVo> selectInterfaceList(InterfaceQueryDto queryDto);
}
