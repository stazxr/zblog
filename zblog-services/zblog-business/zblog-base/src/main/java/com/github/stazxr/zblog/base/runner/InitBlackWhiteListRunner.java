package com.github.stazxr.zblog.base.runner;

import com.github.stazxr.zblog.bas.security.cache.BlackWhiteListCache;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.util.thread.ThreadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 初始化黑白名单列表
 *
 * @author SunTao
 * @since 2022-02-05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitBlackWhiteListRunner extends BlackWhiteListCache implements CommandLineRunner, ApplicationListener<ApplicationStartedEvent> {
    /**
     * 是否开始刷新黑白名单
     */
    private static boolean start = false;

    private static final int REFRESH_INTERVAL_MINUTE = 5;

    private final DictService dictService;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        threadPoolTaskExecutor.execute(() -> {
            for (;;) {
                if (start) {
                    refreshRouterBlackWhiteList();
                }

                ThreadUtils.sleepMinute(REFRESH_INTERVAL_MINUTE);
            }
        });
    }

    private void refreshRouterBlackWhiteList() {
        try {
//            Map<String, String> routerWhiteList = dictService.selectItems(BaseConst.DictKey.ROUTER_WHITE_LIST);
//            Set<String> whiteList = new LinkedHashSet<>();
//            for (String url : routerWhiteList.keySet()) {
//                whiteList.add(routerWhiteList.get(url));
//            }
//            updateWhitelist(whiteList);
//
//            Map<String, String> routerBlackList = dictService.selectItems(BaseConst.DictKey.ROUTER_BLACK_LIST);
//            Set<String> blackList = new LinkedHashSet<>();
//            for (String url : routerBlackList.keySet()) {
//                blackList.add(routerBlackList.get(url));
//            }
//            updateBlacklist(blackList);
        } catch (Exception ex) {
            log.error("refreshRouterBlackWhiteList catch error", ex);
        }
    }

    public void start() {
        log.info("黑白名单刷新状态开启...");
        InitBlackWhiteListRunner.start = true;
        refreshRouterBlackWhiteList();
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
