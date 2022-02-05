package com.github.stazxr.zblog.base.runner;

import com.github.stazxr.zblog.base.security.RouterBlackWhiteListCache;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.util.time.ThreadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 初始化路由黑白名单
 *
 * @author SunTao
 * @since 2022-02-05
 */
@Slf4j
@Component
@Order(11)
@RequiredArgsConstructor
public class InitRouterBlackWhiteListRunner extends RouterBlackWhiteListCache implements CommandLineRunner {
    private static final int CACHE_REFRESH_INTERVAL = 5;

    private final RouterService routerService;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        threadPoolTaskExecutor.execute(() -> {
            while (true) {
                refreshRouterBlackWhiteList();
                ThreadUtils.sleepMinute(CACHE_REFRESH_INTERVAL);
            }
        });
    }

    private void refreshRouterBlackWhiteList() {
        try {
            log.info("start refresh router black and white list...");
            Map<String, String> routerWhiteList = routerService.getRouterWhiteList();
            Set<String> whiteList = new LinkedHashSet<>();
            for (String url : routerWhiteList.keySet()) {
                whiteList.add(routerWhiteList.get(url));
            }
            setWhiteList(whiteList);
            log.info("router white list: {}", whiteList);

            Map<String, String> routerBlackList = routerService.getRouterBlackList();
            Set<String> blackList = new LinkedHashSet<>();
            for (String url : routerBlackList.keySet()) {
                blackList.add(routerBlackList.get(url));
            }
            setBlackList(blackList);
            log.info("router black list: {}", blackList);
            log.info("refresh router black and white list finish...");
        } catch (Exception e) {
            log.error("refresh router black and white list catch eor", e);
        }
    }
}
