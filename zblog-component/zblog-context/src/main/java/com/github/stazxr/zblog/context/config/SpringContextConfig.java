package com.github.stazxr.zblog.context.config;

import com.github.stazxr.zblog.context.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Spring context configuration.
 *
 * <p>
 * This configuration class implements {@link BeanFactoryPostProcessor} and {@link ApplicationContextAware}
 * interfaces to perform initialization operations during Spring container startup and set the Spring
 * ApplicationContext into {@link SpringContextUtil}.
 * </p>
 *
 * @since 2024-04-30
 * @author SunTao
 */
@Slf4j
@Configuration
public class SpringContextConfig implements BeanFactoryPostProcessor, ApplicationContextAware, Ordered {
    /**
     * Modify the application context's internal bean factory after its standard
     * initialization. All bean definitions will have been loaded, but no beans
     * will have been instantiated yet. This allows for overriding or adding
     * properties even to eager-initializing beans.
     *
     * @param beanFactory the bean factory used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    /**
     * Set the application context.
     *
     * @param applicationContext Spring application context
     * @throws BeansException if unable to set the application context
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.setApplicationContext(applicationContext);
    }

    /**
     * Get the order of this configuration class.
     *
     * @return The order of this configuration class, highest priority
     */
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
