package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 接口信息
 *
 * @author SunTao
 * @since 2025-11-03
 */
@Getter
@Setter
public class InterfaceVo extends BaseVo {
    private static final long serialVersionUID = -6011107531417391695L;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口编码
     */
    private String interfaceCode;

    /**
     * 请求地址
     */
    private String interfaceUri;

    /**
     * 请求方式
     */
    private String interfaceMethod;

    /**
     * 接口级别
     */
    private Integer interfaceLevel;

    /**
     * 接口状态
     */
    private Integer interfaceStatus;

    /**
     * 当天调用次数
     */
    private Integer dailyCallCount;

    /**
     * 累计调用次数
     */
    private Integer totalCallCount;

    /**
     * 失败请求次数
     */
    private Integer totalFailureCount;

    /**
     * 调用成功率
     */
    private Double callSuccessRate;

    /**
     * 平均响应时间（单位：毫秒）
     */
    private Double avgResponseTime;

    /**
     * 最大响应时间（单位：毫秒）
     */
    private Double maxResponseTime;

    /**
     * P95 响应时间（95% 请求在此时间内完成）
     */
    private Double p95ResponseTime;

    /**
     * P99 响应时间（99% 请求在此时间内完成）
     */
    private Double p99ResponseTime;

    /**
     * 峰值 QPS（每秒最大请求数）
     */
    private Double maxQps;

    /**
     * 平均 QPS（每秒平均请求数）
     */
    private Double avgQps;
}
