package com.github.stazxr.zblog.base.runner;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
@AllArgsConstructor
public class SetRouteRunner implements CommandLineRunner {
    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
    }
}
