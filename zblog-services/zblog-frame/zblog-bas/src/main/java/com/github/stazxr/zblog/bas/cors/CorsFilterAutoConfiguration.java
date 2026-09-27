package com.github.stazxr.zblog.bas.cors;

import com.github.stazxr.zblog.bas.order.FilterOrder;
import com.github.stazxr.zblog.bas.security.jwt.JwtConstants;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CORS 过滤器配置
 *
 * @author SunTao
 * @since 2026-02-04
 */
@Configuration
@EnableConfigurationProperties(CorsProperties.class)
public class CorsFilterAutoConfiguration {
    private final CorsProperties corsProperties;

    public CorsFilterAutoConfiguration(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList(corsProperties.getAllowedOriginPatternArray()));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.addExposedHeader(JwtConstants.X_TOKEN_STATUS);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration(CorsFilter corsFilter) {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(corsFilter);
        bean.setOrder(FilterOrder.CORS);
        return bean;
    }
}
