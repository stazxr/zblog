package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.content.ext.domain.dto.query.VisitorQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.VisitorVo;

/**
 * 访客管理业务层
 *
 * @author SunTao
 * @since 2026-07-21
 */
public interface VisitorService {
    /**
     * 分页查询访客列表
     *
     * @param queryDto 查询参数
     * @return IPage<VisitorVo>
     */
    IPage<VisitorVo> queryVisitorListByPage(VisitorQueryDto queryDto);

    /**
     * 查询访客详情
     *
     * @param visitorId 访客id
     * @return VisitorVo
     */
    VisitorVo queryVisitorDetail(String visitorId);
}
