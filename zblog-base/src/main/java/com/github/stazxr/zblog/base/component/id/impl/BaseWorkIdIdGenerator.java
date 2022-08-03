package com.github.stazxr.zblog.base.component.id.impl;

import com.github.stazxr.zblog.base.component.id.model.WorkId;
import com.github.stazxr.zblog.base.component.id.IdGenerator;

/**
 * 基于机器ID的全局唯一ID生成器
 *
 * @author SunTao
 * @since 2021-12-12
 */
public abstract class BaseWorkIdIdGenerator implements IdGenerator {
    /**
     * 获取机器ID
     *
     * @return Long workId
     */
    protected abstract Long getWorkId();

    /**
     * 根据机器ID获取机器IP
     *
     * @param workId 机器Id
     * @return WorkIp
     */
    protected abstract String parseWorkerIp(Long workId);

    /**
     * 反解析ID内容
     *
     * @param id 唯一ID
     * @return ID
     */
    protected abstract WorkId parseId(Long id);
}
