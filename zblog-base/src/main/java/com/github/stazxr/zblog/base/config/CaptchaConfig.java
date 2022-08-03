package com.github.stazxr.zblog.base.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取kaptcha.properties配置文件，生成DefaultKaptcha的配置信息
 *
 *   20220727: 类弃用
 *
 * @author SunTao
 * @since 2020-11-14
 */
@Slf4j
@Deprecated
@Configuration
public class CaptchaConfig {
    /**
     * defaultKaptcha
     *
     * @return defaultKaptcha
     * @throws IOException read kaptcha.properties failed.
     */
    @Bean
    public DefaultKaptcha defaultKaptcha() throws IOException {
        final Properties properties = new Properties();
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        properties.load(CaptchaConfig.class.getClassLoader().getResourceAsStream("conf/kaptcha.properties"));
        defaultKaptcha.setConfig(new Config(properties));
        return defaultKaptcha;
    }
}
