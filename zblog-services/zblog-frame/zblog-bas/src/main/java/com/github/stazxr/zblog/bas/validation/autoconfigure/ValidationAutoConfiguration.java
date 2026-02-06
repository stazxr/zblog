package com.github.stazxr.zblog.bas.validation.autoconfigure;

import com.github.stazxr.zblog.bas.i18n.autoconfigure.I18nAutoConfiguration;
import com.github.stazxr.zblog.bas.validation.I18nMessageInterpolator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 *
 *
 * @author SunTao
 * @since 2025-08-13
 */
@Configuration
@AutoConfigureAfter(I18nAutoConfiguration.class)
public class ValidationAutoConfiguration {
    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setMessageInterpolator(new I18nMessageInterpolator());
        return factoryBean;
    }
}
