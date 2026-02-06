package com.github.stazxr.zblog.bas.thread.autoconfigure;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.github.stazxr.zblog.bas.thread.handler.FastFailRejectedExecutionHandler;
import com.github.stazxr.zblog.bas.thread.autoconfigure.properties.ThreadPoolProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 线程池配置
 *
 * @author SunTao
 * @since 2022-02-05
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolAutoConfiguration {
    /**
     * 线程池配置
     *
     * @return ThreadPoolTaskExecutor
     */
    @Bean
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(ThreadPoolProperties properties) {
        // 创建线程池
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数（可根据自己的服务器配置进行适当调整）
        executor.setCorePoolSize(properties.getCoreSize());
        // 设置最大线程数
        executor.setMaxPoolSize(properties.getMaxSize());
        // 设置队列容量
        executor.setQueueCapacity(properties.getQueueCapacity());
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        // 设置默认线程名称
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        // 设置拒绝策略，调用线程处理
        // AbortPolicy: 直接抛 RejectedExecutionException
        // CallerRunsPolicy: 不丢任务，由提交任务的线程自己执行这个任务
        // DiscardPolicy: 直接丢弃任务，不抛异常、不记录日志
        // DiscardOldestPolicy: 丢弃队列中最旧的任务，然后尝试提交新任务
        executor.setRejectedExecutionHandler(new FastFailRejectedExecutionHandler());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 初始化线程池
        executor.initialize();
        return executor;
    }

    @Bean("ttlExecutor")
    public Executor ttlExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return TtlExecutors.getTtlExecutor(threadPoolTaskExecutor);
    }
}
