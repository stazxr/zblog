package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.base.domain.dto.query.UserLoginLogQueryDto;
import com.github.stazxr.zblog.base.domain.entity.UserLoginLog;
import com.github.stazxr.zblog.base.domain.vo.UserLoginLogVo;
import org.apache.ibatis.annotations.Param;

/**
 * 用户登录日志数据层
 *
 * @author SunTao
 * @since 2025-10-17
 */
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {
    /**
     * 分页查询登录日志列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<UserLoginLogVo>
     */
    IPage<UserLoginLogVo> selectUserLoginLogList(@Param("page") Page<UserLoginLogVo> page, @Param("query") UserLoginLogQueryDto queryDto);
}
