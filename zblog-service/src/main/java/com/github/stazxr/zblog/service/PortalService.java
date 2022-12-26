package com.github.stazxr.zblog.service;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import com.github.stazxr.zblog.domain.vo.BlogWebVo;
import com.github.stazxr.zblog.domain.vo.TalkVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 查询首页轮播的说说列表
     *
     * @return TalkList
     */
    List<TalkVo> queryTalkList();

    /**
     * 分页查询前台文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleList
     */
    PageInfo<ArticleVo> queryArticleList(ArticleQueryDto queryDto);
}
