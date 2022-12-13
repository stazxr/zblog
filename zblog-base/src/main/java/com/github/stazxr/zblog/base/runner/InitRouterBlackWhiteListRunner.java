package com.github.stazxr.zblog.base.runner;

import com.github.stazxr.zblog.base.component.security.RouterBlackWhiteListCache;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.util.thread.ThreadUtils;
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

    private static final int CACHE_REFRESH_INTERVAL = 5;

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
                }

                ThreadUtils.sleepMinute(CACHE_REFRESH_INTERVAL);
            }
        });
    }

    private void refreshRouterBlackWhiteList() {
        try {
            Map<String, String> routerWhiteList = dictService.selectItems(BaseConst.DictKey.ROUTER_WHITE_LIST);
            Set<String> whiteList = new LinkedHashSet<>();
            for (String url : routerWhiteList.keySet()) {
                whiteList.add(routerWhiteList.get(url));
            }
            setWhiteList(whiteList);

            Map<String, String> routerBlackList = dictService.selectItems(BaseConst.DictKey.ROUTER_BLACK_LIST);
            Set<String> blackList = new LinkedHashSet<>();
            for (String url : routerBlackList.keySet()) {
                blackList.add(routerBlackList.get(url));
            }
            setBlackList(blackList);
        } catch (Exception ex) {
            log.error("refreshRouterBlackWhiteList catch error", ex);
        }
    }

    public static void start() {
        log.info("路由黑白名单刷新状态开启");
        InitRouterBlackWhiteListRunner.start = true;
    }
}
