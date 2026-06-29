//package com.github.stazxr.zblog.audit.handler;
//
//import com.github.stazxr.zblog.audit.AuditContext;
//import com.github.stazxr.zblog.audit.AuditResult;
//import com.github.stazxr.zblog.audit.provider.SensitiveProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
///**
// * 敏感词审核处理器
// *
// * @author SunTao
// * @since 2026-06-29
// */
//@Component
//@RequiredArgsConstructor
//public class SensitiveAuditHandler implements AuditHandler {
//    private final SensitiveProvider sensitiveProvider;
//
//    @Override
//    public int getOrder() {
//        return 200;
//    }
//
//    @Override
//    public AuditResult handle(AuditContext context) {
//        String content = context.getAuditContent();
//        if (!sensitiveProvider.contains(content)) {
//            return AuditResult.pass(content);
//        }
//
//        String replace = sensitiveProvider.replace(content);
//
//        return AuditResult.replace(replace, sensitiveProvider.findAll(content));
//    }
//}