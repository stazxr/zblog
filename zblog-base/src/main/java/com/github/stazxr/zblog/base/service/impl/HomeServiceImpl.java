package com.github.stazxr.zblog.base.service.impl;

import com.github.stazxr.zblog.base.domain.bo.DateCount;
import com.github.stazxr.zblog.base.domain.bo.NameValue;
import com.github.stazxr.zblog.base.domain.vo.HomePanelDataCountVo;
import com.github.stazxr.zblog.base.domain.vo.echarts.SingleLineChartDataVo;
import com.github.stazxr.zblog.base.mapper.ZblogMapper;
import com.github.stazxr.zblog.base.service.HomeService;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 首页面板管理服务实现层
 *
 * @author SunTao
 * @since 2022-07-19
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HomeServiceImpl implements HomeService {
    private final ZblogMapper zblogMapper;

    /**
     * 获取通知面板的统计信息
     *
     * @return HomePanelDataCountVo
     */
    @Override
    public HomePanelDataCountVo getHomePanelDataCount() {
        return zblogMapper.queryHomePanelDataCount();
    }

    /**
     * 根据图标名称获取对应的折现图标数据
     *
     * @param type 图表类型
     * @return SingleLineChartDataVo
     */
    @Override
    public SingleLineChartDataVo getHomePanelDetailDataByType(String type) {
        String[] xAxisData = DateUtils.getDateAry(7, true, false);

        List<DateCount> dateCounts;
        switch (type) {
            case "pv":
                dateCounts = zblogMapper.queryPvRangeData(xAxisData);
                break;
            case "uv":
                dateCounts = zblogMapper.queryUvRangeData(xAxisData);
                break;
            case "uu":
                dateCounts = zblogMapper.queryUuRangeData(xAxisData);
                break;
            case "av":
                dateCounts = zblogMapper.queryAvRangeData(xAxisData);
                break;
            default:
                throw new ServiceException("不支持的类型：" + type);
        }

        SingleLineChartDataVo singleLineChartDataVo = new SingleLineChartDataVo();
        singleLineChartDataVo.setXAxisData(xAxisData);
        singleLineChartDataVo.setLegendName("趋势图");
        singleLineChartDataVo.setLegendData(parseDateCounts(dateCounts, xAxisData));
        return singleLineChartDataVo;
    }

    /**
     * 获取首页面板的访客地域数据
     *
     * @return NameValue
     */
    @Override
    public List<NameValue> getHomePanelVisitorAreaCount() {
        return zblogMapper.queryVisitorAreaCount();
    }

    private List<Integer> parseDateCounts(List<DateCount> dateCounts, String[] dates) {
        Map<String, Integer> dataMap = dateCounts.stream().collect(Collectors.toMap(DateCount::getDate, DateCount::getCount));
        List<Integer> result = new ArrayList<>();
        for (String date : dates) {
            result.add(dataMap.getOrDefault(date, 0));
        }

        return result;
    }
}
