package com.github.stazxr.zblog.portal.service;

import com.github.stazxr.zblog.portal.domain.dto.BarrageMessageDto;

import javax.servlet.http.HttpServletRequest;

/**
 * 门户管理业务层
 *
 * @author SunTao
 * @since 2027-07-07
 */
public interface PortalService {
    /**
     * 新增弹幕
     *
     * @param request 请求信息
     * @param barrageMessageDto 弹幕信息
     */
    void addBarrageMessage(HttpServletRequest request, BarrageMessageDto barrageMessageDto);
}
