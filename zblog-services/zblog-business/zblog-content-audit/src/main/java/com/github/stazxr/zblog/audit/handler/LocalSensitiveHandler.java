//package com.github.stazxr.zblog.audit.handler;
//
//import com.github.stazxr.zblog.audit.AuditContext;
//import com.github.stazxr.zblog.audit.AuditResult;
//import com.github.stazxr.zblog.audit.provider.SensitiveProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
///**
// * 本地敏感词审核处理器。
// *
// * 负责执行一级审核，
// * 检测是否命中本地敏感词规则。
// *
// * @author SunTao
// * @since 2026-06-29
// */
//@Component
//@RequiredArgsConstructor
//public class LocalSensitiveHandler implements AuditHandler {
//    /**
//     * 敏感词提供者。
//     */
//    private final SensitiveProvider sensitiveProvider;
//
//    /**
//     * 执行本地敏感词审核。
//     *
//     * @param context 审核上下文
//     * @return 审核结果
//     */
//    @Override
//    public AuditResult handle(AuditContext context) {
//        if (sensitiveProvider.contains(context.getContent())) {
//            return AuditResult.reject("内容包含敏感词");
//        }
//        return AuditResult.pass();
//    }
//}
