package com.github.stazxr.zblog.bas.context.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Spring context utility class.
 *
 * This class provides utility methods for accessing beans and environment properties
 * from the Spring ApplicationContext.
 *
 * @author SunTao
 * @since 2024-04-30
 */
@Slf4j
public class SpringContextUtil {
    /**
     * Spring IoC ApplicationContext.
     */
    private static ApplicationContext applicationContext;

    /**
     * Set the ApplicationContext.
     *
     * @param applicationContext The application context to set
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
            log.info("Set ApplicationContext: {}", applicationContext);
        }
    }

    /**
     * Get a bean by name.
     *
     * @param name The name of the bean
     * @return The bean instance
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * Get a bean by type.
     *
     * @param clazz The type of the bean
     * @param <T>   The type of the bean
     * @return The bean instance
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * Get a bean by name and type.
     *
     * @param name  The name of the bean
     * @param clazz The type of the bean
     * @param <T>   The type of the bean
     * @return The bean instance
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * Get the environment of the ApplicationContext.
     *
     * @return The environment object
     */
    public static Environment getEnvironment() {
        return getApplicationContext().getEnvironment();
    }

    /**
     * Get a property from the environment by key.
     *
     * @param key The key of the property
     * @return The property value
     */
    public static String getProperty(String key) {
        return getEnvironment().getProperty(key);
    }

    /**
     * Get a property from the environment by key, with a default value if the property is not found.
     *
     * @param key          The key of the property
     * @param defaultValue The default value if the property is not found
     * @return The property value
     */
    public static String getProperty(String key, String defaultValue) {
        return getEnvironment().getProperty(key, defaultValue);
    }

    /**
     * Get a property from the environment by key and convert it to a specified type.
     *
     * @param key   The key of the property
     * @param clazz The type to convert the property value to
     * @param <T>   The type of the property value
     * @return The property value converted to the specified type
     */
    public static <T> T getProperty(String key, Class<T> clazz) {
        return getEnvironment().getProperty(key, clazz);
    }

    /**
     * Get a property from the environment by key, convert it to a specified type, with a default value if not found.
     *
     * @param key          The key of the property
     * @param clazz        The type to convert the property value to
     * @param defaultValue The default value if the property is not found
     * @param <T>          The type of the property value
     * @return The property value converted to the specified type, or the default value if not found
     */
    public static <T> T getProperty(String key, Class<T> clazz, T defaultValue) {
        return getEnvironment().getProperty(key, clazz, defaultValue);
    }

    /**
     * Get the ApplicationContext object.
     *
     * @return The ApplicationContext instance
     */
    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
