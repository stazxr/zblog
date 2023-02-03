package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.MessageDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.service.PortalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 门户管理
 *
 * @author SunTao
 * @since 2022-11-25
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portal")
public class PortalController {
    private final PortalService portalService;

    /**
     * 查询博客前台信息
     *
     * @return BlogWebVo
     */
    @GetMapping("/queryBlogInfo")
    @Router(name = "查询博客前台信息", code = "queryBlogWebInfo", level = BaseConst.PermLevel.OPEN)
    public Result queryBlogWebInfo() {
        return Result.success().data(portalService.queryBlogWebInfo());
    }

    /**
     * 记录访客信息
     *
     * @param request 请求信息
     * @return Result
     */
    @PostMapping(value = "/recordVisitor")
    @Router(name = "记录访客信息", code = "recordVisitor", level = BaseConst.PermLevel.OPEN)
    public Result recordVisitor(HttpServletRequest request) {
        portalService.recordVisitor(request);
        return Result.success();
    }

    /**
     * 查询首页轮播的说说列表
     *
     * @return TalkList
     */
    @GetMapping("/queryTalkList")
    @Router(name = "查询首页轮播的说说列表", code = "queryWebTalkList", level = BaseConst.PermLevel.OPEN)
    public Result queryTalkList() {
        return Result.success().data(portalService.queryTalkList());
    }

    /**
     * 分页查询前台文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleList
     */
    @GetMapping("/queryArticleList")
    @Router(name = "分页查询前台文章列表", code = "queryWebArticleList", level = BaseConst.PermLevel.OPEN)
    public Result queryArticleList(ArticleQueryDto queryDto) {
        return Result.success().data(portalService.queryArticleList(queryDto));
    }

    /**
     * 分页查询前台文章详情
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    @GetMapping("/queryArticleDetail")
    @Router(name = "分页查询前台文章详情", code = "queryWebArticleDetail", level = BaseConst.PermLevel.OPEN)
    public Result queryArticleDetail(Long articleId) {
        return Result.success().data(portalService.queryArticleDetail(articleId));
    }

    /**
     * 查询前台弹幕列表
     *
     * @return MessageVo
     */
    @GetMapping("/queryMessageList")
    @Router(name = "查询前台弹幕列表", code = "queryWebMessageList", level = BaseConst.PermLevel.OPEN)
    public Result queryMessageList() {
        return Result.success().data(portalService.queryMessageList());
    }

    /**
     * 留言板留言
     *
     * @param request    请求信息
     * @param messageDto 留言信息
     * @return Result
     */
    @PostMapping(value = "/saveMessage")
    @Router(name = "留言板留言", code = "saveMessage", level = BaseConst.PermLevel.OPEN)
    public Result recordVisitor(HttpServletRequest request, @RequestBody MessageDto messageDto) {
        portalService.saveMessage(request, messageDto);
        return Result.success();
    }
}
