package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.base.domain.dto.query.UserLoginLogQueryDto;
import com.github.stazxr.zblog.base.domain.vo.UserLoginLogVo;
import com.github.stazxr.zblog.base.mapper.UserLoginLogMapper;
import com.github.stazxr.zblog.base.service.UserLoginLogService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.office.ExcelUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录日志业务实现层
 *
 * @author SunTao
 * @since 2026-01-23
 */
@Service
@RequiredArgsConstructor
public class UserLoginLogServiceImpl implements UserLoginLogService {
    private final UserLoginLogMapper userLoginLogMapper;

    /**
     * 分页查询登录日志列表
     *
     * @param queryDto 查询参数
     * @return IPage<UserLoginLogVo>
     */
    @Override
    public IPage<UserLoginLogVo> pageUserLoginLogList(UserLoginLogQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getUsername())) {
            queryDto.setUsername(queryDto.getUsername().trim());
        }
        // 分页查询
        Page<UserLoginLogVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return userLoginLogMapper.selectUserLoginLogList(page, queryDto);
    }

    /**
     * 导出登录日志
     *
     * @param queryDto 查询参数
     * @param response Response
     */
    @Override
    public void exportUserLoginLog(UserLoginLogQueryDto queryDto, HttpServletResponse response) {
        // 参数检查
        if (StringUtils.isNotBlank(queryDto.getUsername())) {
            queryDto.setUsername(queryDto.getUsername().trim());
        }
        // 查询并遍历数据
        Page<UserLoginLogVo> page = new Page<>(1, Long.MAX_VALUE);
        page.setSearchCount(false);
        List<UserLoginLogVo> dataList = userLoginLogMapper.selectUserLoginLogList(page, queryDto).getRecords();
        List<Map<String, Object>> data = new ArrayList<>();
        for (UserLoginLogVo vo : dataList) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("登录用户", vo.getUsername());
            item.put("登录时间", DateUtils.format(vo.getLoginTime(), DateUtils.YMD_HMS_PATTERN));
            item.put("登录IP", vo.getLoginIp());
            item.put("登录地址", vo.getLoginAddress());
            item.put("登录平台", vo.getLoginPlatform());
            item.put("登录方式", parseLoginType(vo.getLoginType()));
            item.put("浏览器", vo.getLoginBrowser());
            item.put("浏览器版本", vo.getLoginBrowserVersion());
            item.put("登录结果", vo.getIsSuccess() ? "成功" : "失败");
            item.put("备注", vo.getRemark());
            data.add(item);
        }
        // 导出数据
        ExcelUtils.downloadExcel(data, response);
    }

    private Object parseLoginType(String loginType) {
        if (loginType != null) {
            switch (loginType) {
                case "LT00":
                    return "访客";
                case "LT01":
                    return "密码";
                case "LT02":
                    return "QQ互信";
                case "LT99":
                    return "未知";
                default:
                    return "-";
            }
        }
        return null;
    }
}
