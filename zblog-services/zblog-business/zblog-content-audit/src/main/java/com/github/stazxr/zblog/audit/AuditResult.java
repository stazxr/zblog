package com.github.stazxr.zblog.audit;

import com.github.stazxr.zblog.audit.model.AuditStatus;

import java.util.Collections;
import java.util.List;

/**
 * 审核结果
 *
 * @author SunTao
 * @since 2026-06-29
 */
public class AuditResult {
    /**
     * 审核状态
     */
    private AuditStatus status;

    /**
     * 审核原因
     */
    private String reason;

    /**
     * 最终内容
     */
    private String content;

    /**
     * 命中的关键字
     */
    private List<String> hitWords;

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getHitWords() {
        return hitWords;
    }

    public void setHitWords(List<String> hitWords) {
        this.hitWords = hitWords;
    }

    /**
     * 审核通过
     *
     * @param content 最终内容
     * @return 审核结果
     */
    public static AuditResult pass(String content) {
        AuditResult result = new AuditResult();
        result.setStatus(AuditStatus.PASS);
        result.setContent(content);
        result.setHitWords(Collections.emptyList());
        return result;
    }

    /**
     * 内容被替换
     *
     * @param content 替换后的内容
     * @return 审核结果
     */
    public static AuditResult replace(String content) {
        AuditResult result = new AuditResult();
        result.setStatus(AuditStatus.REPLACE);
        result.setContent(content);
        result.setHitWords(Collections.emptyList());
        return result;
    }

    /**
     * 内容被替换
     *
     * @param content 替换后的内容
     * @param hitWords 命中的敏感词
     * @return 审核结果
     */
    public static AuditResult replace(String content, List<String> hitWords) {
        AuditResult result = new AuditResult();
        result.setStatus(AuditStatus.REPLACE);
        result.setContent(content);
        result.setHitWords(hitWords);
        return result;
    }

    /**
     * 审核拒绝
     *
     * @param reason 拒绝原因
     * @return 审核结果
     */
    public static AuditResult reject(String reason) {
        AuditResult result = new AuditResult();
        result.setStatus(AuditStatus.REJECT);
        result.setReason(reason);
        result.setHitWords(Collections.emptyList());
        return result;
    }

    /**
     * 待人工审核
     *
     * @param reason 待审核原因
     * @return 审核结果
     */
    public static AuditResult pending(String reason) {
        AuditResult result = new AuditResult();
        result.setStatus(AuditStatus.PENDING);
        result.setReason(reason);
        result.setHitWords(Collections.emptyList());
        return result;
    }
}
