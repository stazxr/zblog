package com.github.stazxr.easycloud.starter.easyid.autoconfigure;

import com.github.stazxr.easycloud.id.IdGenerator;
import com.github.stazxr.easycloud.id.core.service.EasyIdDefaultServiceImpl;
import com.github.stazxr.easycloud.id.core.service.IdGeneratorService;
import com.github.stazxr.easycloud.id.core.snowflake.SnowflakeIdGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * EasyId 自动配置
 *
 * @author SunTao
 * @since 2024-04-07
 */
@Configuration
@EnableConfigurationProperties({EasyIdProperties.class})
public class EasyIdAutoConfiguration {
    /**
     * ID 生成器
     *
     * @param properties 配置信息
     * @return IdGenerator
     */
    @Bean
    public IdGenerator idGenerator(EasyIdProperties properties) {
        return new SnowflakeIdGenerator(properties.getDatacenterId(), properties.getMachineId());
    }

    /**
     * ID 生成服务
     *
     * @param idGenerator Id 生成器
     * @return IdGeneratorService
     */
    @Bean(name = "EasyIdGeneratorService")
    public IdGeneratorService idGeneratorService(IdGenerator idGenerator) {
        EasyIdDefaultServiceImpl idGenService = new EasyIdDefaultServiceImpl();
        idGenService.setIdGenerator(idGenerator);
        return idGenService;
    }
}
