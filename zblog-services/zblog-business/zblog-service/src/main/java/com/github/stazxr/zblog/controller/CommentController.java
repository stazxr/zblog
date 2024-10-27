package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论管理
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Api(value = "CommentController", tags = { "评论控制器" })
public class CommentController {
    private final CommentService commentService;

    /**
     * 分页查询评论列表
     *
     * @param queryDto 查询参数
     * @return CommentVoList
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询评论列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询评论列表", code = "pageCommentList")
    public Result pageCommentList(CommentQueryDto queryDto) {
        return Result.success().data(commentService.pageCommentList(queryDto));
    }

    /**
     * 删除评论
     *
     * @param commentIds 评论id列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteComment")
    @ApiOperation(value = "删除评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "commentIds", value = "评论id列表", required = true, dataTypeClass = List.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除评论", code = "deleteComment")
    public Result deleteComment(@RequestBody List<Long> commentIds) {
        commentService.deleteComment(commentIds);
        return Result.success();
    }

    /**
     * 审核评论
     *
     * @param commentIds 评论id列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/auditComment")
    @ApiOperation(value = "审核评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "commentIds", value = "评论id列表", required = true, dataTypeClass = List.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "审核评论", code = "auditComment")
    public Result auditComment(@RequestBody List<Long> commentIds) {
        commentService.auditComment(commentIds);
        return Result.success();
    }
}
