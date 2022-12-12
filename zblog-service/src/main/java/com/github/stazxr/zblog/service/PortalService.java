package com.github.stazxr.zblog.service;

import com.github.stazxr.zblog.domain.vo.BlogWebVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 前台服务层
 *
 * @author SunTao
 * @since 2022-11-25
 */
public interface PortalService {
    /**
     * 查询博客前台信息
     *
     * @return BlogWebVo
     */
    BlogWebVo queryBlogWebInfo();

    /**
     * 记录访客信息
     *
     * @param request 请求信息
     */
    void recordVisitor(HttpServletRequest request);
}
