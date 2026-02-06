package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.base.domain.dto.query.UserLoginLogQueryDto;
import com.github.stazxr.zblog.base.domain.vo.UserLoginLogVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 登录日志业务层
 *
 * @author SunTao
 * @since 2026-01-23
 */
public interface UserLoginLogService {
    /**
     * 分页查询登录日志列表
     *
     * @param queryDto 查询参数
     * @return IPage<UserLoginLogVo>
     */
    IPage<UserLoginLogVo> pageUserLoginLogList(UserLoginLogQueryDto queryDto);

    /**
     * 导出登录日志
     *
     * @param queryDto 查询参数
     * @param response Response
     */
    void exportUserLoginLog(UserLoginLogQueryDto queryDto, HttpServletResponse response);
}
