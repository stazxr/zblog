package com.github.stazxr.zblog.content.ext.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.content.ext.domain.dto.CommentAuditDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.CommentVo;
import com.github.stazxr.zblog.content.ext.service.CommentService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 评论管理
 *
 * @author SunTao
 * @since 2026-09-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Api(value = "CommentController", tags = { "评论管理" })
public class CommentController {
    private final CommentService commentService;

    /**
     * 分页查询评论列表
     *
     * @param queryDto 查询参数
     * @return IPage<CommentVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询评论列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询评论列表", code = "COMNQ001")
    public IPage<CommentVo> queryCommentListByPage(CommentQueryDto queryDto) {
        return commentService.queryCommentListByPage(queryDto);
    }

    /**
     * 查询评论详情
     *
     * @param commentId 评论id
     * @return CommentVo
     */
    @GetMapping(value = "/queryCommentDetail")
    @ApiOperation(value = "查询评论详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询评论详情", code = "COMNQ002")
    public CommentVo queryCommentDetail(@RequestParam Long commentId) {
        return commentService.queryCommentDetail(commentId);
    }

    /**
     * 审核评论
     *
     * @param auditDto 评论审核信息
     */
    @Log
    @PostMapping(value = "/auditComment")
    @ApiOperation(value = "审核评论")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "审核评论", code = "COMNU001")
    public void auditComment(@RequestBody @Validated CommentAuditDto auditDto) {
        commentService.auditComment(auditDto);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     */
    @Log
    @PostMapping(value = "/deleteComment")
    @ApiOperation(value = "删除评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除评论", code = "COMND001")
    public void deleteComment(@RequestParam Long commentId) {
        commentService.deleteComment(commentId);
    }
}
