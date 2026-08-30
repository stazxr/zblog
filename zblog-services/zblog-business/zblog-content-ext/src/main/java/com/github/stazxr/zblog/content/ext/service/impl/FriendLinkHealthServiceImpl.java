package com.github.stazxr.zblog.content.ext.service.impl;

import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.content.ext.domain.entity.FriendLinkCheckLog;
import com.github.stazxr.zblog.content.ext.domain.entity.FriendLinkHealth;
import com.github.stazxr.zblog.content.ext.domain.entity.WebsiteConfig;
import com.github.stazxr.zblog.content.ext.domain.vo.FriendLinkVo;
import com.github.stazxr.zblog.content.ext.mapper.FriendLinkCheckLogMapper;
import com.github.stazxr.zblog.content.ext.mapper.FriendLinkHealthMapper;
import com.github.stazxr.zblog.content.ext.mapper.WebsiteConfigMapper;
import com.github.stazxr.zblog.content.ext.service.FriendLinkHealthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * 友链健康检测业务实现层
 *
 * @author SunTao
 * @since 2026-08-31
 */
@Service
@RequiredArgsConstructor
public class FriendLinkHealthServiceImpl implements FriendLinkHealthService {
    private final Logger log = LoggerFactory.getLogger(FriendLinkHealthServiceImpl.class);

    private final FriendLinkHealthMapper friendLinkHealthMapper;

    private final FriendLinkCheckLogMapper friendLinkCheckLogMapper;

    private final WebsiteConfigMapper websiteConfigMapper;

    /** 连续失败次数达到该值时，标记为异常 */
    private static final int MAX_FAIL_COUNT = 3;

    /** 连接超时时间 */
    private static final int CONNECT_TIMEOUT = 5000;

    /** 读取超时时间 */
    private static final int READ_TIMEOUT = 10000;

    /**
     * 检测单个友链
     *
     * @param friendLink 友链
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkFriendLink(FriendLinkVo friendLink) {
        // 友链信息
        long friendLinkId = friendLink.getId();
        String url = friendLink.getUrl();

        LocalDateTime now = LocalDateTime.now();

        boolean success = false;
        Integer httpStatus = null;
        long responseTime;
        String errorMsg = null;

        long startTime = System.currentTimeMillis();
        HttpURLConnection connection = null;

        try {
            URL targetUrl = new URL(url);
            connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("GET"); // 使用 GET 请求
            connection.setInstanceFollowRedirects(true); // 自动跟随重定向
            connection.setConnectTimeout(CONNECT_TIMEOUT); // 连接超时
            connection.setReadTimeout(READ_TIMEOUT); // 读取超时
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 FriendLinkHealthChecker"); // 设置 User-Agent

            connection.connect();
            httpStatus = connection.getResponseCode();
            responseTime = System.currentTimeMillis() - startTime;
            success = httpStatus >= 200 && httpStatus < 400; // 2xx、3xx 都认为网站正常
            if (!success) {
                errorMsg = "HTTP状态码异常：" + httpStatus;
            }
        } catch (Exception e) {
            responseTime = System.currentTimeMillis() - startTime;
            errorMsg = getErrorMessage(e);
            log.warn("友链检测异常，name: {}，url: {}，error: {}", friendLink.getName(), url, errorMsg);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        // 保存检测日志
        saveCheckLog(friendLinkId, success, httpStatus, responseTime, errorMsg, now);

        // 更新健康状态
        updateHealth(friendLinkId, success, httpStatus, responseTime, errorMsg, now);

        log.info("友链检测完成，friendLinkId: {}，success: {}，httpStatus: {}，responseTime: {} ms", friendLinkId, success, httpStatus, responseTime);
    }

    /**
     * 保存检测日志
     */
    private void saveCheckLog(long linkId, boolean success, Integer httpStatus, Long responseTime, String errorMsg, LocalDateTime checkTime) {
        FriendLinkCheckLog checkLog = new FriendLinkCheckLog();
        checkLog.setId(SequenceUtils.getId());
        checkLog.setLinkId(linkId);
        checkLog.setSuccess(success);
        checkLog.setHttpStatus(httpStatus);
        checkLog.setResponseTime(responseTime);
        checkLog.setErrorMsg(errorMsg);
        checkLog.setCreateTime(checkTime);
        friendLinkCheckLogMapper.insert(checkLog);
    }

    /**
     * 更新友链健康状态
     */
    private void updateHealth(long linkId, boolean success, Integer httpStatus, Long responseTime, String errorMsg, LocalDateTime checkTime) {
        FriendLinkHealth health = friendLinkHealthMapper.selectById(linkId);

        // 第一次检测
        if (health == null) {
            health = new FriendLinkHealth();
            health.setLinkId(linkId);
            health.setStatus(success);
            health.setFailCount(success ? 0 : 1);
            health.setLastCheckTime(checkTime);
            if (success) {
                health.setLastSuccessTime(checkTime);
            } else {
                health.setLastFailTime(checkTime);
            }
            health.setResponseTime(responseTime);
            health.setHttpStatus(httpStatus);
            health.setErrorMsg(errorMsg);
            friendLinkHealthMapper.insert(health);
            return;
        }

        // 更新最后检测时间
        health.setLastCheckTime(checkTime);
        health.setResponseTime(responseTime);
        health.setHttpStatus(httpStatus);
        if (success) {
            // 检测成功
            health.setStatus(true);
            health.setLastSuccessTime(checkTime);
            health.setFailCount(0);
        } else {
            WebsiteConfig websiteConfig = websiteConfigMapper.selectById(1L);
            Integer failedCount = websiteConfig.getFriendLinkCheckFailedCount();

            // 连续失败次数
            int failCount = (health.getFailCount() == null ? 0 : health.getFailCount()) + 1;
            health.setFailCount(failCount);
            health.setLastFailTime(checkTime);
            health.setErrorMsg(errorMsg);
            // 连续失败达到阈值，标记异常
            if (failCount >= (failedCount == null ? MAX_FAIL_COUNT : failedCount)) {
                health.setStatus(false);
            }
        }

        friendLinkHealthMapper.updateById(health);
    }

    /**
     * 获取异常信息
     */
    private String getErrorMessage(Exception e) {
        String message = e.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = e.getClass().getSimpleName();
        }

        if (message.length() > 1000) {
            message = message.substring(0, 1000);
        }

        return message;
    }
}
