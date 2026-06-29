package com.github.stazxr.zblog.audit.handler;

import com.github.stazxr.zblog.audit.AuditContext;
import com.github.stazxr.zblog.audit.AuditResult;
import com.github.stazxr.zblog.audit.config.AuditProperties;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * XSS审核处理器。
 *
 * <p>
 * 用于检测和处理用户提交内容中的恶意脚本代码。
 * </p>
 *
 * <p>
 * 当前支持：
 * </p>
 *
 * <ul>
 *     <li>script标签</li>
 *     <li>img onerror事件</li>
 *     <li>javascript协议</li>
 *     <li>HTML实体编码</li>
 *     <li>Unicode编码</li>
 *     <li>大小写混淆攻击</li>
 * </ul>
 *
 * <p>
 * 审核流程：
 * </p>
 *
 * <pre>
 * 原始内容
 *     ↓
 * ESAPI规范化
 *     ↓
 * Jsoup标签清洗
 *     ↓
 * 根据配置决定：
 *     REPLACE：自动清洗
 *     REJECT ：直接拒绝
 *     PASS   ：允许通过
 * </pre>
 *
 * @author SunTao
 * @since 2026-06-29
 */
@Component
@RequiredArgsConstructor
public class XssAuditHandler implements AuditHandler {
    private static final Logger log = LoggerFactory.getLogger(XssAuditHandler.class);

    /**
     * 审核配置
     */
    private final AuditProperties auditProperties;

    /**
     * 获取处理器顺序。
     *
     * <p>
     * 数值越小优先级越高。
     * </p>
     *
     * @return 顺序值
     */
    @Override
    public int getOrder() {
        return 100;
    }

    /**
     * 获取处理器名称。
     *
     * @return 处理器名称
     */
    @Override
    public String getName() {
        return "XSS审核";
    }

    /**
     * 当前处理器是否启用。
     *
     * @param context 审核上下文
     * @return true：启用
     */
    @Override
    public boolean support(AuditContext context) {
        return auditProperties.isEnabled() && auditProperties.getXss().isEnabled();
    }

    /**
     * 执行XSS审核。
     *
     * @param context 审核上下文
     * @return 审核结果
     */
    @Override
    public AuditResult handle(AuditContext context) {
        // 获取待审核内容。
        String content = context.getAuditContent();

        // 内容为空直接通过。
        if (content == null || content.isEmpty()) {
            return AuditResult.pass(content);
        }

        // 规范化后的内容。
        String normalize = content;

        try {
            normalize = ESAPI.encoder().canonicalize(content);
        } catch (Exception e) {
            log.error("XSS规范化失败，已忽略，content={}", content, e);
        }

        // 清洗后的内容。
        String clean = normalize;

        try {
            /*
             * Jsoup清洗HTML标签。
             *
             * Safelist.none() 表示不允许任何HTML标签。
             */
            clean = Jsoup.clean(normalize, Safelist.none());
        } catch (Exception e) {
            log.error("XSS内容清洗失败，已忽略，content={}", normalize, e);
        }

        /*
         * 内容发生变化，说明存在XSS内容。
         */
        if (!normalize.equals(clean)) {
            log.debug("检测到XSS内容，before={}, after={}", normalize, clean);

            // 拒绝策略
            if (auditProperties.getXss().isReject()) {
                return AuditResult.reject("内容包含非法脚本");
            }

            // 自动替换策略
            if (auditProperties.getXss().isReplace()) {
                return AuditResult.replace(clean);
            }

            // 未开启任何处理策略
            return AuditResult.pass(content);
        }

        // 未发现XSS，直接通过
        return AuditResult.pass(clean);
    }

    /**
     * URL多次解码。
     *
     * <p>支持：
     *
     * <pre>
     * %3Cscript%3E
     * %253Cscript%253E
     * </pre>
     *
     * @param content 原始内容
     * @return 解码结果
     */
    private String decode(String content) {
        String result = content;
        try {
            for (int i = 0; i < 2; i++) {
                String decode = URLDecoder.decode(result, StandardCharsets.UTF_8.name());
                if (decode.equals(result)) {
                    break;
                }
                result = decode;
            }
        } catch (Exception ignored) {
        }

        return result;
    }
}