package com.github.stazxr.zblog.base.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.nimbusds.jose.util.StandardCharset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * 初始化验证码配置信息，并丢入系统缓存中
 *
 * @author SunTao
 * @since 2022-05-20
 */
@Slf4j
@Component
public class InitCaptchaConfigRunner implements CommandLineRunner {
    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        log.info("===> Start Init Captcha From 'conf/kaptchaConfig.json'");
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("conf/kaptchaConfig.json")) {
            if (is == null) {
                log.error("File conf/kaptchaConfig.json is not exist, please check code.");
                System.exit(1);
            }

            // cache info
            String jsonStr = IOUtils.toString(is, StandardCharset.UTF_8);
            JSONArray configs = JSON.parseObject(jsonStr, JSONArray.class);
            Constants.CacheKey captchaConfig = Constants.CacheKey.captchaConfig;
            String keyPrefix = captchaConfig.cacheKey();
            for (Object config : configs) {
                JSONObject jsonConfig = JSON.parseObject(JSON.toJSONString(config));
                String name = jsonConfig.getString("name");
                String properties = jsonConfig.getString("properties");
                log.info("Captcha Config Info: {} - {}", name, properties);
                String key = keyPrefix.concat(":").concat(name);
                CacheUtils.put(key, properties, captchaConfig.duration());
            }
        } catch (IOException e) {
            log.error("Parse 'conf/kaptchaConfig.json' Failed", e);
        }

        log.info("Init Captcha End <===");
    }
}
