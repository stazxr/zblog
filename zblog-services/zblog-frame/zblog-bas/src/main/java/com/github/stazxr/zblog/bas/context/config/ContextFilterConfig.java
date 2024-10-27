package com.github.stazxr.zblog.bas.context.config;

import com.github.stazxr.zblog.bas.context.filter.ContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Configuration class for registering ContextFilter as a servlet filter.
 *
 * @since 2024-05-25
 * @author SunTao
 */
@Configuration
public class ContextFilterConfig {
    private final ContextFilter contextFilter;

    /**
     * Constructor with dependency injection for ContextFilter.
     *
     * @param contextFilter The ContextFilter to be used
     */
    @Autowired
    public ContextFilterConfig(ContextFilter contextFilter) {
        this.contextFilter = contextFilter;
    }

    /**
     * Configures and registers ContextFilter as a servlet filter.
     *
     * @return The FilterRegistrationBean for ContextFilter
     */
    @Bean
    public FilterRegistrationBean<ContextFilter> loggingFilter() {
        FilterRegistrationBean<ContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(contextFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
