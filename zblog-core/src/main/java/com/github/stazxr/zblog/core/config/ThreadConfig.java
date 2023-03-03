package com.github.stazxr.zblog.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author SunTao
 * @since 2022-02-05
 */
@EnableAsync
@Configuration
public class ThreadConfig {
    /**
     * ThreadPoolTaskExecutor 配置
     *
     * @return ThreadPoolTaskExecutor
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 设置核心线程数（可根据自己的服务器配置进行适当调整）
        executor.setCorePoolSize(2);

        // 设置最大线程数
        executor.setMaxPoolSize(10);

        // 设置队列容量
        executor.setQueueCapacity(20);

        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);

        // 设置默认线程名称
        executor.setThreadNamePrefix("zblog-");

        // 设置拒绝策略，调用线程处理
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
