package com.github.stazxr.zblog.bas.order;

import org.springframework.core.Ordered;

/**
 * 过滤器调用顺序配置
 *
 * @author SunTao
 * @since 2026-02-04
 */
public interface FilterOrder {
    /**
     * 上下文：Context初始化（请求头，部署信息，traceID，LoginID）
     */
    int CORS = Ordered.HIGHEST_PRECEDENCE;

    /**
     * 上下文：Context初始化（请求头，部署信息，traceID，LoginID）
     */
    int CONTEXT = CORS + 10;

    /**
     * 日志上下文过滤器：MDC初始化，traceId（从上下文获取），请求开始时间
     */
    int LOG_CONTEXT = CONTEXT + 10;

    /**
     * 请求日志打印：traceId，操作用户等
     */
    int REQ_LOG = LOG_CONTEXT + 100;

    /**
     * 响应日志打印：traceId，耗时时间，操作用户，操作结果等
     */
    int RES_LOG = REQ_LOG + 100;

    /**
     * 统一响应
     */
    int REST_ADVICE = Ordered.LOWEST_PRECEDENCE;

    /**
     * 全局异常处理
     */
    int GLOBAL_EXP_ADVICE = REST_ADVICE - 100;

    /**
     * 校验异常处理
     */
    int VALIDATION_EXP_ADVICE = GLOBAL_EXP_ADVICE - 100;

//    int MDC_BEFORE = TRACE + 10;
//    int JWT = TRACE + 100;
//    int LOGIN_VALIDATE = TRACE + 200;
//    int MDC_AFTER = Ordered.LOWEST_PRECEDENCE;
}
