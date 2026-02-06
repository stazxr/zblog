package com.github.stazxr.zblog.bas.context.autoconfigure;

import com.github.stazxr.zblog.bas.context.autoconfigure.properties.ContextProperties;
import com.github.stazxr.zblog.bas.context.filter.ContextFilter;
import com.github.stazxr.zblog.bas.order.FilterOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 上下文过滤器配置
 *
 * @author SunTao
 * @since 2026-02-04
 */
@Configuration
@RequiredArgsConstructor
public class ContextFilterAutoConfiguration {
    private final ContextProperties contextProperties;

    @Bean
    public ContextFilter contextFilter() {
        return new ContextFilter(contextProperties);
    }

    @Bean
    public FilterRegistrationBean<ContextFilter> contextFilterRegistration(ContextFilter contextFilter) {
        FilterRegistrationBean<ContextFilter> bean = new FilterRegistrationBean<>(contextFilter);
        bean.setOrder(FilterOrder.CONTEXT);
        return bean;
    }
}
