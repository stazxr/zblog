package com.github.stazxr.zblog.bas.log.autoconfigure;

import com.github.stazxr.zblog.bas.log.filter.LogContextFilter;
import com.github.stazxr.zblog.bas.order.FilterOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志上下文过滤器配置
 *
 * @author SunTao
 * @since 2026-02-04
 */
@Configuration
@RequiredArgsConstructor
public class LogContextFilterAutoConfiguration {
    @Bean
    public LogContextFilter logContextFilter() {
        return new LogContextFilter();
    }

    @Bean
    public FilterRegistrationBean<LogContextFilter> logContextFilterRegistration(LogContextFilter logContextFilter) {
        FilterRegistrationBean<LogContextFilter> bean = new FilterRegistrationBean<>(logContextFilter);
        bean.setOrder(FilterOrder.LOG_CONTEXT);
        return bean;
    }
}
