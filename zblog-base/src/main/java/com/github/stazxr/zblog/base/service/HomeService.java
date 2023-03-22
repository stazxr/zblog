package com.github.stazxr.zblog.base.service;

import com.github.stazxr.zblog.base.domain.vo.HomePanelDataCountVo;
import com.github.stazxr.zblog.base.domain.vo.echarts.SingleLineChartDataVo;

/**
 * 首页面板管理服务层
 *
 * @author SunTao
 * @since 2022-07-19
 */
public interface HomeService {
    /**
     * 获取通知面板的统计信息
     *
     * @return HomePanelDataCountVo
     */
    HomePanelDataCountVo getHomePanelDataCount();

    /**
     * 根据图标名称获取对应的折现图标数据
     *
     * @param type 图表类型
     * @return SingleLineChartDataVo
     */
    SingleLineChartDataVo getHomePanelDetailDataByType(String type);
}
