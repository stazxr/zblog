package com.github.stazxr.zblog.base.service;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.query.InterfaceQueryDto;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;

/**
 * 字典服务层
 *
 * @author SunTao
 * @since 2025-11-02
 */
public interface InterfaceService {
    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<DictVo>
     */
    PageInfo<InterfaceVo> queryInterfaceListByPage(InterfaceQueryDto queryDto);
}
