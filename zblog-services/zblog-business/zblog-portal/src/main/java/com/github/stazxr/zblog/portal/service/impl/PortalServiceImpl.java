package com.github.stazxr.zblog.portal.service.impl;

import cn.hutool.http.useragent.*;
import com.github.stazxr.zblog.audit.api.AuditService;
import com.github.stazxr.zblog.audit.enums.AuditScene;
import com.github.stazxr.zblog.audit.model.AuditContext;
import com.github.stazxr.zblog.audit.model.AuditResult;
import com.github.stazxr.zblog.bas.context.Context;
import com.github.stazxr.zblog.bas.encryption.util.Md5Utils;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.content.ext.domain.entity.BarrageMessage;
import com.github.stazxr.zblog.content.ext.domain.entity.BarrageMessageLike;
import com.github.stazxr.zblog.content.ext.domain.entity.Visitor;
import com.github.stazxr.zblog.content.ext.domain.entity.VisitorProfile;
import com.github.stazxr.zblog.content.ext.domain.enums.BarrageMessageAuditStatus;
import com.github.stazxr.zblog.content.ext.domain.enums.ThemeType;
import com.github.stazxr.zblog.content.ext.domain.vo.BarrageMessageVo;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemePageVo;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemeVo;
import com.github.stazxr.zblog.content.ext.mapper.*;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.portal.domain.bo.UserBaseInfo;
import com.github.stazxr.zblog.portal.domain.bo.WebLoginUser;
import com.github.stazxr.zblog.portal.domain.dto.BarrageMessageDto;
import com.github.stazxr.zblog.portal.publisher.BarrageMessagePublisher;
import com.github.stazxr.zblog.portal.service.PortalService;
import com.github.stazxr.zblog.portal.util.VisitorUtil;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpRegion;
import com.github.stazxr.zblog.util.net.IpRegionUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 门户管理业务实现层
 *
 * @author SunTao
 * @since 2027-07-07
 */
@Service
@RequiredArgsConstructor
public class PortalServiceImpl implements PortalService {
    private static final Logger log = LoggerFactory.getLogger(PortalServiceImpl.class);

    private final AuditService auditService;

    private final BarrageMessageMapper barrageMessageMapper;

    private final BarrageMessageLikeMapper barrageMessageLikeMapper;

    private final BarrageMessagePublisher barrageMessagePublisher;

    private final ThemeMapper themeMapper;

    private final ThemePageMapper themePageMapper;

    private final VisitorMapper visitorMapper;

    private final VisitorProfileMapper visitorProfileMapper;

    /**
     * 获取Web端登录用户信息
     *
     * @return WebLoginUser
     */
    @Override
    public WebLoginUser currentWebUserDetail() {
        WebLoginUser webLoginUser = new WebLoginUser();

        boolean authenticated = SecurityUtils.isAuthenticated();
        webLoginUser.setAuthenticated(authenticated);
        if (!authenticated) {
            // 获取访客信息
            String visitorId = Context.get("x-visitor-id");
            if (StringUtils.isNotBlank(visitorId)) {
                VisitorProfile profile = visitorProfileMapper.selectById(visitorId);
                webLoginUser.setUser(new UserBaseInfo(profile));
            }
            return webLoginUser;
        }

        // 查询登录用户信息
        User loginUser = SecurityUtils.getLoginUser();
        webLoginUser.setUser(new UserBaseInfo(loginUser));

        return webLoginUser;
    }

    /**
     * 查询博客页面信息
     *
     * @return Map<String, List<ThemePageVo>>
     *     K: pageLabel
     *     V: List<ThemePageVo>
     */
    @Override
    public Map<String, List<ThemePageVo>> queryPageInfo() {
        // 获取客户端类型
        String clientType = Context.get("x-client-type");
        ThemeType themeType = "01".equals(clientType) ? ThemeType.MOBILE : ThemeType.PC;

        ThemeVo themeVo = null; // 当前主题
        if (SecurityUtils.isAuthenticated()) { // 用户已登录，查询用户当前激活主题
            Long loginId = SecurityUtils.getLoginId();
            themeVo = themeMapper.selectUserCurrentTheme(loginId, themeType);
        }

        if (themeVo == null) { // 查询系统默认主题
            themeVo = themeMapper.selectDefaultTheme(themeType);
        }

        // 查询主题页面
        List<ThemePageVo> themePages = new ArrayList<>();
        if (themeVo != null) {
            themePages = themePageMapper.selectThemePage(themeVo.getId());
        }

        Map<String, List<ThemePageVo>> themeMap = new LinkedHashMap<>();
        for (ThemePageVo themePage : themePages) {
            List<ThemePageVo> pages = themeMap.getOrDefault(themePage.getPageLabel(), new ArrayList<>());
            pages.add(themePage);
            themeMap.putIfAbsent(themePage.getPageLabel(), pages);
        }
        return themeMap;
    }

