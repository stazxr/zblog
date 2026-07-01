package com.github.stazxr.zblog.audit.provider;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 基于 houbb sensitive-word 的 SensitiveProvider 实现
 *
 * @author SunTao
 * @since 2026-07-01
 */
@Component
public class HoubbSensitiveProvider implements SensitiveProvider {
    private static final Logger log = LoggerFactory.getLogger(HoubbSensitiveProvider.class);

    /**
     * 核心引擎
     */
    private final AtomicReference<SensitiveWordBs> engineRef = new AtomicReference<>();

    /**
     * 初始化词库
     */
    @PostConstruct
    public void init() {
        reload();
    }

    /**
     * 构建敏感词引擎
     */
    private SensitiveWordBs build() {
        return SensitiveWordBs.newInstance()
            .ignoreCase(true)
            .ignoreWidth(true)
            .ignoreNumStyle(true)
            .ignoreChineseStyle(true)
            .enableNumCheck(false)
            .enableEmailCheck(false)
            .enableUrlCheck(false)
            .init();
    }

    @Override
    public boolean contains(String text) {
        return engine().contains(text);
    }

    @Override
    public List<String> findAll(String text) {
        return engine().findAll(text);
    }

    @Override
    public String replace(String text) {
        return engine().replace(text);
    }

    /**
     * =========================
     * 🔥 热更新核心方法
     * =========================
     */
    @Override
    public synchronized void reload() {
        log.info("敏感词词库开始热更新...");
        engineRef.set(build());
        log.info("敏感词词库热更新完成");
    }



    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void refresh() {
        reload();
    }

    private SensitiveWordBs engine() {
        return engineRef.get();
    }
}
