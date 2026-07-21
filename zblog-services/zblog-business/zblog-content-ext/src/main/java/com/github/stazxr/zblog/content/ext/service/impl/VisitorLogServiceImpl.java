package com.github.stazxr.zblog.content.ext.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.content.ext.domain.dto.query.VisitorLogQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.VisitorLogVo;
import com.github.stazxr.zblog.content.ext.domain.vo.VisitorVo;
import com.github.stazxr.zblog.content.ext.mapper.VisitorLogMapper;
import com.github.stazxr.zblog.content.ext.service.VisitorLogService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 访客日志管理业务层
 *
 * @author SunTao
 * @since 2026-07-21
 */
@Service
@RequiredArgsConstructor
public class VisitorLogServiceImpl implements VisitorLogService {
    private final VisitorLogMapper visitorLogMapper;

    /**
     * 分页查询访客日志列表
     *
     * @param queryDto 查询参数
     * @return IPage<VisitorLogVo>
     */
    @Override
    public IPage<VisitorLogVo> queryVisitorLogListByPage(VisitorLogQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getVisitorId())) {
            queryDto.setVisitorId(queryDto.getVisitorId().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getIp())) {
            queryDto.setIp(queryDto.getIp().trim());
        }

        // 分页查询
        Page<VisitorLogVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return visitorLogMapper.selectVisitorLogList(page, queryDto);
    }

    /**
     * 查询访客日志详情
     *
     * @param visitorLogId 访客日志id
     * @return VisitorLogVo
     */
    @Override
    public VisitorLogVo queryVisitorLogDetail(Long visitorLogId) {
        VisitorLogVo visitorLogVo = visitorLogMapper.selectVisitorLogDetail(visitorLogId);
        return ThrowUtils.requireNonNull(visitorLogVo, BaseErrorCode.ECOREA001);
    }
}
