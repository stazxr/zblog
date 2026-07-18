package com.github.stazxr.zblog.content.ext.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.audit.mapper.AuditRecordMapper;
import com.github.stazxr.zblog.audit.model.AuditRecord;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.content.ext.domain.dto.BarrageMessageAuditDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.BarrageMessageQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.BarrageMessage;
import com.github.stazxr.zblog.content.ext.domain.enums.BarrageMessageAuditStatus;
import com.github.stazxr.zblog.content.ext.domain.error.BarrageMessageErrorCode;
import com.github.stazxr.zblog.content.ext.domain.vo.BarrageMessageVo;
import com.github.stazxr.zblog.content.ext.mapper.BarrageMessageMapper;
import com.github.stazxr.zblog.content.ext.service.BarrageMessageService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 弹幕管理业务实现层
 *
 * @author SunTao
 * @since 2027-07-07
 */
@Service
@RequiredArgsConstructor
public class BarrageMessageServiceImpl implements BarrageMessageService {
    private final BarrageMessageMapper barrageMessageMapper;

    private final AuditRecordMapper auditRecordMapper;

    /**
     * 分页查询弹幕列表
     *
     * @param queryDto 查询参数
     * @return IPage<BarrageMessageVo>
     */
    @Override
    public IPage<BarrageMessageVo> queryBarrageMessageListByPage(BarrageMessageQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getContent())) {
            queryDto.setContent(queryDto.getContent().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getNickname())) {
            queryDto.setNickname(queryDto.getNickname().trim());
        }

        // 分页查询
        Page<BarrageMessageVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return barrageMessageMapper.selectBarrageMessageList(page, queryDto);
    }

    /**
     * 查询弹幕详情
     *
     * @param barrageMessageId 弹幕id
     * @return BarrageMessageVo
     */
    @Override
    public BarrageMessageVo queryBarrageMessageDetail(Long barrageMessageId) {
        BarrageMessageVo barrageMessageVo = barrageMessageMapper.selectBarrageMessageDetail(barrageMessageId);
        if (barrageMessageVo != null) {
            AuditRecord auditRecord = auditRecordMapper.selectByOid(barrageMessageId);
            barrageMessageVo.setAuditRecord(auditRecord);
        }
        return ThrowUtils.requireNonNull(barrageMessageVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 审核弹幕
     *
     * @param auditDto 弹幕审核信息
     */
    @Override
    public void auditBarrageMessage(BarrageMessageAuditDto auditDto) {
        // 判断弹幕是否存在
        BarrageMessage barrageMessage = barrageMessageMapper.selectById(auditDto.getBarrageMessageId());
        ThrowUtils.throwIfNull(barrageMessage, BaseErrorCode.ECOREA001);
        // 判断弹幕状态是否为待审批
        boolean isPending = BarrageMessageAuditStatus.PENDING.getStatus().equals(barrageMessage.getAuditStatus());
        boolean isManual = BarrageMessageAuditStatus.MANUAL.getStatus().equals(barrageMessage.getAuditStatus());
        ThrowUtils.throwIf(!isPending && !isManual, BarrageMessageErrorCode.EBMESA001);
        if (BarrageMessageAuditStatus.REJECTED.getStatus().equals(barrageMessage.getAuditStatus())) {
            if (StringUtils.isBlank(auditDto.getAuditReason())) {
                auditDto.setAuditReason("审批拒绝");
            }
        } else {
            if (StringUtils.isBlank(auditDto.getAuditReason())) {
                auditDto.setAuditReason("审批通过");
            }
        }

        // 更新审核状态
        barrageMessage.setAuditStatus(auditDto.getAuditStatus());
        barrageMessage.setAuditReason(auditDto.getAuditReason());
        barrageMessage.setAuditUserId(SecurityUtils.getLoginId());
        barrageMessage.setAuditTime(LocalDateTime.now());
        ThrowUtils.when(barrageMessageMapper.updateById(barrageMessage) != 1).system(BaseErrorCode.SCOREA002);
    }

    /**
     * 删除弹幕
     *
     * @param barrageMessageId 弹幕id
     */
    @Override
    public void deleteBarrageMessage(Long barrageMessageId) {
        barrageMessageMapper.deleteById(barrageMessageId);
    }
}
