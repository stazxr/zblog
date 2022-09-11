package com.github.stazxr.zblog.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.log.domain.dto.LogQueryDto;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
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
     * @param result    执行结果
     */
    void saveLog(HttpServletRequest request, ProceedingJoinPoint joinPoint, Log log, Object result);

    /**
     * 查询用户日志列表
     *
     * @param queryDto 查询参数
     * @return userLog
     */
    PageInfo<LogVo> queryUserLog(LogQueryDto queryDto);
}
