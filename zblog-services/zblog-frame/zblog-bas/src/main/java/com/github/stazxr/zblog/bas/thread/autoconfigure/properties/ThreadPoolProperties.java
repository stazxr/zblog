package com.github.stazxr.zblog.bas.thread.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池配置属性。
 *
 * @author SunTao
 * @since 2026-01-25
 */
@ConfigurationProperties(prefix = ThreadPoolProperties.CONFIG_PREFIX)
public class ThreadPoolProperties {
    static final String CONFIG_PREFIX = "zblog.base.thread-pool";

    /**
     * 核心线程数。
     * <p>
     * 线程池在空闲时仍然保持存活的线程数量，一般建议根据服务器 CPU 核心数进行合理配置。
     */
    private int coreSize = 1;

    /**
     * 最大线程数。
     * <p>
     * 当任务队列已满且核心线程全部占用时，线程池允许创建的最大线程数量。
     */
    private int maxSize = 2;

    /**
     * 任务队列容量。
     * <p>
     * 用于缓存尚未被执行的任务，队列过大会导致任务等待时间过长。
     */
    private int queueCapacity = 20;

    /**
     * 线程空闲存活时间（秒）。
     * <p>
     * 当线程数超过核心线程数时，多余线程在空闲达到该时间后会被回收。
     */
    private int keepAliveSeconds = 60;

    /**
     * 线程名称前缀。
     * <p>
     * 便于日志排查和线程定位，建议设置具有业务含义的前缀。
     */
    private String threadNamePrefix = "zblog-";

    /**
     * 应用关闭时是否等待线程池中的任务执行完成。
     * <p>
     * {@code true}：优雅关闭，等待任务完成后再退出
     * <br>
     * {@code false}：立即关闭，可能中断正在执行的任务
     */
    private boolean waitForTasksToCompleteOnShutdown = true;

    /**
     * 是否开启线程池监控日志
     */
    private boolean monitorEnabled = true;

    /**
     * 线程池监控日志打印间隔（毫秒）
     */
    private long monitorInterval = 10000;

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public boolean isWaitForTasksToCompleteOnShutdown() {
        return waitForTasksToCompleteOnShutdown;
    }

    public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
    }

    public boolean isMonitorEnabled() {
        return monitorEnabled;
    }

    public void setMonitorEnabled(boolean monitorEnabled) {
        this.monitorEnabled = monitorEnabled;
    }

    public long getMonitorInterval() {
        return monitorInterval;
    }

    public void setMonitorInterval(long monitorInterval) {
        this.monitorInterval = monitorInterval;
    }
}
