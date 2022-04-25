package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.dto.CalendarDeleteDto;
import com.github.stazxr.zblog.base.domain.dto.CalendarImportDto;
import com.github.stazxr.zblog.base.domain.dto.CalendarInsertDto;
import com.github.stazxr.zblog.base.domain.entity.Calendar;
import com.github.stazxr.zblog.base.domain.enums.HolidayType;
import com.github.stazxr.zblog.base.domain.vo.CalendarVo;
import com.github.stazxr.zblog.base.mapper.CalendarMapper;
import com.github.stazxr.zblog.base.service.CalendarService;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.IdUtils;
import com.github.stazxr.zblog.util.file.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * 日历业务实现层
 *
 * @author SunTao
 * @since 2022-04-06
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CalendarServiceImpl extends ServiceImpl<CalendarMapper, Calendar> implements CalendarService {
    @Resource
    private CalendarMapper calendarMapper;

    /**
     * 获取节假日或工作日列表
     *
     * @param year 查看的年份
     * @return calendarList
     */
    @Override
    public List<CalendarVo> listCalendarSet(String year) {
        return calendarMapper.listCalendarSet(year);
    }

    /**
     * 设置节假日或工作日
     *
     * @param insertDto 工作日信息
     */
    @Override
    public void setHolidayOrWorkday(CalendarInsertDto insertDto) {
        // delete
        String date = insertDto.getDate();
        calendarMapper.deleteDataConfig(date);

        // insert
        Calendar calendar = new Calendar();
        calendar.setId(IdUtils.getId());
        calendar.setDate(insertDto.getDate());
        calendar.setFlag(insertDto.getType());
        calendar.setRemark(insertDto.getRemark());
        calendarMapper.insert(calendar);
    }

    /**
     * 取消节假日或工作日
     *
     * @param deleteDto 删除参数
     */
    @Override
    public void cancelHolidayOrWorkday(CalendarDeleteDto deleteDto) {
        String date = deleteDto.getDate();
        calendarMapper.deleteDataConfig(date);
    }

    /**
     * 导出节假日或工作日设置
     *
     * @param year 导出年份
     * @param response 响应
     */
    @Override
    public void exportCalendarSet(String year, HttpServletResponse response) {
        // 获取数据列表
        List<CalendarVo> list = listCalendarSet(year);

        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            String filename = year.concat("年度日历导出清单");
            String sheetName = "日历导出清单";
            String[] headers = {"日期", "类型", "备注", "创建人", "创建时间"};
            String[] tableNames = {"date", "typeName", "remark", "createUser", "createTime"};
            ExcelUtils.exportExcel(false, workbook, filename, sheetName, headers, tableNames, list, null, response);
        } catch (Exception e) {
            log.error("导出节假日或工作日设置失败[{}]", year, e);
            throw new ServiceException("导出失败: " + e.getMessage());
        }
    }

    /**
     * 导入节假日或工作日设置
     *
     * @param importDto 导入参数
     */
    @Override
    public void importCalendarSet(CalendarImportDto importDto) {
        String filePath = importDto.getUrl();

        File file = new File(filePath);
        if (!file.exists()) {
            throw new ServiceException("导入的文件不存在");
        }

        if (!ExcelUtils.isExcelFile(filePath)) {
            file.deleteOnExit();
            throw new ServiceException("不支持的文件类型，请使用下载的模板进行导入");
        }

        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = WorkbookFactory.create(fis)) {
            // 遍历数据: 日期 类型（0：节假日；1：工作日） 备注
            Sheet sheet = workbook.getSheetAt(0);
            for (Row cells : sheet) {
                // 第一行跳过
                int realRowNum = cells.getRowNum() + 1;
                if (realRowNum == 1) {
                    continue;
                }

                Cell dateCell = cells.getCell(0);
                if (ExcelUtils.isCellBlank(dateCell)) {
                    // 已结束
                    break;
                }

                Cell flagCell = cells.getCell(1);
                if (ExcelUtils.isCellBlank(flagCell)) {
                    throw new ServiceException(String.format("第%s行没有填写日期类型", realRowNum));
                }

                String flag = ExcelUtils.getStringCellValue(flagCell);
                if (!HolidayType.HOLIDAY.value().equals(flag) && !HolidayType.WORKDAY.value().equals(flag)) {
                    throw new ServiceException(String.format("第%s行没有填写日期类型配置错误，有效范围['0', '1']", realRowNum));
                }

                String date = ExcelUtils.getStringCellValue(dateCell);
                String remark = ExcelUtils.getStringCellValue(cells.getCell(2));

                CalendarInsertDto insertDto = new CalendarInsertDto();
                insertDto.setDate(date);
                insertDto.setType(flag);
                insertDto.setRemark(remark);
                setHolidayOrWorkday(insertDto);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("导入节假日或工作日设置失败[{}]", importDto, e);
            throw new ServiceException("导入失败，系统发生了未知的错误");
        } finally {
            // delete file
            file.deleteOnExit();
        }
    }
}
