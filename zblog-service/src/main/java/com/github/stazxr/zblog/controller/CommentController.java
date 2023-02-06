package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.CommentService;
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
public class CommentController {
    private final CommentService commentService;

    /**
     * 分页查询后台评论列表
     *
     * @param queryDto 查询参数
     * @return CommentVoList
     */
    @GetMapping(value = "/pageList")
    @Router(name = "分页查询后台评论列表", code = "pageCommentList")
    public Result pageCommentList(CommentQueryDto queryDto) {
        return Result.success().data(commentService.pageCommentList(queryDto));
    }

    /**
     * 删除评论
     *
     * @param commentIds 评论列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteComment")
    @Router(name = "删除评论", code = "deleteComment")
    public Result deleteComment(@RequestBody List<Long> commentIds) {
        commentService.deleteComment(commentIds);
        return Result.success();
    }

    /**
     * 审核评论
     *
     * @param commentIds 评论列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/auditComment")
    @Router(name = "审核评论", code = "auditComment")
    public Result auditComment(@RequestBody List<Long> commentIds) {
        commentService.auditComment(commentIds);
        return Result.success();
    }
}
