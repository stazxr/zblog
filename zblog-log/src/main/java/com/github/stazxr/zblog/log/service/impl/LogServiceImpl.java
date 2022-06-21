package com.github.stazxr.zblog.log.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.util.IpImplUtils;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.mapper.LogMapper;
import com.github.stazxr.zblog.log.service.LogService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.IdUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
@Service
@Transactional(rollbackFor = Exception.class)
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    /**
     * 保存日志信息
     *
     * @param request   请求信息
     * @param joinPoint 切点信息
     * @param log       日志信息
     */
    @Override
    public void saveLog(HttpServletRequest request, ProceedingJoinPoint joinPoint, Log log) {
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

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";
        log.setOperateMethod(methodName);

        log.setId(IdUtils.getId());
        log.setOperateUser(SecurityUtils.getLoginUsernameNoEor());
        log.setOperateParam(getParameter(method, joinPoint.getArgs()));
        log.setRequestIp(IpUtils.getIp(request));
        log.setRequestUri(request.getRequestURI());
        log.setRequestMethod(request.getMethod());
        log.setAddress(IpImplUtils.getCityInfo(log.getRequestIp()));
        log.setBrowser(IpUtils.getBrowser(request));
        save(log);
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
