package com.github.stazxr.zblog.log.service.impl;

import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.service.LogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步日志服务
 *
 * @author SunTao
 * @since 2026-02-04
 */
@Service
public class AsyncLogService {
    private final LogService logService;

    public AsyncLogService(LogService logService) {
        this.logService = logService;
    }

    @Async
    public void saveAsync(Log log) {
        logService.save(log);
    }
}
