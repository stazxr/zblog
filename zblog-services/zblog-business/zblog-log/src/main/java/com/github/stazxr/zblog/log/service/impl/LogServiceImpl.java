package com.github.stazxr.zblog.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.log.domain.dto.LogQueryDto;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.domain.enums.LogType;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import com.github.stazxr.zblog.log.mapper.LogMapper;
import com.github.stazxr.zblog.log.service.LogService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.office.ExcelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 日志业务实现层
 *
 * @author SunTao
 * @since 2021-05-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    private final LogMapper logMapper;

    /**
     * 分页查询日志列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<LogVo>
     */
    @Override
    public PageInfo<LogVo> queryLogListByPage(LogQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        Assert.notNull(queryDto.getLogType(), ExpMessageCode.of("valid.log.logType.NotNull"));
        checkCommonParam(queryDto);
        // 分页查询
        try (Page<LogVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<LogVo> dataList = logMapper.selectLogList(queryDto);
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }

    /**
     * 导出日志列表
     *
     * @param queryDto 查询参数
     * @param response HttpServletResponse
     */
    @Override
    public void exportLogList(LogQueryDto queryDto, HttpServletResponse response) {
        // 参数检查
        Assert.notNull(queryDto.getLogType(), ExpMessageCode.of("valid.log.logType.NotNull"));
        checkCommonParam(queryDto);
        // 查询并遍历数据
        List<LogVo> dataList = logMapper.selectLogList(queryDto);
        List<Map<String, Object>> data = new ArrayList<>();
        for (LogVo vo : dataList) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("日志序列", String.valueOf(vo.getId()));
            item.put("日志类型", LogType.of(vo.getLogType()));
            item.put("操作描述", vo.getDescription());
            item.put("接口编码", vo.getInterfaceCode());
            item.put("操作用户", vo.getOperateUser());
            item.put("请求IP", vo.getRequestIp());
            item.put("请求来源", vo.getAddress());
            item.put("浏览器", vo.getBrowser());
            item.put("请求地址", vo.getRequestUri().concat("_").concat(vo.getRequestMethod()));
            item.put("请求结果", vo.isExecResult() ? "成功" : "失败");
            item.put("结果描述", vo.getExecMessage());
            item.put("请求耗时", vo.getCostTime());
            item.put("请求时间", vo.getEventTime());
            data.add(item);
        }
        // 导出数据
        ExcelUtils.downloadExcel(data, response);
    }

    /**
     * 查询日志堆栈详情
     *
     * @param logId 日志序号
     * @return exceptionDetail
     */
    @Override
    public String queryLogExpDetail(Long logId) {
        Assert.notNull(logId, ExpMessageCode.of("valid.common.id.NotNull"));
        return logMapper.selectLogExpDetail(logId);
    }

    /**
     * 删除操作日志
     *
     * @param queryDto 查询参数
     */
    @Override
    public void deleteLog(LogQueryDto queryDto) {
        Assert.notNull(queryDto.getLogType(), ExpMessageCode.of("valid.log.logType.NotNull"));
        checkCommonParam(queryDto);
        logMapper.deleteLog(queryDto);
    }

    private void checkCommonParam(LogQueryDto queryDto) {
        if (StringUtils.isNotBlank(queryDto.getDescription())) {
            queryDto.setDescription(queryDto.getDescription().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getInterfaceCode())) {
            queryDto.setInterfaceCode(queryDto.getInterfaceCode().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getUsername())) {
            queryDto.setUsername(queryDto.getUsername().trim());
        }
    }
}
