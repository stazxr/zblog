package com.github.stazxr.zblog.bas.notify.mail.autoconfigure;

import com.github.stazxr.zblog.bas.notify.mail.MailService;
import com.github.stazxr.zblog.bas.notify.mail.autoconfigure.properties.MailFromProperties;
import com.github.stazxr.zblog.bas.notify.mail.impl.MailServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮箱自动配置
 *
 * @author SunTao
 * @since 2026-01-16
 */
@Configuration
@EnableConfigurationProperties(value = { MailFromProperties.class })
public class MailAutoConfiguration {
    @Bean
    public MailService mailService(MailFromProperties fromProperties, JavaMailSender javaMailSender) {
        return new MailServiceImpl(fromProperties, javaMailSender);
    }
}
