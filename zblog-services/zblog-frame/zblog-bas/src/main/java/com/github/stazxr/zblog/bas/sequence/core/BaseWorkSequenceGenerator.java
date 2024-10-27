package com.github.stazxr.zblog.bas.sequence.core;

import com.github.stazxr.zblog.bas.sequence.SequenceGenerator;

/**
 * 基于数据中心和部署机器的序号生成器扩展
 *
 * @author SunTao
 * @since 2021-12-12
 */
public abstract class BaseWorkSequenceGenerator implements SequenceGenerator {
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
