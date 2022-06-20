package com.github.stazxr.zblog.base.runner;

import com.github.stazxr.zblog.base.manager.RouterManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化系统路由列表
 *
 * @author SunTao
 * @since 2020-11-18
 */
@Slf4j
@Component
@Order(21)
@RequiredArgsConstructor
public class InitRouterRunner implements CommandLineRunner {
    private final RouterManager routerManager;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        try {
            log.info("start init router...");
            routerManager.initRouter();
            log.info("init router finish...");
        } catch (Exception e) {
            log.error("init router catch eor", e);
        }
    }
}
