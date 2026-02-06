package com.github.stazxr.zblog.bas.thread.monitor;

import com.github.stazxr.zblog.bas.thread.autoconfigure.properties.ThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池状态监控器
 *
 * <p>定时打印线程池状态，帮助运维了解线程池负载情况。</p>
 */
@Component
public class ThreadPoolMonitor {
    private static final Logger log = LoggerFactory.getLogger("resource.monitor.threadpool");

    private final ThreadPoolTaskExecutor executor;

    private final ThreadPoolProperties properties;

    public ThreadPoolMonitor(ThreadPoolTaskExecutor executor, ThreadPoolProperties properties) {
        this.executor = executor;
        this.properties = properties;
    }

    /**
     * 定时打印线程池状态
     */
    @Scheduled(fixedRateString = "${zblog.base.thread-pool.monitor-interval}")
    public void monitor() {
        if (!properties.isMonitorEnabled()) {
            return;
        }

        ThreadPoolExecutor threadPool = executor.getThreadPoolExecutor();
        int corePoolSize = threadPool.getCorePoolSize();
        int maxPoolSize = threadPool.getMaximumPoolSize();
        int activeCount = threadPool.getActiveCount();
        long completedTaskCount = threadPool.getCompletedTaskCount();
        int queueSize = threadPool.getQueue().size();
        int queueRemainingCapacity = threadPool.getQueue().remainingCapacity();

        // 打印日志
        log.info("线程池监控 -> 核心线程数: {}, 最大线程数: {}, 活跃线程数: {}, 已完成任务数: {}, 队列大小: {}, 队列剩余容量: {}",
            corePoolSize, maxPoolSize, activeCount, completedTaskCount, queueSize, queueRemainingCapacity
        );
    }
}
