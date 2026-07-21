package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.ext.domain.dto.query.VisitorLogQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.VisitorLog;
import com.github.stazxr.zblog.content.ext.domain.vo.VisitorLogVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 访客日志管理数据层
 *
 * @author SunTao
 * @since 2026-07-20
 */
public interface VisitorLogMapper extends BaseMapper<VisitorLog> {
    /**
     * 分页查询访客日志列表
     *
     * @param queryDto 查询参数
     * @return IPage<VisitorLogVo>
     */
    IPage<VisitorLogVo> selectVisitorLogList(@Param("page") Page<VisitorLogVo> page, @Param("query") VisitorLogQueryDto queryDto);

    /**
     * 查询访客日志详情
     *
     * @param visitorLogId 访客日志id
     * @return VisitorLogVo
     */
    VisitorLogVo selectVisitorLogDetail(@Param("visitorLogId") Long visitorLogId);
}
