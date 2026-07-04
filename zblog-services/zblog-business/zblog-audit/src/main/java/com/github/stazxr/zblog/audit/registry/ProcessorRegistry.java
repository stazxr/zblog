package com.github.stazxr.zblog.audit.registry;

import com.github.stazxr.zblog.audit.processor.AuditProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Processor注册中心
 *
 * <p>负责统一管理 Spring Bean + SPI Processor</p>
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
@Component
public class ProcessorRegistry {
    private static final Logger log = LoggerFactory.getLogger(ProcessorRegistry.class);

    /**
     * processor注册表
     */
    private final Map<String, AuditProcessor> registryProcessors = new HashMap<>();

    public ProcessorRegistry(List<AuditProcessor> springProcessors) {
        // 1. 注册 Spring Bean
        for (AuditProcessor processor : springProcessors) {
            put(processor);
        }

        // 2. 注册 SPI
        ServiceLoader<AuditProcessor> loader = ServiceLoader.load(AuditProcessor.class);
        for (AuditProcessor processor : loader) {
            put(processor);
        }
    }

    /**
     * 获取 processor
     */
    public AuditProcessor get(String name) {
        return registryProcessors.get(name);
    }

    /**
     * 获取全部 processor
     */
    public Map<String, AuditProcessor> getAll() {
        return Collections.unmodifiableMap(registryProcessors);
    }

    /**
     * 统一注册逻辑（关键）
     */
    private void put(AuditProcessor processor) {
        String name = processor.name();
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("Processor name cannot be empty");
        }

        if (registryProcessors.containsKey(name)) {
            throw new IllegalStateException("Duplicate processor name: " + name);
        }
        registryProcessors.put(name, processor);
    }

    @PostConstruct
    public void printAuditProcessors() {
        Map<String, AuditProcessor> auditProcessorMap = getAll();
        if (auditProcessorMap == null || auditProcessorMap.isEmpty()) {
            log.warn("\n================ Audit Processor Registry ================\n" +
                    "No audit processors registered!\n" +
                    "===========================================================");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n================ Audit Processor Registry ================\n");

        int[] indexAry = {1};
        auditProcessorMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> sb.append(
            String.format("%-3d %-20s -> %s\n", indexAry[0]++, entry.getKey(), entry.getValue().getClass().getName())
        ));

        sb.append("----------------------------------------------------------\n");
        sb.append("Total: ").append(auditProcessorMap.size()).append(" processors registered\n");
        sb.append("===========================================================");
        log.info(sb.toString());
    }
}
