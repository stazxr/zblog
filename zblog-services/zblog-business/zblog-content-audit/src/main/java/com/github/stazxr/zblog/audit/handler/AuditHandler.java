package com.github.stazxr.zblog.audit.handler;

import com.github.stazxr.zblog.audit.AuditContext;
import com.github.stazxr.zblog.audit.AuditResult;

/**
 * 内容审核处理器。
 *
 * <p>审核处理器采用责任链模式实现，每个处理器负责一种审核能力，例如：
 *
 * <ul>
 *     <li>XSS审核</li>
 *     <li>敏感词审核</li>
 *     <li>内容规则审核</li>
 *     <li>云审核</li>
 *     <li>人工审核</li>
 * </ul>
 *
 * <p>处理器按照 {@link #getOrder()} 的顺序执行，数值越小优先级越高。
 *
 * @author SunTao
 * @since 2026-06-29
 */
public interface AuditHandler {
    /**
     * 获取处理器顺序。
     *
     * <p>数值越小优先级越高。
     *
     * <pre>
     * XSS审核：100
     * 敏感词审核：200
     * 内容规则：300
     * 云审核：400
     * 人工审核：500
     * </pre>
     *
     * @return 顺序值
     */
    int getOrder();

    /**
     * 获取处理器名称。
     *
     * <p>用于日志记录、监控以及调试。
     *
     * @return 处理器名称
     */
    default String getName() {
        return getClass().getSimpleName();
    }

    /**
     * 当前处理器是否启用。
     *
     * <p>子类可根据配置动态决定是否参与审核。
     *
     * @param context 审核上下文
     * @return true：启用
     */
    default boolean support(AuditContext context) {
        return true;
    }

    /**
     * 执行审核。
     *
     * @param context 审核上下文
     * @return 审核结果
     */
    AuditResult handle(AuditContext context);
}
