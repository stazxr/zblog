//package com.github.stazxr.zblog.audit.handler;
//
//import com.github.stazxr.zblog.audit.AuditContext;
//import com.github.stazxr.zblog.audit.AuditResult;
//import com.github.stazxr.zblog.audit.provider.ManualAuditProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ManualAuditHandler implements AuditHandler {
//
//    private final ManualAuditProvider manualAuditProvider;
//
//    @Override
//    public AuditResult handle(AuditContext context) {
//        manualAuditProvider.submit(context.getContent());
//        return AuditResult.pending();
//    }
//}
