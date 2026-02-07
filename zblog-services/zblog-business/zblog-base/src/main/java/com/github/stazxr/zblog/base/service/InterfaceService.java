package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.base.domain.dto.query.InterfaceQueryDto;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 接口管理业务层
 *
 * @author SunTao
 * @since 2025-11-02
 */
public interface InterfaceService {
    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return IPage<InterfaceVo>
     */
    IPage<InterfaceVo> queryInterfaceListByPage(InterfaceQueryDto queryDto);

    /**
     * 导出接口列表
     *
     * @param queryDto 查询参数
     * @param response Response
     */
    void exportInterface(InterfaceQueryDto queryDto, HttpServletResponse response);
}