    /**
     * 查询最新弹幕列表
     *
     * @return List<BarrageMessageVo>
     */
    @Override
    public List<BarrageMessageVo> queryBarrageMessageList() {
        int size = 100; // TODO 网站配置功能待开发
        return barrageMessageMapper.selectLastedBarrageMessageList(size);
    }

    /**
     * 新增弹幕
     *
     * @param request           请求信息
     * @param barrageMessageDto 弹幕信息
     */
    @Override
    public void addBarrageMessage(HttpServletRequest request, BarrageMessageDto barrageMessageDto) {
        // 生成弹幕id
        Long messageId = SequenceUtils.getId();

        // 1.审核
        String content = barrageMessageDto.getContent();
        AuditResult auditResult = auditBarrageMessage(messageId, content);

        // 2.入库
        BarrageMessage message = buildBarrageMessage(request, content, messageId, auditResult);
        ThrowUtils.when(barrageMessageMapper.insert(message) != 1).system(BaseErrorCode.SCOREA001);

        // 3.广播
        if (BarrageMessageAuditStatus.APPROVED.getStatus().equals(message.getAuditStatus())) {
            barrageMessagePublisher.send(barrageMessageMapper.selectBarrageMessageDetail(messageId));
        }
    }

    /**
     * 点赞弹幕
     *
     * @param request 请求信息
     * @param barrageMessageId 弹幕id
     */
    @Override
    public void likeBarrageMessage(HttpServletRequest request, Long barrageMessageId) {
        boolean authenticated = SecurityUtils.isAuthenticated();
        BarrageMessageLike newLike = null;
        if (authenticated) {
            Long userId = SecurityUtils.getLoginId();
            BarrageMessageLike like = barrageMessageLikeMapper.selectOneByUserId(barrageMessageId, userId);
            if (like == null) {
                newLike = new BarrageMessageLike();
            }
        } else {
            String visitorId = Context.get("x-visitor-id");
            if (StringUtils.isBlank(visitorId)) {
                return;
            }
            BarrageMessageLike like = barrageMessageLikeMapper.selectOneByVisitorId(barrageMessageId, visitorId);
            if (like == null) {
                newLike = new BarrageMessageLike();

            }
        }

        if (newLike != null) {
            newLike.setId(SequenceUtils.getId());
            newLike.setBarrageMessageId(barrageMessageId);
            newLike.setIp(IpUtils.getIp(request));
            newLike.setUserAgent(IpUtils.getUserAgent(request));
            newLike.setCreateTime(LocalDateTime.now());
            barrageMessageLikeMapper.insert(newLike);
        }
    }

