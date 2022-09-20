package com.github.stazxr.zblog.log.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.util.SecurityUtils;
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
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志业务实现层
 *
 * @author SunTao
 * @since 2021-05-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    private static final String RESULT_CLASS = "Result";

    @Resource
    private LogMapper logMapper;

    /**
     * 保存日志信息
     *
     * @param joinPoint 切点信息
     * @param log       日志信息
     * @param result    执行结果
     * @param e         异常信息
     */
    @Override
    public void saveLog(ProceedingJoinPoint joinPoint, Log log, Object result, Throwable e) {
        // 获取路由信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
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
                log.setExecMessage("系统发生未知错误!");
                log.setExceptionDetail(ThrowableUtils.getStackTrace(e).getBytes());
            }
        }

        // 设置其他信息
        log.setId(IdUtils.getId());
        log.setRequestParam(getParameter(method, joinPoint.getArgs()));
        log.setOperateUser(SecurityUtils.getLoginUsernameNoEor());
        save(log);
    }

    /**
     * 查询用户日志列表
     *
     * @param queryDto 查询参数
     * @return userLog
     */
    @Override
    public PageInfo<LogVo> queryUserLog(LogQueryDto queryDto) {
        Assert.notNull(queryDto.getPage(), "参数page不能为空");
        Assert.notNull(queryDto.getPageSize(), "参数pageSize不能为空");
        Assert.notNull(queryDto.getUsername(), "用户名不能为空");

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<LogVo> dataList = logMapper.selectLogList(queryDto);
        return new PageInfo<>(dataList);
    }

    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            // 将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }

            // 将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>(4);
                String key = StringUtils.isEmpty(requestParam.value()) ? parameters[i].getName() : requestParam.value();
                map.put(key, args[i]);
                argList.add(map);
            }
        }

        return argList.isEmpty() ? "" : argList.size() == 1 ? JSONUtil.toJsonStr(argList.get(0)) : JSONUtil.toJsonStr(argList);
    }
}
