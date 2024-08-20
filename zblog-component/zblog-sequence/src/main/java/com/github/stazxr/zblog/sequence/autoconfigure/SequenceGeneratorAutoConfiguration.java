package com.github.stazxr.zblog.sequence.autoconfigure;

import com.github.stazxr.zblog.sequence.SequenceGenerator;
import com.github.stazxr.zblog.sequence.core.id.IdGenerator;
import com.github.stazxr.zblog.sequence.core.id.IdGeneratorImpl;
import com.github.stazxr.zblog.sequence.core.snowflake.SnowflakeSequenceGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 序号生成器自动配置
 *
 * @author SunTao
 * @since 2024-04-07
 */
@Configuration
@EnableConfigurationProperties({SequenceGeneratorProperties.class})
public class SequenceGeneratorAutoConfiguration {
    /**
     * 序号生成器
     *
     * @param properties 配置信息
     * @return IdGenerator
     */
    @Bean
    public SequenceGenerator sequenceGenerator(SequenceGeneratorProperties properties) {
        return new SnowflakeSequenceGenerator(properties.getDatacenterId(), properties.getMachineId());
    }

    /**
     * ID 生成器
     *
     * @param sequenceGenerator 序号生成器
     * @return IdGenerator
     */
    @Bean(name = "idGeneratorService")
    public IdGenerator idGeneratorService(SequenceGenerator sequenceGenerator) {
        IdGeneratorImpl idGenerator = new IdGeneratorImpl();
        idGenerator.setSequenceGenerator(sequenceGenerator);
        return idGenerator;
    }
}
