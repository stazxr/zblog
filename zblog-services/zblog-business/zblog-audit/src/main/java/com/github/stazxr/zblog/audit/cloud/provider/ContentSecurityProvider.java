package com.github.stazxr.zblog.audit.cloud.provider;

import com.github.stazxr.zblog.audit.model.ProcessorResult;

import java.util.Map;

/**
 * 内容安全云服务统一接口（Provider层）
 *
 * <p>职责：
 * <ul>
 *     <li>屏蔽不同云厂商实现（腾讯/阿里/火山等）</li>
 *     <li>提供统一审核能力抽象</li>
 *     <li>输出统一 ProcessorResult</li>
 * </ul>
 *
 * @author SunTao
 * @since 2026-07-05
 */
public interface ContentSecurityProvider {
    /**
     * 文本审核
     *
     * @param content 审核内容
     * @param params 扩展参数（如 bizType / userLevel / scene）
     * @return 统一审核结果
     */
    ProcessorResult checkText(String content, Map<String, Object> params);
}
