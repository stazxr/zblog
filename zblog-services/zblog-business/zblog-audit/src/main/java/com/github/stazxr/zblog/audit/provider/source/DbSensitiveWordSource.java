package com.github.stazxr.zblog.audit.provider.source;

import com.github.stazxr.zblog.audit.mapper.SensitiveWordMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据库词库
 *
 * @author SunTao
 * @since 2026-07-05
 */
@Component
public class DbSensitiveWordSource implements SensitiveWordSource {
    private final SensitiveWordMapper sensitiveWordMapper;

    public DbSensitiveWordSource(SensitiveWordMapper sensitiveWordMapper) {
        this.sensitiveWordMapper = sensitiveWordMapper;
    }

    @Override
    public List<String> load() {
        return sensitiveWordMapper.selectAllWords();
    }
}
