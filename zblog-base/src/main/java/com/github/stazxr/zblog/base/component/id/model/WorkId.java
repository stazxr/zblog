package com.github.stazxr.zblog.base.component.id.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 全局唯一id数据结构
 *
 * @author SunTao
 * @since 2021-12-12
 */
@Data
public class WorkId {
    /**
     * 生成id的时间戳
     */
    private Timestamp lockTime;

    /**
     * 机器id
     */
    private Long workId;

    /**
     * 机器ip地址
     */
    private String workIpAddr;

    /**
     * 生成的序列号
     */
    private Long sequence;
}
