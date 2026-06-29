package com.github.stazxr.zblog.audit.engine;

import com.github.stazxr.zblog.audit.AuditContext;
import com.github.stazxr.zblog.audit.AuditResult;
import com.github.stazxr.zblog.audit.handler.AuditHandler;
import com.github.stazxr.zblog.audit.model.AuditStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

/**
 * 内容审核引擎。
 *
 * <p>按照责任链模式依次执行所有审核处理器。
 *
 * <p>审核流程：
 *
 * <pre>
 * XSS审核
 *      ↓
 * 敏感词审核
 *      ↓
 * 内容规则审核
 *      ↓
 * 云审核
 *      ↓
 * 人工审核
 * </pre>
 *
 * <p>审核过程中：
 *
 * <ul>
 *     <li>PASS：继续执行后续处理器。</li>
 *     <li>REPLACE：更新审核内容后继续执行。</li>
 *     <li>REJECT：立即终止审核流程。</li>
 *     <li>PENDING：进入人工审核流程。</li>
 * </ul>
 *
 * @author SunTao
 * @since 2026-06-29
 */
@Component
@RequiredArgsConstructor
public class AuditEngine {
    /**
     * 审核处理器列表
     *
     * <p>Spring 会自动注入所有实现了 {@link AuditHandler} 接口的 Bean，并在初始化阶段按照顺序进行排序。
     */
    private final List<AuditHandler> handlers;

    /**
     * 初始化审核处理器顺序。
     *
     * <p>按照处理器定义的顺序值进行升序排序，数值越小优先级越高。
     */
    @PostConstruct
    public void init() {
        handlers.sort(Comparator.comparingInt(AuditHandler::getOrder));
    }

    /**
     * 执行内容审核。
     *
     * <p>首先将原始内容复制到审核内容字段，后续所有处理器均操作审核内容。
     *
     * <p>如果处理器返回：
     *
     * <ul>
     *     <li>PASS：继续执行下一处理器。</li>
     *     <li>REPLACE：更新审核内容后继续执行。</li>
     *     <li>REJECT：立即结束审核。</li>
     *     <li>PENDING：进入人工审核。</li>
     * </ul>
     *
     * @param context 审核上下文
     * @return 审核结果
     */
    public AuditResult audit(AuditContext context) {
        // 初始化审核内容，后续所有处理器都基于 auditContent 进行处理，保留原始内容便于日志记录和人工审核。
        context.setAuditContent(context.getContent());

        // 按顺序执行所有审核处理器。
        for (AuditHandler handler : handlers) {
            // 如果当前处理器未启用，则跳过。
            if (!handler.support(context)) {
                continue;
            }

            // 当前处理器执行审核。
            AuditResult result = handler.handle(context);

            // 审核拒绝，立即终止后续处理。
            if (result.getStatus() == AuditStatus.REJECT) {
                return result;
            }

            // 待人工审核，结束自动审核流程。
            if (result.getStatus() == AuditStatus.PENDING) {
                return result;
            }

            // 内容被修改，例如：XSS清洗、敏感词替换等。
            if (result.getStatus() == AuditStatus.REPLACE) {
                context.setAuditContent(result.getContent());
            }
        }

        // 所有处理器执行完成，返回最终审核后的内容。
        return AuditResult.pass(context.getAuditContent());
    }
}