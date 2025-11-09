package com.github.stazxr.zblog.base.runner;

import com.github.stazxr.zblog.bas.security.cache.BlackWhiteListCache;
import com.github.stazxr.zblog.util.thread.ThreadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 初始化黑白名单列表
 *
 * @author SunTao
 * @since 2022-02-05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitBlackWhiteListRunner extends BlackWhiteListCache
        implements CommandLineRunner, ApplicationListener<ApplicationStartedEvent> {
    /**
     * 是否开始刷新黑白名单
     */
    private static boolean start = false;

    private static final int REFRESH_INTERVAL_MINUTE = 5;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    @SuppressWarnings("all")
    public void run(String... args) {
        threadPoolTaskExecutor.execute(() -> {
            for (;;) {
                if (start) {
                    refreshBlackWhiteList();
                }

                ThreadUtils.sleepMinute(REFRESH_INTERVAL_MINUTE);
            }
        });
    }

    private void refreshBlackWhiteList() {
        try {
            // TODO 暂不启用黑白名单功能
            updateWhitelist(null);
            updateBlacklist(null);
        } catch (Exception ex) {
            log.error("refreshBlackWhiteList catch error", ex);
        }
    }

    public void start() {
        log.info("黑白名单刷新状态开启...");
        InitBlackWhiteListRunner.start = true;
        refreshBlackWhiteList();
    }

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NotNull ApplicationStartedEvent event) {
        // 开启刷新黑白名单任务
        start();
    }
}
