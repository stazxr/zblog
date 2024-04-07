package com.github.stazxr.easycloud.starter.easyid.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * EasyId 配置项
 *
 * @author SunTao
 * @since 2024-04-07
 */
@ConfigurationProperties(prefix= EasyIdProperties.EASY_ID_PREFIX)
public class EasyIdProperties {
    static final String EASY_ID_PREFIX= "easycloud.idgen";

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
