package com.github.stazxr.zblog.audit.processor;

import com.github.stazxr.zblog.audit.config.properties.XssProcessorConfig;
import com.github.stazxr.zblog.audit.enums.XssStrategy;
import com.github.stazxr.zblog.audit.model.AuditContext;
import com.github.stazxr.zblog.audit.model.ProcessorResult;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * XSS 审核处理器
 *
 * @author Sun Tao
 * @since 2026-07-05
 */
@Component
public class XssAuditProcessor extends AbstractAuditProcessor {
    private static final Logger log = LoggerFactory.getLogger(XssAuditProcessor.class);

    private static final Safelist SAFE_LIST = Safelist.relaxed()
        // 移除高危标签
        .removeTags("script", "iframe", "style", "object", "embed", "meta", "link")
        // a 只允许安全属性
        .addAttributes("a", "href", "title", "target", "rel")
        // img 只允许安全属性
        .addAttributes("img", "src", "alt", "title")
        // 协议控制
        .addProtocols("a", "href", "http", "https", "mailto")
        .addProtocols("img", "src", "http", "https", "data");

    private static final XssStrategy DEFAULT_STRATEGY = XssStrategy.MANUAL;

    private final XssProcessorConfig xssProcessorConfig;

    public XssAuditProcessor(XssProcessorConfig xssProcessorConfig) {
        this.xssProcessorConfig = xssProcessorConfig;
    }

    @Override
    public String name() {
        return "xssProcessor";
    }

    @Override
    public ProcessorResult process(AuditContext context) {
        String row = context.getContent();
        if (row == null) {
            return pass();
        }

        // 解码
        String decodeRow = urlDecodeLoop(row);

        // 规范化后的内容
        String normalizeRow = decodeRow;

        try {
            normalizeRow = ESAPI.encoder().canonicalize(decodeRow);
        } catch (Exception e) {
            log.error("XSS规范化失败", e);
        }

        // 清洗后的内容。
        String cleanRow = normalizeRow;

        try {
            /*
             * Jsoup清洗HTML标签。
             *
             * Safelist.none() 表示不允许任何HTML标签。
             */
            cleanRow = Jsoup.clean(normalizeRow, SAFE_LIST);
        } catch (Exception e) {
            log.error("XSS内容清洗失败", e);
        }

        if (!normalizeRow.equals(cleanRow)) {
            Map<String, XssStrategy> scenes = xssProcessorConfig.getScenes();
            XssStrategy strategy = xssProcessorConfig.getStrategy();
            String sceneKey = context.getScene().name() + ".strategy";
            if (scenes != null && scenes.containsKey(sceneKey)) {
                strategy = scenes.get(sceneKey);
            }
            if (strategy == null) {
                strategy = DEFAULT_STRATEGY;
            }

            switch (strategy) {
                case CLEAN:
                    return modify(cleanRow, Collections.singletonList("XSS"));
                case REJECT:
                    return reject("XSS检测命中", Collections.singletonList("XSS"));
                case MANUAL:
                    return manual("XSS检测命中", Collections.singletonList("XSS"));
            }
        }

        return pass();
    }

    @Override
    public boolean support(AuditContext context) {
        return true;
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
    private String urlDecodeLoop(String content) {
        String result = content;
        try {
            while (true) {
                String decoded = URLDecoder.decode(result, StandardCharsets.UTF_8.name());
                if (decoded.equals(result)) break;
                result = decoded;
            }
            return result;
        } catch (Exception ignored) {
            return content;
        }
    }
}
