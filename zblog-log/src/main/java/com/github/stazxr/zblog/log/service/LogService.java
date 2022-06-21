package com.github.stazxr.zblog.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.log.domain.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志业务层
 *
 * @author SunTao
 * @since 2021-05-16
 */
public interface LogService extends IService<Log> {
    /**
     * 保存日志信息
     *
     * @param request   请求信息
     * @param joinPoint 切点信息
     * @param log       日志信息
     */
    void saveLog(HttpServletRequest request, ProceedingJoinPoint joinPoint, Log log);
}
