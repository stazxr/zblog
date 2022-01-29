package com.github.stazxr.zblog.base.runner;

import com.github.stazxr.zblog.base.manager.RouterManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 刷新系统路由列表
 *
 * @author SunTao
 * @since 2020-11-18
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SetRouteRunner implements CommandLineRunner {
    private final RouterManager routerManager;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        try {
            routerManager.initRouter();
        } catch (Exception e) {
            log.error("init router catch eor", e);
            System.exit(1);
        }
    }
}