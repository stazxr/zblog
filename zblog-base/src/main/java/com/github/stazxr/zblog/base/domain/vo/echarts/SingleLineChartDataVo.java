package com.github.stazxr.zblog.base.domain.vo.echarts;

import lombok.Data;
import lombok.ToString;

/**
 * 单折线图
 *
 * @author SunTao
 * @since 2022-07-19
 */
@Data
@ToString
public class SingleLineChartDataVo {
    /**
     * X轴下标
     */
    private String[] xAxisData;

    /**
     * 折现名称
     */
    private String legendName;

    /**
     * 折现类型，默认是折现图
     */
    private String legendType = "line";

    /**
     * Y轴数据
     */
    private int[] legendData;
}
