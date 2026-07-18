package com.github.stazxr.zblog.audit.engine;

import com.github.stazxr.zblog.audit.chain.AuditChain;
import com.github.stazxr.zblog.audit.config.properties.AuditPolicyProperties;
import com.github.stazxr.zblog.audit.config.properties.AuditProperties;
import com.github.stazxr.zblog.audit.enums.AuditDecision;
import com.github.stazxr.zblog.audit.enums.AuditScene;
import com.github.stazxr.zblog.audit.model.AuditContext;
import com.github.stazxr.zblog.audit.model.AuditRecord;
import com.github.stazxr.zblog.audit.model.AuditResult;
import com.github.stazxr.zblog.audit.record.AuditRecordAsyncWriter;
import com.github.stazxr.zblog.util.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 审核引擎
 *
 * <p>
 * 负责：
 * 1. 策略选择
 * 2. 默认兜底
 * 3. 总控执行
 * </p>
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
@Component
public class AuditEngine {
    private static final Logger log = LoggerFactory.getLogger(AuditEngine.class);
    private final AuditProperties auditProperties;
    private final AuditChain auditChain;
    private final AuditRecordAsyncWriter auditRecordAsyncWriter;

    public AuditEngine(AuditProperties auditProperties, AuditChain auditChain, AuditRecordAsyncWriter auditRecordAsyncWriter) {
        this.auditProperties = auditProperties;
        this.auditChain = auditChain;
        this.auditRecordAsyncWriter = auditRecordAsyncWriter;
    }

    /**
     * 执行审核
     */
    public AuditResult audit(AuditContext context) {
        // ========== 1. 基础校验 ==========
        if (context == null || context.getOriginalContent() == null || context.getScene() == null) {
            throw new IllegalArgumentException("AuditContext invalid");
        }

        // 初始化可变内容
        context.setContent(context.getOriginalContent());

        // ========== 2. 策略解析 ==========
        AuditPolicyProperties policy = getPolicy(context.getScene());

        // ========== 3. 执行责任链 ==========
        long startTime = System.currentTimeMillis(), costMs = 0L;
        AuditResult result;
        try {
            result = auditChain.execute(context, policy);
        } catch (Exception e) {
            log.error("AuditChain execute failed", e);
            result = new AuditResult();
            result.setDecision(AuditDecision.MANUAL);
            result.setReason("Audit Chain Error: " + e.getMessage());
        } finally {
            costMs = System.currentTimeMillis() - startTime;
        }

        // ========== 4. 构建审计记录并持久化 ==========
        AuditRecord record = buildRecord(context, result);
        record.setCostMs(costMs);
        persist(record);

        // ========== 6. 返回结果 ==========
        return result;
    }

    /**
     * 获取策略（核心逻辑）
     */
    private AuditPolicyProperties getPolicy(AuditScene scene) {
        if (scene == null) {
            throw new IllegalStateException("AuditScene cannot be null");
        }

        // 1. YAML中查找
        AuditPolicyProperties policy = auditProperties.getPolicies().get(scene.name());
        if (policy != null) {
            return policy;
        }

        // 2. fallback 默认策略
        throw new IllegalStateException("No audit policy configured for scene: " + scene);
    }

    /**
     * 构建审核记录
     */
    private AuditRecord buildRecord(AuditContext context, AuditResult result) {
        AuditRecord record = new AuditRecord();
        record.setId(UuidUtils.genUuidStr());
        record.setUid(context.getUid());
        record.setOid(context.getOid());
        record.setScene(context.getScene());
        record.setOriginalContent(context.getOriginalContent());
        record.setFinalContent(result.getContent());
        record.setDecision(result.getDecision());
        record.setHitWords(result.getHitWords());
        record.setReason(result.getReason());
        record.setTraces(result.getTraces());
        return record;
    }

    /**
     * 持久化审核记录
     */
    private void persist(AuditRecord record) {
        try {
            auditRecordAsyncWriter.write(record);
        } catch (Exception ignored) { }
    }
}
