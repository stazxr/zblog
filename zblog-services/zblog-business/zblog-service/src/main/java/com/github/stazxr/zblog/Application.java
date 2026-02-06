package com.github.stazxr.zblog;

import com.github.stazxr.zblog.log.annotation.EnableLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 前后端分离个人博客开源项目 - zblog.
 *  本项目链接：<a href="https://github.com/stazxr/zblog">...</a>
 *  基于单体的 3.1 版本进行改造（已停止维护，需要的自行下载），单体链接：<a href="https://github.com/stazxr/Blog">...</a>
 *
 * @author SunTao
 * @since 2020-11-14
 * @version 5.0.0
 */
@EnableLog
@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
            log.info("Application start successfully...");
        } catch (Exception e) {
            log.error("Application start failed...");
        }

    }
}
