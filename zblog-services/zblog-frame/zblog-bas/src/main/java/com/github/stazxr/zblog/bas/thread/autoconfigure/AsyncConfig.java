package com.github.stazxr.zblog.bas.thread.autoconfigure;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

/**
 * 异步线程池配置
 *
 * @author SunTao
 * @since 2026-02-05
 */
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    private final Executor ttlExecutor;

    public AsyncConfig(@Qualifier("ttlExecutor") Executor ttlExecutor) {
        this.ttlExecutor = ttlExecutor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return ttlExecutor;
    }
}
