package com.github.stazxr.zblog.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.log.domain.dto.LogQueryDto;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 日志数据持久层
 *
 * @author SunTao
 * @since 2021-05-16
 */
public interface LogMapper extends BaseMapper<Log> {
    /**
     * 查询日志列表
     *
     * @param param 查询参数
     * @return LogVoList
     */
    List<LogVo> selectLogList(LogQueryDto param);

    /**
     * 删除日志列表
     *
     * @param logType 日志类型
     */
    void deleteLog(@Param("logType") Integer logType);

    /**
     * 查询日志堆栈详情
     *
     * @param logId 日志序号
     * @return exceptionDetail
     */
    String selectLogErrorDetail(@Param("logId") Long logId);
}
