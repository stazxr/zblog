package com.github.stazxr.zblog.bas.thread.handler;

import com.github.stazxr.zblog.bas.exception.SystemException;
import com.github.stazxr.zblog.bas.exception.code.CommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池过载拒绝策略（快速失败）
 *
 * <p>
 * 行为说明：
 * 当线程池已达到最大线程数，且任务队列已满时：
 * 1. 记录详细告警日志（线程池状态）
 * 2. 直接抛出 RejectedExecutionException
 * 3. 由上层统一返回「系统繁忙，请稍后再试」
 * </p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
public class FastFailRejectedExecutionHandler implements RejectedExecutionHandler {
    private static final Logger log = LoggerFactory.getLogger("resource.monitor.threadpool.error");

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 线程池关键运行指标（非常重要，方便排查问题）
        int corePoolSize = executor.getCorePoolSize();
        int maxPoolSize = executor.getMaximumPoolSize();
        int activeCount = executor.getActiveCount();
        int poolSize = executor.getPoolSize();
        int queueSize = executor.getQueue().size();
        int queueRemainingCapacity = executor.getQueue().remainingCapacity();

        // 打印日志
        log.error("线程池已满，触发拒绝策略！corePoolSize={}, maxPoolSize={}, poolSize={}, " +
            "activeCount={}, queueSize={}, queueRemainingCapacity={}", corePoolSize, maxPoolSize,
            poolSize, activeCount, queueSize, queueRemainingCapacity
        );

        // 快速失败
        throw new SystemException(CommonErrorCode.SBASEA001);
    }
}
