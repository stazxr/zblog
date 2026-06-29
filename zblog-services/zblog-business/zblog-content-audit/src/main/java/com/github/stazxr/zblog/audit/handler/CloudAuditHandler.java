//package com.github.stazxr.zblog.audit.handler;
//
//import com.github.stazxr.zblog.audit.AuditContext;
//import com.github.stazxr.zblog.audit.AuditResult;
//import com.github.stazxr.zblog.audit.model.AuditContext;
//import com.github.stazxr.zblog.audit.model.AuditResult;
//import com.github.stazxr.zblog.audit.provider.CloudAuditProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class CloudAuditHandler implements AuditHandler {
//
//    private final CloudAuditProvider cloudAuditProvider;
//
//    @Override
//    public AuditResult handle(AuditContext context) {
//
//        if (!cloudAuditProvider.pass(context.getContent())) {
//            return AuditResult.reject("云审核失败");
//        }
//
//        return AuditResult.pass();
//    }
//}