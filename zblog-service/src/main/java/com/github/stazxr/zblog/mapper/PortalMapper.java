package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.domain.vo.BlogWebVo;

/**
 * 门户数据持久层
 *
 * @author SunTao
 * @since 2022-12-09
 */
public interface PortalMapper {
    /**
     * 查询博客前台信息
     *
     * @return BlogWebVo
     */
    BlogWebVo selectBlogWebInfo();
}
