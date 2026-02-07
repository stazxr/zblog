package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.bas.router.RouterExtLevel;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.base.domain.dto.query.InterfaceQueryDto;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;
import com.github.stazxr.zblog.base.mapper.DictMapper;
import com.github.stazxr.zblog.base.mapper.InterfaceMapper;
import com.github.stazxr.zblog.base.service.InterfaceService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.office.ExcelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口管理业务实现层
 *
 * @author SunTao
 * @since 2025-11-02
 */
@Service
@RequiredArgsConstructor
public class InterfaceServiceImpl implements InterfaceService {
    private final InterfaceMapper interfaceMapper;

    private final DictMapper dictMapper;

    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return IPage<InterfaceVo>
     */
    @Override
    public IPage<InterfaceVo> queryInterfaceListByPage(InterfaceQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        checkCommonParam(queryDto);
        // 分页查询
        Page<InterfaceVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return interfaceMapper.selectInterfaceListV2(page, queryDto);
    }

    /**
     * 导出接口列表
     *
     * @param queryDto 查询参数
     * @param response Response
     */
    @Override
    public void exportInterface(InterfaceQueryDto queryDto, HttpServletResponse response) {
        // 参数检查
        checkCommonParam(queryDto);
        // 查询并遍历数据
        Page<InterfaceVo> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<InterfaceVo> dataList = interfaceMapper.selectInterfaceListV2(page, queryDto);
        List<Map<String, Object>> data = new ArrayList<>();
        for (InterfaceVo vo : dataList.getRecords()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("接口名称", vo.getInterfaceName());
            item.put("接口编码", vo.getInterfaceCode());
            item.put("接口URI", vo.getInterfaceUri());
            item.put("接口Method", vo.getInterfaceMethod());
            item.put("接口级别", parseInterfaceLevel(vo.getInterfaceLevel()));
            item.put("接口状态", parseInterfaceStatus(vo.getInterfaceStatus()));
            item.put("当天调用次数", vo.getDailyCallCount());
            item.put("累计调用次数", vo.getTotalCallCount());
            item.put("失败请求次数", vo.getTotalFailureCount());
            item.put("调用成功率", vo.getCallSuccessRate());
            item.put("平均响应时间（单位：毫秒）", vo.getAvgResponseTime());
            item.put("最大响应时间（单位：毫秒）", vo.getMaxResponseTime());
            item.put("历史峰值 QPS", vo.getHistoryMaxQps());
            item.put("当天巅峰 QPS", vo.getTodayMaxQps());
            item.put("当天平均 QPS", vo.getTodayAvgQps());
            data.add(item);
        }
        // 导出数据
        ExcelUtils.downloadExcel(data, response);
    }

    private String parseInterfaceStatus(Integer interfaceStatus) {
        if (interfaceStatus == null) {
            return "";
        } else if (0 == interfaceStatus) {
            return "禁用";
        } else if (1 == interfaceStatus) {
            return "启用";
        } else if (2 == interfaceStatus) {
            return "默认";
        } else {
            return "";
        }
    }

    private String parseInterfaceLevel(Integer interfaceLevel) {
        if (interfaceLevel == null) {
            return "";
        } else if (RouterLevel.OPEN == interfaceLevel) {
            return "公开访问";
        } else if (RouterLevel.PUBLIC == interfaceLevel) {
            return "登录访问";
        } else if (RouterLevel.PERM == interfaceLevel) {
            return "授权访问";
        } else if (RouterExtLevel.FORBIDDEN == interfaceLevel) {
            return "禁止访问";
        } else {
            return "";
        }
    }

    private void checkCommonParam(InterfaceQueryDto queryDto) {
        if (StringUtils.isNotBlank(queryDto.getInterfaceName())) {
            queryDto.setInterfaceName(queryDto.getInterfaceName().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getInterfaceCode())) {
            queryDto.setInterfaceCode(queryDto.getInterfaceCode().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getInterfaceUri())) {
            queryDto.setInterfaceUri(queryDto.getInterfaceUri().trim());
        }

        String switchVar = dictMapper.selectDictValueByDictKey("INTERFACE_METRICS_FILTER_LOCAL");
        if ("1".equals(switchVar)) {
            queryDto.setFilterLocalData(true);
        }
    }
}
