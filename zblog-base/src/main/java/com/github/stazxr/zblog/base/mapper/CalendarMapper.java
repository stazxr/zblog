package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.Calendar;
import com.github.stazxr.zblog.base.domain.vo.CalendarVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 日历数据持久层
 *
 * @author SunTao
 * @since 2022-04-06
 */
public interface CalendarMapper extends BaseMapper<Calendar> {
    /**
     * 删除节假日/工作日配置
     *
     * @param date 日期
     */
    void deleteDataConfig(@Param("date") String date);

    /**
     * 获取节假日或工作日列表
     *
     * @param year 查看的年份
     * @return calendarList
     */
    List<CalendarVo> listCalendarSet(@Param("year") String year);
}
