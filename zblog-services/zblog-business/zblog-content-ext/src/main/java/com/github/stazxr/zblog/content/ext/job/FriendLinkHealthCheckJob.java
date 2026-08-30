package com.github.stazxr.zblog.content.ext.job;

import com.github.stazxr.zblog.content.ext.domain.vo.FriendLinkVo;
import com.github.stazxr.zblog.content.ext.mapper.FriendLinkMapper;
import com.github.stazxr.zblog.content.ext.service.FriendLinkHealthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 友链健康检测任务
 *
 * @author SunTao
 * @since 2026-08-31
 */
@Component
@RequiredArgsConstructor
public class FriendLinkHealthCheckJob {
    private final Logger log = LoggerFactory.getLogger(FriendLinkHealthCheckJob.class);

    private final FriendLinkHealthService friendLinkHealthService;

    private final FriendLinkMapper friendLinkMapper;

    /**
     * 每天凌晨3点检测友链健康情况
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void checkFriendLinkHealth() {
        log.info("开始执行友链健康检测任务");

        // 开始时间
        long startTime = System.currentTimeMillis();

        try {
            // 查询所有待检测的友链
            List<FriendLinkVo> friendLinks = friendLinkMapper.selectHealthCheckFriendLinkList();
            if (friendLinks == null || friendLinks.isEmpty()) {
                log.info("没有需要检测的友链");
                return;
            }

            log.info("本次需要检测友链数量: {}", friendLinks.size());

            for (FriendLinkVo friendLink : friendLinks) {
                try {
                    friendLinkHealthService.checkFriendLink(friendLink);
                } catch (Exception e) {
                    // 单个友链检测失败不能影响其他友链
                    log.error("检测友链失败，name: {}，url: {}", friendLink.getName(), friendLink.getUrl(), e);
                }
            }
        } catch (Exception e) {
            log.error("友链健康检测任务执行失败", e);
        } finally {
            log.info("友链健康检测结束，耗时: {} ms", System.currentTimeMillis() - startTime);
        }
    }
}