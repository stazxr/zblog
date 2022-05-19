package com.github.stazxr.zblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 前后端分离个人博客开源项目 - zblog.
 *  基于单体的 3.1 版本进行改造，单体链接：https://github.com/stazxr/Blog
 *
 * @author SunTao
 * @since 2020-11-14
 * @version 4.0.0
 */
@EnableCaching
@SpringBootApplication
public class ZblogApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZblogApplication.class, args);
    }
}
