package com.github.stazxr.zblog.audit.chain;

import com.github.stazxr.zblog.audit.config.properties.AuditPolicyProperties;
import com.github.stazxr.zblog.audit.config.properties.AuditProcessorProperties;
import com.github.stazxr.zblog.audit.enums.AuditDecision;
import com.github.stazxr.zblog.audit.model.*;
import com.github.stazxr.zblog.audit.processor.AuditProcessor;
import com.github.stazxr.zblog.audit.registry.ProcessorRegistry;
import com.github.stazxr.zblog.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 审核责任链执行器
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
@Component
public class AuditChain {
    private static final Logger log = LoggerFactory.getLogger(AuditChain.class);

    private final ProcessorRegistry processorRegistry;

    public AuditChain(ProcessorRegistry processorRegistry) {
        this.processorRegistry = processorRegistry;
    }

    /**
     * 执行审核链路（核心方法）
     *
     * <p>
     * 执行流程说明：
     * <ol>
     *     <li>加载当前场景的 Processor 配置</li>
     *     <li>按 order 排序</li>
     *     <li>逐个执行 Processor</li>
     *     <li>根据 ProcessorResult 决定是否修改内容 / 是否终止链路</li>
     *     <li>汇总 hitWords / reason / trace</li>
     *     <li>生成最终 AuditResult</li>
     * </ol>
     *
     * @param context 审核上下文
     * @param policy  当前场景的审核策略（YAML配置）
     * @return 最终审核结果
     */
    public AuditResult execute(AuditContext context, AuditPolicyProperties policy) {
        // ========== 1. 基础校验 ==========
        if (policy == null || policy.getProcessors() == null) {
            throw new IllegalStateException("No audit policy configured");
        }

        String content = context.getOriginalContent(); // 当前处理内容（链路中会不断被修改）
        List<String> hitWords = new ArrayList<>(); // 命中词汇总
        StringBuilder reasonBuilder = new StringBuilder(); // 原因汇总（多个 Processor 叠加）
        List<ProcessorTrace> traces = new ArrayList<>(); // 执行链路追踪（用于审计 & 排查问题）

        // ========== 2. 按 order 排序 ==========
        List<AuditProcessorProperties> processors = policy.getProcessors();
        processors.sort(Comparator.comparingInt(AuditProcessorProperties::getOrder));

        // ========== 3. 执行 Processor 链 ==========
        for (AuditProcessorProperties config : processors) {
            // 3.1 跳过未启用 Processor（配置级开关）
            if (!config.isEnabled()) {
                continue;
            }

            // 3.2 获取 Processor 实例
            AuditProcessor processor = processorRegistry.get(config.getName());
            if (processor == null) {
                continue;
            }

            // ========== 3.3 执行 Processor ==========
            long start = System.currentTimeMillis(), cost;
            ProcessorResult result;
            try {
                result = processor.process(context);
                if (result == null || result.getDecision() == null) {
                    continue;
                }
                log.info("[Audit] processor={} status={}", config.getName(), result.getDecision().name());
            } catch (Exception e) {
                log.error("[Audit] processor={} process failed", config.getName(), e);
                cost = System.currentTimeMillis() - start;
                ProcessorTrace trace = new ProcessorTrace();
                trace.setProcessor(config.getName());
                trace.setCostMs(cost);
                trace.setStatus("ERROR");
                trace.setReason("EXCEPTION: " + e.getMessage());
                traces.add(trace);
                continue;
            }

            // ========== 4. 记录 Trace ==========
            ProcessorTrace trace = new ProcessorTrace();
            trace.setProcessor(config.getName());
            trace.setCostMs(System.currentTimeMillis() - start);
            trace.setStatus(result.getDecision().name());
            trace.setHitWords(result.getHitWords());
            trace.setReason(result.getReason());
            traces.add(trace);

            // ========== 5. 内容修改处理 ==========
            // MODIFY 表示该 Processor 对内容进行了修正
            if (result.getDecision() == AuditDecision.MODIFY) {
                if (StringUtils.isBlank(result.getContent())) {
                    // 经过替换后，文本为空字符串
                    result.setDecision(AuditDecision.MANUAL);
                    result.setReason("经过替换后，文本为空");
                } else {
                    content = result.getContent();
                    context.setContent(content);
                }
            }

            // ========== 6. 命中词汇总 ==========
            if (result.getHitWords() != null) {
                hitWords.addAll(result.getHitWords());
            }

            // ========== 7. 原因汇总 ==========
            if (result.getReason() != null) {
                reasonBuilder.append(result.getReason()).append(";");
            }

            // ========== 8. 强终止条件 ==========
            // REJECT：直接拒绝（立即终止链路）
            if (result.getDecision() == AuditDecision.REJECT) {
                return reject(content, hitWords, reasonBuilder.toString(), traces);
            }
            // MANUAL：进入人工审核（立即终止链路）
            if (result.getDecision() == AuditDecision.MANUAL) {
                return manual(content, hitWords, reasonBuilder.toString(), traces);
            }
        }

        return pass(content, hitWords, reasonBuilder.toString(), traces);
    }

    // ================= result =================

    private AuditResult pass(String content, List<String> hitWords, String reason, List<ProcessorTrace> traces) {
        AuditResult r = new AuditResult();
        r.setDecision(AuditDecision.PASS);
        r.setContent(content);
        r.setHitWords(hitWords);
        r.setReason(reason);
        r.setTraces(traces);
        return r;
    }

    private AuditResult reject(String content, List<String> hitWords, String reason, List<ProcessorTrace> traces) {
        AuditResult r = new AuditResult();
        r.setDecision(AuditDecision.REJECT);
        r.setContent(content);
        r.setHitWords(hitWords);
        r.setReason(reason);
        r.setTraces(traces);
        return r;
    }

    private AuditResult manual(String content, List<String> hitWords, String reason, List<ProcessorTrace> traces) {
        AuditResult r = new AuditResult();
        r.setDecision(AuditDecision.MANUAL);
        r.setContent(content);
        r.setHitWords(hitWords);
        r.setReason(reason);
        r.setTraces(traces);
        return r;
    }
}
