package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.IgnoreResult;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.util.IpImplUtils;
import com.github.stazxr.zblog.log.annotation.IgnoredLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 数据测试，仅作个人测试用
 *
 * @author SunTao
 * @since 2023-02-19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Api(value = "TestController", tags = { "接口测试控制器" })
public class TestController {
    String[] headers = {
        "第一列", "第二列", "第三列", "第四列", "第五列", "第六列", "第七列", "第八列",
        "第九列", "第十列", "第十一列", "第十二列", "第十三列", "第十四列", "第十五列", "第十六列",
        "第十七列", "第十八列", "第十九列"
    };

    int maxLength = 100;

    String[] quartAry = { "第一季度", "第二季度", "第三季度", "第四季度" };

    String[] typeAry = { "1.1 ", "1.2 ", "2.1 ", "2.2 ", "2.3 ",
        "2.4 ", "2.5 ", "2.6 ", "2.7 ", "3 ", "4.1 ",
        "4.2 ", "4.3 ", "4.4 "
    };

    String[] gradeAry = { "A ", "B ", "C " };

    String[] booleanAry = { "1", "2" };

    String[] constructionAry = { "1", "2", "3" };

    String[] designAry = { "1", "2", "3", "4", "5", "6" };

    String[] businessAreaAry = { "1", "2", "3", "4", "5" };

    /**
     * 测试IP工具类
     *
     * @param ip IP地址
     * @return IP来源
     */
    @IgnoredLog
    @IgnoreResult
    @GetMapping(value = "/getIpSource")
    @ApiOperation(value = "测试IP工具类")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ip", value = "ip地址", required = true, dataTypeClass = String.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "测试IP工具类", code = "getIpSourceByTest", level = BaseConst.PermLevel.OPEN)
    public String testIpUtils(@RequestParam String ip) {
        log.info("IpImplUtils.getIpSourceByHttp({}): {}", ip, IpImplUtils.getIpSourceByHttp(ip));
        log.info("IpImplUtils.getIpSourceByLocal({}): {}", ip, IpImplUtils.getIpSourceByLocal(ip));
        return IpImplUtils.getIpSource(ip);
    }

    /**
     * 测试POI
     */
    @IgnoredLog
    @IgnoreResult
    @PostMapping(value = "/excelPoi")
    @ApiOperation(value = "测试POI")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "测试POI", code = "excelPoiByTest", level = BaseConst.PermLevel.OPEN)
    public void testExcelPoi(HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // 创建表单
            Sheet sheet = workbook.createSheet("项目导入模板（导入时请删除实例行，单次最多支持导入100个项目）");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = createHeaderCellStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                CellUtil.createCell(headerRow, i, headers[i], headerStyle);
                sheet.setColumnWidth(i, 4700);
            }

            // 创建 DataValidationHelper 对象
            DataValidationHelper validationHelper = sheet.getDataValidationHelper();

            // 设置项目季度的数据验证
            DataValidation validation = getCellUlValidation(validationHelper, 2, quartAry);
            sheet.addValidationData(validation);

            // 设置项目类型的数据验证
            validation = getCellUlValidation(validationHelper, 3, typeAry);
            sheet.addValidationData(validation);

            // 设置项目等级的数据验证
            validation = getCellUlValidation(validationHelper, 4, gradeAry);
            sheet.addValidationData(validation);

            // 设置是否重点项目的数据验证
            validation = getCellUlValidation(validationHelper, 5, booleanAry);
            sheet.addValidationData(validation);

            // 建设性质
            validation = getCellUlValidation(validationHelper, 6, constructionAry);
            sheet.addValidationData(validation);

            // 设计深度
            validation = getCellUlValidation(validationHelper, 10, designAry);
            sheet.addValidationData(validation);

            // 业务范围
            validation = getCellUlValidation(validationHelper, 11, businessAreaAry);
            sheet.addValidationData(validation);

            // 年初计划总人工日
            validation = getCellNumberValidation(validationHelper, 14);
            sheet.addValidationData(validation);

            // 项目启动计划总人工日
            validation = getCellNumberValidation(validationHelper, 15);
            sheet.addValidationData(validation);

            for (int i = 1; i <= maxLength; i++) {
                // 创建行
                Row dataRow = sheet.createRow(i);
                dataRow.setHeight((short) 500);
                for (int j = 0; j < headers.length; j++) {
                    // 创建单元格
                    Cell cell = dataRow.createCell(j);
                    cell.setCellType(CellType.STRING);

                    // 赋默认值
                    if (i == 1) {
                        setEgCellValue(cell);
                    } else {
                        cell.setCellValue("");
                    }
                }
            }

            String realFilename = "file.xls";
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realFilename, "UTF-8"));
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("export file catch eor", e);
            throw new RuntimeException("系统发生未知异常~");
        }
    }

    private void setEgCellValue(Cell cell) {
        int columnIndex = cell.getColumnIndex();
        switch (columnIndex) {
            case 0:
                cell.setCellValue("XXX");
                return;
            case 1:
                cell.setCellValue("XXX（格式：yyyy）");
                return;
            case 2:
                cell.setCellValue("XXX（非必选）");
                return;
            case 3:
            case 4:
            case 5:
            case 6:
            case 10:
            case 11:
                cell.setCellValue("AAA");
                return;
            case 7:
                cell.setCellValue("BBB");
                return;
            case 8:
            case 9:
                cell.setCellValue("CCC");
                return;
            case 12:
                cell.setCellValue("2023-01（格式：yyyy-MM）");
                return;
            case 13:
                cell.setCellValue("2023-02（格式：yyyy-MM）");
                return;
            case 14:
                cell.setCellValue("1（整数）");
                return;
            case 15:
                cell.setCellValue("1（整数，非必填）");
                return;
            case 16:
                cell.setCellValue("XXX（非必填）");
                return;
            case 17:
                cell.setCellValue("YYY（非必填）");
                return;
            case 18:
                cell.setCellValue("BBB（非必填）");
                return;
            default:
                cell.setCellValue("");
        }
    }

    private DataValidation getCellNumberValidation(DataValidationHelper validationHelper, int colIndex) {
        CellRangeAddressList typeRange = new CellRangeAddressList(1, maxLength, colIndex, colIndex);
        DataValidationConstraint constraint = validationHelper.createNumericConstraint(
                DataValidationConstraint.ValidationType.INTEGER, DataValidationConstraint.OperatorType.GREATER_THAN, "-1", null
        );
        DataValidation validation = validationHelper.createValidation(constraint, typeRange);
        validation.setShowErrorBox(true);
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("输入错误", "请输入整数");
        return validation;
    }

    private DataValidation getCellUlValidation(DataValidationHelper validationHelper, int colIndex, String[] quartAry) {
        CellRangeAddressList typeRange = new CellRangeAddressList(1, maxLength, colIndex, colIndex);
        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(quartAry);
        DataValidation validation = validationHelper.createValidation(constraint, typeRange);
        validation.setShowErrorBox(true);
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("输入错误", "只能输入下拉列表中的选项值");
        return validation;
    }

    private static CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
}
