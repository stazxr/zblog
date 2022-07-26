package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.CalendarDeleteDto;
import com.github.stazxr.zblog.base.domain.dto.CalendarImportDto;
import com.github.stazxr.zblog.base.domain.dto.CalendarInsertDto;
import com.github.stazxr.zblog.base.domain.enums.HolidayType;
import com.github.stazxr.zblog.base.domain.vo.CalendarVo;
import com.github.stazxr.zblog.base.service.CalendarService;
import com.github.stazxr.zblog.core.annotation.IgnoreResult;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 日历管理
 *
 * @author SunTao
 * @since 2022-04-06
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    /**
     * 获取节假日或工作日列表
     *
     * @param year 查看的年份
     * @return calendarList
     */
    @GetMapping("/listCalendarSet")
    @Router(name = "获取节假日或工作日列表", code = "listCalendarSet", level = BaseConst.PermLevel.OPEN)
    public Result listCalendarSet(@RequestParam String year) {
        List<CalendarVo> calendarList = calendarService.listCalendarSet(year);
        return Result.success().data(calendarList);
    }

    /**
     * 设置节假日或工作日
     *
     * @param insertDto 节假日或工作日信息
     * @return Result
     */
    @PostMapping("/calendarSet")
    @Router(name = "设置节假日或工作日", code = "calendarSet", level = BaseConst.PermLevel.OPEN)
    public Result calendarSet(@RequestBody CalendarInsertDto insertDto) {
        String type = insertDto.getType();
        if (StringUtils.hasEmpty(insertDto.getDate(), type)) {
            return Result.failure(ResultCode.PARAM_EMPTY);
        }

        if (!HolidayType.HOLIDAY.getType().equals(type) && !HolidayType.WORKDAY.getType().equals(type)) {
            return Result.failure(ResultCode.PARAM_VALID, "日期类型错误，有效范围['0', '1']");
        }

        calendarService.setHolidayOrWorkday(insertDto);
        return Result.success();
    }

    /**
     * 取消节假日或工作日设置
     *
     * @param deleteDto 参数
     * @return Result
     */
    @PostMapping("/cancelCalendarSet")
    @Router(name = "取消节假日或工作日设置", code = "cancelCalendarSet", level = BaseConst.PermLevel.OPEN)
    public Result cancelCalendarSet(@RequestBody CalendarDeleteDto deleteDto) {
        calendarService.cancelHolidayOrWorkday(deleteDto);
        return Result.success();
    }

    /**
     * 导出节假日或工作日设置
     *
     * @param response 响应
     */
    @IgnoreResult
    @PostMapping("/exportCalendarSet")
    @Router(name = "导出节假日或工作日设置", code = "exportCalendarSet", level = BaseConst.PermLevel.OPEN)
    public void exportCalendarSet(HttpServletResponse response) {
        String year = DateUtils.formatNow("yyyy");
        calendarService.exportCalendarSet(year, response);
    }

    /**
     * 导入节假日或工作日设置
     *
     * @param importDto 导入参数
     * @return ResultVO
     */
    @IgnoreResult
    @PostMapping("/importCalendarSet")
    @Router(name = "导入节假日或工作日设置", code = "importCalendarSet", level = BaseConst.PermLevel.OPEN)
    public Result importCalendarSet(@RequestBody CalendarImportDto importDto) {
        if (StringUtils.hasBlank(importDto.getUrl(), importDto.getFileId())) {
            return Result.failure(ResultCode.PARAM_EMPTY);
        }

        calendarService.importCalendarSet(importDto);
        return Result.success();
    }
}
