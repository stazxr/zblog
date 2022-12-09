package com.github.stazxr.zblog.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 前台服务层
 *
 * @author SunTao
 * @since 2022-11-25
 */
public interface PortalService {
    /**
     * 记录访客信息
     *
     * @param request 请求信息
     */
    void recordVisitor(HttpServletRequest request);
}
