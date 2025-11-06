package com.github.stazxr.zblog.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.log.domain.dto.LogQueryDto;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.domain.vo.LogVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 日志业务层
 *
 * @author SunTao
 * @since 2021-05-16
 */
public interface LogService extends IService<Log> {
    /**
     * 分页查询日志列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<LogVo>
     */
    PageInfo<LogVo> queryLogListByPage(LogQueryDto queryDto);

    /**
     * 导出日志列表
     *
     * @param queryDto 查询参数
     * @param response HttpServletResponse
     */
    void exportLogList(LogQueryDto queryDto, HttpServletResponse response);

    /**
     * 查询日志堆栈详情
     *
     * @param logId 日志序号
     * @return exceptionDetail
     */
    String queryLogExpDetail(Long logId);

    /**
     * 删除操作日志
     *
     * @param queryDto 查询参数
     */
    void deleteLog(LogQueryDto queryDto);
}
