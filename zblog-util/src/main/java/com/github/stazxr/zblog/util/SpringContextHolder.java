package com.github.stazxr.zblog.util;

/**
 * 上下文工具，只声明方法，不进行实现，service模块对该类进行了覆写，
 *  供给非必须依赖Core模块的其他模块调用，保证其他模块可以正常使用Spring上下文获取Bean实例
 *
 * @author SunTao
 * @since 2022-05-31
 */
public class SpringContextHolder {
    /**
     * 根据名称获取 Bean
     *
     * @param name beanName
     * @return Object
     */
    public static Object getBean(String name) {
        return null;
    }

    /**
     * 通过类信息获取 Bean
     *
     * @param clazz class
     * @param <T> Type
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        return null;
    }

    /**
     * 通过名称和类信息获取 Bean
     *
     * @param name beanName
     * @param clazz class
     * @param <T> Type
     * @return T
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return null;
    }
}
