package com.github.stazxr.easycloud.id.core.service;

/**
 * 序号生成服务
 *
 * @author SunTao
 * @since 2024-04-03
 */
public interface IdGeneratorService {
    /**
     * 生成一个序号
     *
     * @return 序号
     */
    Long generateId();
}
