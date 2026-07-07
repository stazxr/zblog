package com.github.stazxr.zblog.portal.service.impl;

import com.github.stazxr.zblog.audit.api.AuditService;
import com.github.stazxr.zblog.audit.enums.AuditScene;
import com.github.stazxr.zblog.audit.model.AuditContext;
import com.github.stazxr.zblog.audit.model.AuditResult;
import com.github.stazxr.zblog.bas.encryption.util.Md5Utils;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.content.ext.domain.entity.BarrageMessage;
import com.github.stazxr.zblog.content.ext.domain.enums.BarrageMessageAuditStatus;
import com.github.stazxr.zblog.content.ext.mapper.BarrageMessageMapper;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.portal.domain.dto.BarrageMessageDto;
import com.github.stazxr.zblog.portal.service.PortalService;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 门户管理业务实现层
 *
 * @author SunTao
 * @since 2027-07-07
 */
@Service
@RequiredArgsConstructor
public class PortalServiceImpl implements PortalService {
    private final BarrageMessageMapper barrageMessageMapper;

    private final AuditService auditService;

    /**
     * 新增弹幕
     *
     * @param request           请求信息
     * @param barrageMessageDto 弹幕信息
     */
    @Override
    public void addBarrageMessage(HttpServletRequest request, BarrageMessageDto barrageMessageDto) {
        Long messageId = SequenceUtils.getId();
        boolean isLogin = SecurityUtils.isAuthenticated();

        // 自动审核
        String content = barrageMessageDto.getContent();
        AuditContext auditContext = new AuditContext(content, AuditScene.BARRAGE);
        auditContext.setOid(String.valueOf(messageId));
        if (isLogin) {
            auditContext.setUid(String.valueOf(SecurityUtils.getLoginId()));
        }
        AuditResult auditResult = auditService.audit(auditContext);

        // 构造弹幕信息
        BarrageMessage message = buildBarrageMessage(request, content, messageId, auditResult, isLogin);

        // 数据入库
        ThrowUtils.when(barrageMessageMapper.insert(message) != 1).system(BaseErrorCode.SCOREA001);
    }

    private BarrageMessage buildBarrageMessage(HttpServletRequest request,
            String content, Long messageId, AuditResult auditResult, boolean isLogin) {
        BarrageMessage message = new BarrageMessage();
        message.setId(messageId);
        message.setContent(content);
        if (isLogin) {
            User user = SecurityUtils.getLoginUser();
            message.setUserId(user.getId());
            message.setNickname(user.getNickname());
            message.setAvatar(user.getHeadImgUrl());
        } else {
            message.setNickname("游客" + RandomStringUtils.randomAlphanumeric(4).toUpperCase());
        }

        LocalDateTime now = LocalDateTime.now();
        switch (auditResult.getDecision()) {
            case PASS:
                message.setAuditStatus(BarrageMessageAuditStatus.APPROVED.getStatus());
                message.setAuditReason("自动审核通过");
                message.setContent(auditResult.getContent());
                message.setAuditTime(now);
                break;
            case REJECT:
                message.setAuditStatus(BarrageMessageAuditStatus.REJECTED.getStatus());
                message.setAuditReason(auditResult.getReason());
                message.setAuditTime(now);
                break;
            case MANUAL:
                message.setAuditStatus(BarrageMessageAuditStatus.MANUAL.getStatus());
                message.setAuditReason(auditResult.getReason());
                message.setAuditTime(now);
                break;
            default:
                message.setAuditStatus(BarrageMessageAuditStatus.PENDING.getStatus());
        }

        String ip = IpUtils.getIp(request);
        String userAgent = IpUtils.getUserAgent(request);
        message.setIp(ip);
        message.setIpRegion(IpUtils.getIpLocation(ip, IpUtils.IP_LOCATION_PRO_CITY_REGION));
        message.setUserAgent(userAgent);
        message.setDeviceId(buildDeviceId(ip, userAgent));
        message.setColor("#FFFFFF");
        message.setSpeed(ThreadLocalRandom.current().nextInt(6, 10));
        message.setCreateTime(now);
        return message;
    }

    private String buildDeviceId(String ip, String ua) {
        try {
            return Md5Utils.md5(ip + ua);
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
