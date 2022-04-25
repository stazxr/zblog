package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.dto.CalendarDeleteDto;
import com.github.stazxr.zblog.base.domain.dto.CalendarImportDto;
import com.github.stazxr.zblog.base.domain.dto.CalendarInsertDto;
import com.github.stazxr.zblog.base.domain.entity.Calendar;
import com.github.stazxr.zblog.base.domain.vo.CalendarVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 日历服务层
 *
 * @author SunTao
 * @since 2022-04-06
 */
public interface CalendarService extends IService<Calendar> {
    /**
     * 获取节假日或工作日列表
     *
     * @param year 查看的年份
     * @return calendarList
     */
    List<CalendarVo> listCalendarSet(String year);

    /**
     * 设置节假日或工作日
     *
     * @param insertDto 工作日信息
     */
    void setHolidayOrWorkday(CalendarInsertDto insertDto);

    /**
     * 取消节假日或工作日
     *
     * @param deleteDto 删除参数
     */
    void cancelHolidayOrWorkday(CalendarDeleteDto deleteDto);

    /**
     * 导出节假日或工作日设置
     *
     * @param year 导出年份
     * @param response 响应
     */
    void exportCalendarSet(String year, HttpServletResponse response);

    /**
     * 导入节假日或工作日设置
     *
     * @param importDto 导入参数
     */
    void importCalendarSet(CalendarImportDto importDto);
}
