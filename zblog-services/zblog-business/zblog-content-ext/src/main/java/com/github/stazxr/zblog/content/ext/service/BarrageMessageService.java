package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.content.ext.domain.dto.BarrageMessageAuditDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.BarrageMessageQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.BarrageMessageVo;

/**
 * 弹幕管理业务层
 *
 * @author SunTao
 * @since 2027-07-06
 */
public interface BarrageMessageService {
    /**
     * 分页查询弹幕列表
     *
     * @param queryDto 查询参数
     * @return IPage<BarrageMessageVo>
     */
    IPage<BarrageMessageVo> queryBarrageMessageListByPage(BarrageMessageQueryDto queryDto);

    /**
     * 查询弹幕详情
     *
     * @param barrageMessageId 弹幕id
     * @return BarrageMessageVo
     */
    BarrageMessageVo queryBarrageMessageDetail(Long barrageMessageId);

    /**
     * 审核弹幕
     *
     * @param auditDto 弹幕审核信息
     */
    void auditBarrageMessage(BarrageMessageAuditDto auditDto);

    /**
     * 删除弹幕
     *
     * @param barrageMessageId 弹幕id
     */
    void deleteBarrageMessage(Long barrageMessageId);
}
