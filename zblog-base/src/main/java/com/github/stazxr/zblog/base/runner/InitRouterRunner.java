package com.github.stazxr.zblog.base.runner;

import com.github.stazxr.zblog.base.component.router.RouterManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 在项目启动时，持久化系统路由列表
 *
 * @author SunTao
 * @since 2020-11-18
 */
@Slf4j
@Component
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
            log.info("===> Start Init Router");
            routerManager.initRouter();
        } catch (Exception ex) {
            log.error("Init Router Failed, Exit system", ex);
            System.exit(1);
        }

        log.info("Init Router End <===");
    }
}
