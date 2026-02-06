package com.github.stazxr.zblog.bas.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.Comparator;
import java.util.Map;

/**
 * 启动后打印所有 Filter 执行顺序，方便排查过滤器链问题
 *
 * @author SunTao
 * @since 2026-02-04
 */
@Component
public class FilterOrderPrinter implements SmartLifecycle {
    private static final Logger log = LoggerFactory.getLogger(FilterOrderPrinter.class);

    private final ApplicationContext context;

    private boolean running = false;

    public FilterOrderPrinter(ApplicationContext context) {
        this.context = context;
    }

    @Override
    @SuppressWarnings("all")
    public void start() {
        try {
            Map<String, FilterRegistrationBean> beans = context.getBeansOfType(FilterRegistrationBean.class);
            log.info("========== Filter Execution Order ==========");
            beans.values().stream()
                .sorted(Comparator.comparingInt(FilterRegistrationBean::getOrder))
                .forEach(bean -> {
                    Filter filter = bean.getFilter();
                    int order = bean.getOrder();
                    log.info("order={} -> {}", order, filter == null ? "unknown" : filter.getClass().getName());
                });
            log.info("=============================================");
        } catch (Exception e) {
            log.error("打印 Filter 顺序失败", e);
        }

        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * 确保 Spring 完全启动后执行
     */
    @Override
    public int getPhase() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
