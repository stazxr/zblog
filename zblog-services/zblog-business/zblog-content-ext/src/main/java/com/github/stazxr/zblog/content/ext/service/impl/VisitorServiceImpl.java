package com.github.stazxr.zblog.content.ext.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.content.ext.domain.dto.query.VisitorQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.VisitorVo;
import com.github.stazxr.zblog.content.ext.mapper.VisitorMapper;
import com.github.stazxr.zblog.content.ext.service.VisitorService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 访客管理业务实现层
 *
 * @author SunTao
 * @since 2026-07-21
 */
@Service
@RequiredArgsConstructor
public class VisitorServiceImpl implements VisitorService {
    private final VisitorMapper visitorMapper;

    /**
     * 分页查询访客列表
     *
     * @param queryDto 查询参数
     * @return IPage<VisitorVo>
     */
    @Override
    public IPage<VisitorVo> queryVisitorListByPage(VisitorQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getUsername())) {
            queryDto.setUsername(queryDto.getUsername().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getIp())) {
            queryDto.setIp(queryDto.getIp().trim());
        }

        // 分页查询
        Page<VisitorVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return visitorMapper.selectVisitorList(page, queryDto);
    }

    /**
     * 查询访客详情
     *
     * @param visitorId 访客id
     * @return VisitorVo
     */
    @Override
    public VisitorVo queryVisitorDetail(String visitorId) {
        VisitorVo visitorVo = visitorMapper.selectVisitorDetail(visitorId);
        return ThrowUtils.requireNonNull(visitorVo, BaseErrorCode.ECOREA001);
    }
}
