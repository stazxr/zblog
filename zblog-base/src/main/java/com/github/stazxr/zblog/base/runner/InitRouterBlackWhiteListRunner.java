package com.github.stazxr.zblog.base.runner;

import com.github.stazxr.zblog.base.component.security.RouterBlackWhiteListCache;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.util.time.ThreadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
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
@RequiredArgsConstructor
public class InitRouterBlackWhiteListRunner extends RouterBlackWhiteListCache implements CommandLineRunner {
    /**
     * 是否开始刷新黑白名单
     */
    private static boolean start = false;

    private static final int CACHE_REFRESH_INTERVAL_1 = 5;

    private static final int CACHE_REFRESH_INTERVAL_2 = 10;

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
            while (true) {
                if (start) {
                    refreshRouterBlackWhiteList();
                    ThreadUtils.sleepMinute(CACHE_REFRESH_INTERVAL_1);
                } else {
                    ThreadUtils.sleepSecond(CACHE_REFRESH_INTERVAL_2);
                }
            }
        });
    }

    private void refreshRouterBlackWhiteList() {
        try {
            log.info("Start refresh router black and white list...");
            Map<String, String> routerWhiteList = dictService.selectItems(BaseConst.DictKey.ROUTER_WHITE_LIST);
            Set<String> whiteList = new LinkedHashSet<>();
            for (String url : routerWhiteList.keySet()) {
                whiteList.add(routerWhiteList.get(url));
            }
            setWhiteList(whiteList);
            log.info("Router White List: {}", whiteList);

            Map<String, String> routerBlackList = dictService.selectItems(BaseConst.DictKey.ROUTER_BLACK_LIST);
            Set<String> blackList = new LinkedHashSet<>();
            for (String url : routerBlackList.keySet()) {
                blackList.add(routerBlackList.get(url));
            }
            setBlackList(blackList);
            log.info("Router Black List: {}", blackList);
            log.info("Refresh router black and white list finish...");
        } catch (Exception e) {
            log.error("Refresh router black and white list catch eor", e);
        }
    }

    public static void start() {
        InitRouterBlackWhiteListRunner.start = true;
    }
}
