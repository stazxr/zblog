package com.github.stazxr.zblog.base.security.config;

import com.github.stazxr.zblog.base.security.jwt.*;
import com.github.stazxr.zblog.base.security.jwt.cache.JwtTokenStorage;
import com.github.stazxr.zblog.core.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JwtConfiguration
 *
 * @author SunTao
 * @since 2022-01-20
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {
//    /**
//     * JwtTokenStorage
//     *
//     * @return JwtTokenStorage
//     */
//    @Bean
//    public JwtTokenStorage jwtTokenStorage() {
//        return SpringContextUtils.getBean("jwtTokenStorage", JwtTokenStorage.class);
//    }

    /**
     * JwtProperties
     *
     * @return JwtProperties
     */
    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    /**
     * JwtTokenGenerator
     *
     * @param jwtTokenStorage JwtTokenStorage
     * @param jwtProperties jwtProperties
     * @return JwtTokenGenerator
     */
    @Bean
    public JwtTokenGenerator jwtTokenGenerator(JwtTokenStorage jwtTokenStorage, JwtProperties jwtProperties) {
        return new JwtTokenGenerator(jwtTokenStorage, jwtProperties);
    }
}
