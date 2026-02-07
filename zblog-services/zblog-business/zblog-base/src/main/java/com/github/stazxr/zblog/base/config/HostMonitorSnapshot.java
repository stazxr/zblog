package com.github.stazxr.zblog.base.config;

import com.github.stazxr.zblog.base.domain.bo.HostData;
import com.github.stazxr.zblog.base.service.impl.HostMonitorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 主机监控数据快照缓存
 *
 * <p>
 * 该组件负责：
 * <ul>
 *     <li>定时采集主机运行状态（CPU / 内存 / JVM / 磁盘等）</li>
 *     <li>将采集结果以“快照”的形式缓存在内存中</li>
 *     <li>为多个客户端提供统一、稳定、低成本的监控数据读取</li>
 * </ul>
 *
 * <p>
 * 设计理念：
 * <ul>
 *     <li>监控数据不应按请求实时采集，而应按时间定期采集</li>
 *     <li>采集线程与查询线程解耦，避免高并发场景下系统抖动</li>
 *     <li>所有客户端共享同一份节点监控状态</li>
 * </ul>
 *
 * <p>
 * 线程安全说明：
 * <ul>
 *     <li>使用 {@code volatile} 修饰快照对象，保证多线程可见性</li>
 *     <li>每次采集都会生成一个新的 {@link HostData} 实例</li>
 *     <li>通过“整体替换”的方式发布快照，避免加锁</li>
 * </ul>
 *
 * <p>
 * 适用场景：
 * <ul>
 *     <li>后台管理系统运行监控</li>
 *     <li>服务监控 / 数据监控页面</li>
 *     <li>高并发环境下的节点状态查询</li>
 * </ul>
 *
 * @author SunTao
 * @since 2025-12-31
 */
@Component
@EnableScheduling
public class HostMonitorSnapshot {
    private final Logger log = LoggerFactory.getLogger(HostMonitorSnapshot.class);

    /**
     * 当前主机监控数据快照
     *
     * <p>
     * 使用 volatile 保证：
     * <ul>
     *     <li>采集线程更新快照后，其他线程立即可见</li>
     *     <li>避免使用 synchronized 或锁，提高并发性能</li>
     * </ul>
     *
     * <p>
     * 快照对象一旦创建后不再修改，仅在 refresh() 中整体替换
     */
    private volatile HostData snapshot;

    /**
     * 实际的监控数据采集器
     *
     * <p>
     * 负责从 OSHI / JVM / 系统接口中获取原始监控数据
     */
    private final HostMonitorServiceImpl collector;

    public HostMonitorSnapshot(HostMonitorServiceImpl collector) {
        this.collector = collector;
    }

    /**
     * 容器启动完成后立即执行一次采集
     *
     * <p>
     * 防止在系统刚启动时，监控接口返回 null 或空数据
     */
    @PostConstruct
    public void init() {
        refresh();
    }

    /**
     * 定时刷新主机监控快照
     *
     * <p>
     * fixedDelay 表示：
     * <ul>
     *     <li>本次采集完成后，延迟 10 秒再执行下一次</li>
     *     <li>避免采集任务堆积或并发执行</li>
     * </ul>
     *
     * <p>
     * 即使采集过程中发生异常：
     * <ul>
     *     <li>也不会影响下一次任务调度</li>
     *     <li>会继续对外提供上一份有效快照</li>
     * </ul>
     */
    @Scheduled(fixedDelay = 10_000)
    public void refresh() {
        try {
            snapshot = collector.queryHostData();
        } catch (Exception e) {
            // 保留旧数据，防止监控接口因采集异常而不可用
            log.error("Host monitor snapshot refresh failed", e);
        }
    }

    /**
     * 获取当前主机监控数据快照
     *
     * <p>
     * 该方法不触发任何采集逻辑，仅返回内存中的快照数据，
     * 可被高频调用而不会对系统造成额外负担。
     *
     * @return 主机监控数据快照
     */
    public HostData getSnapshot() {
        return snapshot;
    }
}
