package com.github.stazxr.zblog.audit.provider;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.stazxr.zblog.audit.provider.source.SensitiveWordSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
     * Houbb 默认引擎（内置词库）
     */
    private final SensitiveWordBs defaultEngine;

    /**
     * 自定义词库引擎（扩展词库）
     */
    private final AtomicReference<SensitiveWordBs> customEngineRef = new AtomicReference<>();

    private final List<SensitiveWordSource> sources;

    public HoubbSensitiveProvider(List<SensitiveWordSource> sources) {
        this.sources = sources;

        // 1. 默认词库引擎（不传 wordDeny = 默认词库）
        this.defaultEngine = SensitiveWordBs.newInstance()
            .ignoreCase(true)
            .ignoreWidth(true)
            .ignoreNumStyle(true)
            .ignoreChineseStyle(true)
            .enableNumCheck(false)
            .enableEmailCheck(false)
            .enableUrlCheck(false)
            .init();
    }

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
    private SensitiveWordBs buildCustomEngine() {
        // 加载词库
        List<String> words = new ArrayList<>();
        for (SensitiveWordSource source : sources) {
            List<String> list = source.load();
            int size = (list == null) ? 0 : list.size();
            log.info("[SensitiveWordSource] {} load words: {}", source.getClass().getSimpleName(), size);
            if (list != null && !list.isEmpty()) {
                words.addAll(list);
            }
        }

        return SensitiveWordBs.newInstance()
            .wordDeny(() -> new ArrayList<>(words)) // 扩展词库
            .ignoreCase(true) // 忽略大小写
            .ignoreWidth(true) // 忽略全角/半角
            .ignoreNumStyle(true) // 忽略数字变形
            .ignoreChineseStyle(true) // 中文变体归一化
            .enableNumCheck(false)
            .enableEmailCheck(false)
            .enableUrlCheck(false)
            .init();
    }

    @Override
    public boolean contains(String text) {
        return defaultEngine.contains(text) || customEngine().contains(text);
    }

    @Override
    public List<String> findAll(String text) {
        Set<String> result = new HashSet<>();
        result.addAll(defaultEngine.findAll(text));
        result.addAll(customEngine().findAll(text));
        return new ArrayList<>(result);
    }

    @Override
    public String replace(String text) {
        return customEngine().replace(defaultEngine.replace(text));
    }

    @Override
    public synchronized void reload() {
        log.info("自定义敏感词词库开始热更新...");
        customEngineRef.set(buildCustomEngine());
        log.info("自定义敏感词词库热更新完成");
    }

    @Scheduled(fixedDelay = 300_000)
    public void refresh() {
        reload();
    }

    private SensitiveWordBs customEngine() {
        return customEngineRef.get();
    }
}
