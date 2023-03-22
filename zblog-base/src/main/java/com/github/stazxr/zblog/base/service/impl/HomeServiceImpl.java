package com.github.stazxr.zblog.base.service.impl;

import com.github.stazxr.zblog.base.domain.vo.HomePanelDataCountVo;
import com.github.stazxr.zblog.base.domain.vo.echarts.SingleLineChartDataVo;
import com.github.stazxr.zblog.base.service.HomeService;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 首页面板管理服务实现层
 *
 * @author SunTao
 * @since 2022-07-19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HomeServiceImpl implements HomeService {
    /**
     * 获取通知面板的统计信息
     *
     * @return HomePanelDataCountVo
     */
    @Override
    public HomePanelDataCountVo getHomePanelDataCount() {
        // TODO 待实现
        return new HomePanelDataCountVo();
    }

    /**
     * 根据图标名称获取对应的折现图标数据
     *
     * @param type 图表类型
     * @return SingleLineChartDataVo
     */
    @Override
    public SingleLineChartDataVo getHomePanelDetailDataByType(String type) {
        // TODO 待实现
        String[] xAxisData = DateUtils.getDateAry(7, true, false);

        int[] legendDataAry = new int[xAxisData.length];
        switch (type) {
            case "visits":
            case "messages":
            case "articles":
            case "warnings":
                break;
            default:
                throw new ServiceException("不支持的类型：" + type);
        }

        SingleLineChartDataVo singleLineChartDataVo = new SingleLineChartDataVo();
        singleLineChartDataVo.setXAxisData(xAxisData);
        singleLineChartDataVo.setLegendName("趋势图");
        singleLineChartDataVo.setLegendData(legendDataAry);
        return singleLineChartDataVo;
    }
}
