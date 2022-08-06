package com.github.stazxr.zblog.log.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.core.annotation.Router;
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
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
     * @param request   请求信息
     * @param joinPoint 切点信息
     * @param log       日志信息
     * @param result    执行结果
     */
    @Override
    public void saveLog(HttpServletRequest request, ProceedingJoinPoint joinPoint, Log log, Object result) {
        Assert.notNull(log, "日志信息不能为空");

        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取注解信息
        com.github.stazxr.zblog.log.annotation.Log aopLog = method.getAnnotation(com.github.stazxr.zblog.log.annotation.Log.class);
        String desc;
        if (aopLog == null || StringUtils.isBlank(aopLog.value())) {
            // 这里依赖了业务的 Router 注解，如果不需要，可以去除这里的逻辑
            Router aopRouter = method.getAnnotation(Router.class);
            if (aopRouter != null) {
                desc = aopRouter.name();
            } else {
                // default: methodName
                desc = method.getName();
            }
        } else {
            desc = aopLog.value();
        }
        Assert.isTrue(desc.length() > 50, "操作描述长度不能大于50个字符");
        log.setDescription(desc);

        // 设置响应结果
        if (result != null && RESULT_CLASS.equals(method.getReturnType().getSimpleName())) {
            String resultStr = JSON.toJSONString(result);
            JSONObject resultObj = JSON.parseObject(resultStr, JSONObject.class);
            log.setExecResult(HttpStatus.OK.value() == resultObj.getInteger("code"));
            log.setExecMessage(resultObj.getString("message"));
        } else {
            // 返回不是Result，默认返回的是数据，默认成功
            log.setExecResult(LogType.ERROR.getValue().equals(log.getLogType()));
        }

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";
        log.setOperateMethod(methodName);

        log.setId(IdUtils.getId());
        log.setOperateUser(SecurityUtils.getLoginUsernameNoEor());
        log.setOperateParam(getParameter(method, joinPoint.getArgs()));
        log.setRequestInfo(request);
        save(log);
    }

    /**
     * 查询用户日志列表
     *
     * @param queryDto 查询参数
     * @return LogList
     */
    @Override
    public PageInfo<LogVo> queryUserLog(LogQueryDto queryDto) {
        Assert.notNull(queryDto.getPage(), "参数page不能为空");
        Assert.notNull(queryDto.getPageSize(), "参数pageSize不能为空");
        Assert.notNull(queryDto.getUsername(), "用户名不能为空");

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        Map<String, Object> param = new HashMap<>(CollectionUtils.mapSize(1));
        param.put("operateUser", queryDto.getUsername());
        param.put("logType", LogType.INFO.getValue());
        List<LogVo> dataList = logMapper.selectLogList(param);
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
