package com.github.stazxr.zblog.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis Plus Config
 *
 * @author SunTao
 * @since 2020-12-09
 */
@Configuration(proxyBeanMethods = false)
@MapperScan(value = {"com.github.stazxr.zblog.*.mapper"})
public class MyBatisPlusConfig {
    /**
     * MybatisPlusInterceptor config
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 设置分页拦截器拦截配置
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setDbType(DbType.MYSQL);
        paginationInterceptor.setMaxLimit(-1L);
        paginationInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(paginationInterceptor);

        // 乐观锁配置
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // 阻止恶意的全表更新删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        return interceptor;
    }
}
