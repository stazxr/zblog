package com.github.stazxr.zblog.base.component.id.work.model;

import lombok.Data;

import java.util.Map;

/**
 * workId缓存信息
 *
 * @author SunTao
 * @since 2021-12-16
 */
@Data
public class WorkIdCache {
    private Map<String, Long> workIdsCache;

    /**
     * default 0L
     */
    private long lastWorkId;
}