    /**
     * 记录访客信息
     *
     * @param request    请求信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordVisitor(HttpServletRequest request) {
        String visitorId = Context.get("x-visitor-id");
        if (StringUtils.isBlank(visitorId)) {
            return;
        }

        // 获取请求信息
        String ip = IpUtils.getIp(request);
        String ua = IpUtils.getUserAgent(request);
        LocalDateTime now = LocalDateTime.now();
        Long userId = SecurityUtils.isAuthenticated() ? SecurityUtils.getLoginId() : null;

        // 查询访客信息
        Visitor visitor = visitorMapper.selectById(visitorId);
        if (visitor == null) {
            visitor = new Visitor();
            visitor.setVisitorId(visitorId);
            visitor.setUserId(userId);
            fillVisitorIpInfo(visitor, ip);
            fillVisitorUserAgent(visitor, ua);
            visitor.setFirstVisitTime(now);
            visitor.setLastVisitDate(LocalDate.now());
            visitor.setCreateTime(now);
            visitorMapper.insert(visitor);

            VisitorProfile profile = new VisitorProfile();
            profile.setVisitorId(visitorId);
            profile.setNickname(generateVisitorNickname(visitorId));
            profile.setAvatar(generateVisitorAvatar(visitorId));
            visitorProfileMapper.insert(profile);
        } else {
            boolean change = false;
            if (StringUtils.isNotBlank(ip) && !ip.equals(visitor.getIp())) {
                change = true;
                fillVisitorIpInfo(visitor, ip);
            }
            if (StringUtils.isNotBlank(ua) && !ua.equals(visitor.getUserAgent())) {
                change = true;
                fillVisitorUserAgent(visitor, ua);
            }
            if (visitor.getUserId() == null && userId != null) {
                change = true;
                visitor.setUserId(userId);
            }
            if (!LocalDate.now().equals(visitor.getLastVisitDate())) {
                change = true;
                visitor.setLastVisitDate(LocalDate.now());
                visitor.setUpdateTime(now);
            }

            if (change) {
                visitorMapper.updateById(visitor);
            }
        }
    }

    private AuditResult auditBarrageMessage(Long messageId, String content) {
        AuditContext auditContext = new AuditContext(content, AuditScene.BARRAGE);
        auditContext.setOid(String.valueOf(messageId));
        if (SecurityUtils.isAuthenticated()) {
            auditContext.setUid(String.valueOf(SecurityUtils.getLoginId()));
        }
        return auditService.audit(auditContext);
    }

    private BarrageMessage buildBarrageMessage(HttpServletRequest request,
            String content, Long messageId, AuditResult auditResult) {
        BarrageMessage message = new BarrageMessage();
        message.setId(messageId);
        message.setContent(content);
        if (SecurityUtils.isAuthenticated()) {
            User user = SecurityUtils.getLoginUser();
            message.setUserId(user.getId());
            message.setNickname(user.getNickname());
            message.setAvatar(user.getHeadImgUrl());
        } else {
            String visitorId = Context.get("x-visitor-id");
            VisitorProfile profile = null;
            if (StringUtils.isNotBlank(visitorId)) {
                message.setVisitorId(visitorId);
                profile = visitorProfileMapper.selectById(visitorId);
            }

            if (profile != null) {
                message.setNickname(profile.getNickname());
                message.setAvatar(profile.getAvatar());
            } else {
                message.setNickname("游客" + RandomStringUtils.randomAlphanumeric(4).toUpperCase());
            }
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
        message.setIpRegion(IpRegionUtils.getRegion(ip));
        message.setUserAgent(userAgent);
        message.setDeviceId(buildDeviceId(ip, userAgent));
        message.setColor("#FFFFFF"); // TODO 弹幕颜色待配置化
        message.setCreateTime(now);
        return message;
    }

    private String buildDeviceId(String ip, String ua) {
        try {
            return Md5Utils.md5(ip + ua);
        } catch (Exception e) {
            log.error("build deviceId exception", e);
            return null;
        }
    }

    private void fillVisitorIpInfo(Visitor visitor, String ip) {
        if (StringUtils.isBlank(ip) || visitor == null) {
            return;
        }

        visitor.setIp(ip);
        IpRegion search = IpRegionUtils.search(ip);
        visitor.setCountry(search.getCountry());
        visitor.setProvince(search.getProvince());
        visitor.setCity(search.getCity());
        visitor.setDistrict(null);
        visitor.setIsp(search.getIsp());
    }

    private void fillVisitorUserAgent(Visitor visitor, String ua) {
        if (StringUtils.isBlank(ua) || visitor == null) {
            return;
        }

        try {
            visitor.setUserAgent(ua);
            UserAgent userAgent = UserAgentUtil.parse(ua);
            if (userAgent == null) {
                return;
            }

            Browser browser = userAgent.getBrowser();
            if (browser != null) {
                visitor.setBrowser(browser.getName());
                visitor.setBrowserVersion(browser.getVersion(ua));
            }

            OS os = userAgent.getOs();
            if (os != null) {
                visitor.setOs(os.getName());
            }

            Platform platform = userAgent.getPlatform();
            if (platform != null) {
                visitor.setDeviceType(detectDeviceType(platform));
            }
        } catch (Exception e) {
            log.error("parse user agent error: {}", ua, e);
        }
    }

    private String detectDeviceType(Platform platform) {
        return platform.getName();
    }

    private String generateVisitorNickname(String visitorId) {
        return VisitorUtil.getNickname(visitorId);
    }

    private String generateVisitorAvatar(String visitorId) {
        return VisitorUtil.getAvatar(visitorId);
    }
}
