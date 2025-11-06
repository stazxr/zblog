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
     * 查询日志异常堆栈
     *
     * @param logId 日志id
     * @return 异常堆栈
     */
    String selectLogExpDetail(@Param("logId") Long logId);

    /**
     * 删除操作日志
     *
     * @param queryDto 查询参数
     */
    void deleteLog(LogQueryDto queryDto);
}
