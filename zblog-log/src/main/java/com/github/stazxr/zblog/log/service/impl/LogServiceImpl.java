package com.github.stazxr.zblog.log.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.log.annotation.IgnoredLog;
import com.github.stazxr.zblog.log.domain.dto.LogQueryDto;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.domain.enums.LogType;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import com.github.stazxr.zblog.log.mapper.LogMapper;
import com.github.stazxr.zblog.log.service.LogService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.IdUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.ThrowableUtils;
import com.github.stazxr.zblog.util.office.ExcelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
    private static final String[] IGNORED_PARAM = {"HttpServletResponse", "HttpServletRequest", "MultipartFile", "MultipartFile[]"};

    private static final String RESULT_CLASS = "Result";

    private static final int MAX_PARAM_LENGTH = 65535;

    private final LogMapper logMapper;

    /**
     * 保存日志信息
     *
     * @param joinPoint 切点信息
     * @param log       日志信息
     * @param result    执行结果
     * @param e         异常信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveLog(ProceedingJoinPoint joinPoint, Log log, Object result, Throwable e) {
        // 获取路由信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        IgnoredLog ignoredLog = method.getAnnotation(IgnoredLog.class);
        if (ignoredLog != null && e == null) {
            // 过滤非异常日志
            return;
        }

        Router aopRouter = method.getAnnotation(Router.class);
        if (aopRouter == null) {
            return;
        }

        com.github.stazxr.zblog.log.annotation.Log aopLog = method.getAnnotation(com.github.stazxr.zblog.log.annotation.Log.class);
        if (aopLog != null) {
            // 操作日志
            log.setLogType(LogType.OPERATE.getValue());
            log.setDescription(StringUtils.isBlank(aopLog.value()) ? aopRouter.name() : aopLog.value());
        } else {
            // 接口日志
            log.setLogType(LogType.API.getValue());
            log.setDescription(aopRouter.name());
        }

        // 设置响应结果
        if (result != null && RESULT_CLASS.equals(method.getReturnType().getSimpleName())) {
            // 标准返回
            String resultStr = JSON.toJSONString(result);
            JSONObject resultObj = JSON.parseObject(resultStr, JSONObject.class);
            log.setExecResult(HttpStatus.OK.value() == resultObj.getInteger("code"));
            log.setExecMessage(resultObj.getString("message"));
        } else {
            // 返回不是Result，默认返回的是数据，默认成功
            log.setExecResult(true);
            log.setExecMessage(result == null ? null : JSON.toJSONString(result));
        }

        // 异常信息
        if (e != null) {
            log.setExecResult(false);
            if (e instanceof ServiceException) {
                // 业务异常
                log.setExecMessage(e.getMessage());
            } else {
                // 系统异常
                log.setLogType(LogType.ERROR.getValue());
                log.setExecMessage("系统发生未知错误：".concat(e.getMessage()));
                log.setExceptionDetail(ThrowableUtils.getStackTrace(e).getBytes());
            }
        }

        // 设置其他信息
        log.setId(IdUtils.getId());
        String parameter = getParameter(method, joinPoint.getArgs());
        log.setRequestParam(parameter.length() > MAX_PARAM_LENGTH ? "参数内容过长" : parameter);
        save(log);
    }

    /**
     * 查询用户日志列表
     *
     * @param queryDto 查询参数
     * @return userLog
     */
    @Override
    public PageInfo<LogVo> queryUserLogListByPage(LogQueryDto queryDto) {
        queryDto.checkPage();
        Assert.notNull(queryDto.getUsername(), "参数【username】不能为空");

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<LogVo> dataList = logMapper.selectLogList(queryDto);
        return new PageInfo<>(dataList);
    }

    /**
     * 分页查询日志列表
     *
     * @param queryDto 查询参数
     * @return LogList
     */
    @Override
    public PageInfo<LogVo> queryLogListByPage(LogQueryDto queryDto) {
        queryDto.checkPage();

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(logMapper.selectLogList(queryDto));
    }

    /**
     * 导出日志列表
     *
     * @param queryDto 查询参数
     * @param response response
     */
    @Override
    public void exportLogList(LogQueryDto queryDto, HttpServletResponse response) {
        List<LogVo> dataList = logMapper.selectLogList(queryDto);

        List<Map<String, Object>> data = new ArrayList<>();
        for (LogVo logVo : dataList) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("日志序列", String.valueOf(logVo.getId()));
            item.put("日志类型", LogType.of(logVo.getLogType()));
            item.put("操作描述", logVo.getDescription());
            item.put("操作用户", logVo.getOperateUser());
            item.put("请求IP", logVo.getRequestIp());
            item.put("请求来源", logVo.getAddress());
            item.put("浏览器", logVo.getBrowser());
            item.put("请求地址", logVo.getRequestUri().concat("_").concat(logVo.getRequestMethod()));
            item.put("请求结果", LogType.ERROR.getValue() == logVo.getLogType() ? "系统错误" : logVo.isExecResult() ? "成功" : "失败");
            item.put("请求耗时", logVo.getCostTime());
            item.put("结果描述", logVo.getExecMessage());
            item.put("请求时间", logVo.getEventTime());
            data.add(item);
        }

        ExcelUtils.downloadExcel(data, response);
    }

    /**
     * 删除日志列表
     *
     * @param logType 日志类型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLog(Integer logType) {
        Assert.notNull(logType, "参数【logType】不能为空");
        logMapper.deleteLog(logType);
    }

    /**
     * 查询日志堆栈详情
     *
     * @param logId 日志序号
     * @return exceptionDetail
     */
    @Override
    public String queryLogErrorDetail(Long logId) {
        Assert.notNull(logId, "参数【logId】不能为空");
        return logMapper.selectLogErrorDetail(logId);
    }

    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String paramTypeName = parameter.getType().getSimpleName();
            if (Arrays.asList(IGNORED_PARAM).contains(paramTypeName)) {
                // 不记录HttpServletResponse、HttpServletRequest参数信息
                continue;
            }

            RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
            RequestPostSingleParam singleParam = parameter.getAnnotation(RequestPostSingleParam.class);
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if (requestBody != null) {
                argList.add(args[i]);
            } else if (singleParam != null) {
                argList.add(args[i]);
            } else if (requestParam != null) {
                Map<String, Object> map = new HashMap<>(4);
                String key = StringUtils.isEmpty(requestParam.value()) ? parameter.getName() : requestParam.value();
                map.put(key, args[i]);
                argList.add(map);
            } else {
                argList.add(args[i]);
            }
        }

        return argList.isEmpty() ? "" : argList.size() == 1 ? JSON.toJSONString(argList.get(0)) : JSON.toJSONString(argList);
    }
}
