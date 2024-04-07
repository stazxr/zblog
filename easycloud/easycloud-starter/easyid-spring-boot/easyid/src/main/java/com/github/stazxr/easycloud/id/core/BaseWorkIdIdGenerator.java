package com.github.stazxr.easycloud.id.core;

import com.github.stazxr.easycloud.id.IdGenerator;

/**
 * 序号生成器扩展
 *
 * @author SunTao
 * @since 2021-12-12
 */
public abstract class BaseWorkIdIdGenerator implements IdGenerator {
    /**
     * 获取数据中心ID
     *
     * @return Long datacenterId
     */
    protected abstract Long getDatacenterId();

    /**
     * 获取机器ID
     *
     * @return Long machineId
     */
    protected abstract Long getMachineId();
}
