package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.content.ext.domain.dto.query.VisitorLogQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.VisitorLogVo;

/**
 * 访客日志管理业务层
 *
 * @author SunTao
 * @since 2026-07-21
 */
public interface VisitorLogService {
    /**
     * 分页查询访客日志列表
     *
     * @param queryDto 查询参数
     * @return IPage<VisitorLogVo>
     */
    IPage<VisitorLogVo> queryVisitorLogListByPage(VisitorLogQueryDto queryDto);

    /**
     * 查询访客日志详情
     *
     * @param visitorLogId 访客日志id
     * @return VisitorLogVo
     */
    VisitorLogVo queryVisitorLogDetail(Long visitorLogId);
}
