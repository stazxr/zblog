package com.github.stazxr.zblog.bas.i18n.source;

import com.github.stazxr.zblog.bas.i18n.I18nProperties;
import com.github.stazxr.zblog.util.thread.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.support.AbstractMessageSource;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * 基于数据库的 MessageSource。
 *
 * @author SunTao
 * @since 2025-08-10
 */
@Slf4j
public class DatabaseMessageSource extends AbstractMessageSource implements Runnable {
    private final Map<String, Properties> messageCache = new ConcurrentHashMap<>();

    private final DataSource dataSource;

    private final I18nProperties i18nProperties;

    private volatile boolean running = true;

    private Thread refreshThread;

    public DatabaseMessageSource(DataSource dataSource, I18nProperties i18nProperties) {
        this.dataSource = dataSource;
        this.i18nProperties = i18nProperties;
        startBackgroundRefresh();
    }

    @SuppressWarnings("all")
    private void startBackgroundRefresh() {
        refreshThread = new Thread(this, "DatabaseMessageSource-Refresh");
        refreshThread.setDaemon(true);
        refreshThread.start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                loadMessages();
                ThreadUtils.sleepMillisecond(i18nProperties.getCacheTtl());
            } catch (Exception e) {
                log.error("[i18n] 刷新国际化缓存失败", e);
            }
        }
    }

    private void loadMessages() {
        Map<String, Properties> newMessageCache = new HashMap<>(2);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(i18nProperties.getLoadSql());
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String code = rs.getString(i18nProperties.getCodeColumn());
                String locale = rs.getString(i18nProperties.getLocaleColumn());
                String message = rs.getString(i18nProperties.getMessageColumn());

                // 数据健壮性检查
                if (code == null || locale == null || message == null) {
                    log.warn("[i18n] 检测到无效国际化记录，已跳过: code={}, locale={}, message={}", code, locale, message);
                    return;
                }

                Properties props = newMessageCache.computeIfAbsent(locale, k -> new Properties());
                props.setProperty(code, message);
            }
            messageCache.clear();
            messageCache.putAll(newMessageCache);

            if (log.isDebugEnabled()) {
                for (String locale : messageCache.keySet()) {
                    log.debug("[i18n] 国际化缓存刷新完成，语言种类：{}，总条数：{}", locale, messageCache.get(locale).size());
                }
            }
        } catch (Exception e) {
            log.error("[i18n] 国际化缓存刷新失败", e);
        }
    }

    @Nullable
    @Override
    protected MessageFormat resolveCode(@NonNull String code, @NonNull Locale locale) {
        if (!messageCache.containsKey(locale.toString())) {
            return null;
        }

        String message = messageCache.get(locale.toString()).getProperty(code);
        return message != null ? new MessageFormat(message, locale) : null;
    }

    @PreDestroy
    public void shutdown() {
        running = false;
        if (refreshThread != null) {
            refreshThread.interrupt();
        }
        log.info("[i18n] DatabaseMessageSource 已停止刷新线程");
    }
}

