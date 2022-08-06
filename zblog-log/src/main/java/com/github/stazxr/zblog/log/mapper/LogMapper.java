package com.github.stazxr.zblog.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.domain.vo.LogVo;

import java.util.List;
import java.util.Map;

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
    List<LogVo> selectLogList(Map<String, Object> param);
}
