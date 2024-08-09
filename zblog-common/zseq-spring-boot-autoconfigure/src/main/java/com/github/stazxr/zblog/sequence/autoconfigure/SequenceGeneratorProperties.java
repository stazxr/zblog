package com.github.stazxr.zblog.sequence.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 序号生成器配置项
 *
 * @author SunTao
 * @since 2024-04-07
 */
@ConfigurationProperties(prefix= SequenceGeneratorProperties.SEQ_PREFIX)
public class SequenceGeneratorProperties {
    static final String SEQ_PREFIX= "zblog.seq";

    /**
     * 数据中心ID
     */
    private short datacenterId;

    /**
     * 机器ID
     */
    private short machineId;

    public short getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(short datacenterId) {
        this.datacenterId = datacenterId;
    }

    public short getMachineId() {
        return machineId;
    }

    public void setMachineId(short machineId) {
        this.machineId = machineId;
    }
}
