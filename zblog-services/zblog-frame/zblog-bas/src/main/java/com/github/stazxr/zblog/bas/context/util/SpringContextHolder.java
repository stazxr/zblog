package com.github.stazxr.zblog.bas.context.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Spring Context 工具类。
 *
 * <p>
 * 提供全局访问 Spring ApplicationContext、获取 Bean 和 Environment 属性的方法。
 * 这是一个纯静态工具类，设计为线程安全，并在访问前做初始化检查。
 * </p>
 *
 * <p>
 * 注意：调用此工具类方法前，必须通过 SpringContextConfig 设置 ApplicationContext。
 * </p>
 *
 * @author SunTao
 * @since 2024-04-30
 */
public final class SpringContextHolder {
    private static final Logger log = LoggerFactory.getLogger(SpringContextHolder.class);

    /**
     * Spring IoC ApplicationContext
     */
    private static volatile ApplicationContext applicationContext;

    /**
     * 私有构造函数，防止实例化
     */
    private SpringContextHolder() {
    }

    /**
     * 设置 Spring ApplicationContext。
     * <p>
     * 仅允许设置一次，如果重复设置，将忽略。
     * 使用双重检查锁保证线程安全。
     * </p>
     *
     * @param context Spring 应用上下文
     */
    public static void setApplicationContext(ApplicationContext context) {
        if (SpringContextHolder.applicationContext != null) {
            return; // 已经初始化，直接返回
        }
        synchronized (SpringContextHolder.class) {
            if (SpringContextHolder.applicationContext == null) {
                SpringContextHolder.applicationContext = context;
                log.info("Spring ApplicationContext 初始化完成: {}", context);
            }
        }
    }

    /**
     * 获取 Spring ApplicationContext。
     *
     * @return ApplicationContext 实例
     * @throws IllegalStateException 如果 ApplicationContext 未初始化
     */
    public static ApplicationContext getApplicationContext() {
        ApplicationContext ctx = applicationContext;
        if (ctx == null) {
            throw new IllegalStateException("Spring ApplicationContext 尚未初始化，请确保 SpringContextConfig 已正确配置");
        }
        return ctx;
    }

    /**
     * 根据类型获取 Bean。
     *
     * @param clazz Bean 类型
     * @param <T>   Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 根据名称和类型获取 Bean。
     *
     * @param name  Bean 名称
     * @param clazz Bean 类型
     * @param <T>   Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 根据名称获取 Bean。
     *
     * @param name Bean 名称
     * @return Bean 实例
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 获取 Spring Environment。
     *
     * @return Environment 实例
     */
    public static Environment getEnvironment() {
        return getApplicationContext().getEnvironment();
    }

    /**
     * 获取 Environment 中的属性值。
     *
     * @param key 属性名
     * @return 属性值，如果不存在返回 null
     */
    public static String getProperty(String key) {
        return getEnvironment().getProperty(key);
    }

    /**
     * 获取 Environment 中的属性值，如果不存在返回默认值。
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public static String getProperty(String key, String defaultValue) {
        return getEnvironment().getProperty(key, defaultValue);
    }

    /**
     * 获取 Environment 中的属性值，并转换为指定类型。
     *
     * @param key   属性名
     * @param clazz 目标类型
     * @param <T>   目标类型
     * @return 转换后的属性值，如果不存在返回 null
     */
    public static <T> T getProperty(String key, Class<T> clazz) {
        return getEnvironment().getProperty(key, clazz);
    }

    /**
     * 获取 Environment 中的属性值，并转换为指定类型，如果不存在返回默认值。
     *
     * @param key          属性名
     * @param clazz        目标类型
     * @param defaultValue 默认值
     * @param <T>          目标类型
     * @return 转换后的属性值或默认值
     */
    public static <T> T getProperty(String key, Class<T> clazz, T defaultValue) {
        return getEnvironment().getProperty(key, clazz, defaultValue);
    }
}
