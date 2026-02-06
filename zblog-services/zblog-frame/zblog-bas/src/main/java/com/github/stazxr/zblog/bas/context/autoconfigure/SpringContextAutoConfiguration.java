package com.github.stazxr.zblog.bas.context.autoconfigure;

import com.github.stazxr.zblog.bas.context.util.SpringContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

/**
 * Spring Context 配置类。
 *
 * <p>
 * 主要作用：
 * 1. 将 Spring ApplicationContext 设置到 SpringContextUtil 工具类
 * 2. 提供全局访问 Spring Bean 与 Environment 的能力
 * </p>
 *
 * @author SunTao
 * @since 2024-04-30
 */
@Configuration
public class SpringContextAutoConfiguration implements ApplicationContextAware {
    /**
     * 设置 ApplicationContext，并初始化 SpringContextUtil。
     *
     * @param applicationContext Spring 应用上下文
     * @throws BeansException 如果设置失败
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.setApplicationContext(applicationContext);
    }
}