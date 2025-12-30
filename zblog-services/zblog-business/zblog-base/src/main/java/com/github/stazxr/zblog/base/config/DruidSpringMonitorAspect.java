package com.github.stazxr.zblog.base.config;

import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid Spring 方法级监控配置
 *
 * <p>
 * 功能说明：
 * <ul>
 *   <li>基于 Spring AOP 对 Service 层方法进行拦截</li>
 *   <li>采集方法调用次数、执行耗时等统计信息</li>
 *   <li>数据最终展示在 Druid 监控页面的「Spring监控」模块</li>
 * </ul>
 *
 * <p>
 * 监控范围：
 * <pre>
 *   com.github.stazxr.zblog..*.service..*
 * </pre>
 *
 * <p>
 * 使用前提：
 * <ul>
 *   <li>已引入 druid-spring-boot-starter</li>
 *   <li>已开启 Spring AOP（spring-boot-starter-aop）</li>
 *   <li>Druid 已作为项目数据源</li>
 * </ul>
 *
 * @author SunTao
 * @since 2025-12-31
 */
@Configuration
public class DruidSpringMonitorAspect {
    /**
     * Druid 提供的方法统计拦截器
     * <p>
     * 用于统计：
     * <ul>
     *   <li>方法调用次数</li>
     *   <li>方法执行时间</li>
     *   <li>方法关联的 JDBC 执行情况</li>
     * </ul>
     */
    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    /**
     * 方法拦截切点定义（正则表达式）
     * <p>
     * 说明：
     * <ul>
     *   <li>使用 Java 正则（非 Spring 包通配符）</li>
     *   <li>拦截所有 zblog 下的 service 包方法</li>
     * </ul>
     */
    @Bean
    public JdkRegexpMethodPointcut druidStatPointcut() {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPattern("com\\.github\\.stazxr\\.zblog\\..*\\.service\\..*");
        return pointcut;
    }

    /**
     * AOP 顾问配置
     * <p>
     * 将切点与拦截器进行绑定，
     * 由 Spring AOP 在运行期自动织入代理。
     */
    @Bean
    public DefaultPointcutAdvisor druidStatAdvisor() {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(druidStatPointcut());
        advisor.setAdvice(druidStatInterceptor());
        return advisor;
    }
}
