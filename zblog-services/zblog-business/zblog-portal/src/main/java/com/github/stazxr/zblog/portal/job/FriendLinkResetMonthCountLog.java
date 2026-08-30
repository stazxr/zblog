package com.github.stazxr.zblog.portal.job;

import com.github.stazxr.zblog.content.ext.mapper.FriendLinkStatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 友链月统计数清理任务
 *
 * @author SunTao
 * @since 2026-08-30
 */
@Component
@RequiredArgsConstructor
public class FriendLinkResetMonthCountLog {
    private final FriendLinkStatMapper friendLinkStatMapper;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetMonthClick() {
        friendLinkStatMapper.resetMonthClick();
    }
}
