package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.query.MessageQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 弹幕管理
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Api(value = "MessageController", tags = { "弹幕控制器" })
public class MessageController {
    private final MessageService messageService;

    /**
     * 分页查询弹幕列表
     *
     * @param queryDto 查询参数
     * @return MessageVoList
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询弹幕列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询弹幕列表", code = "pageMessageList")
    public Result pageMessageList(MessageQueryDto queryDto) {
        return Result.success().data(messageService.pageMessageList(queryDto));
    }

    /**
     * 删除弹幕
     *
     * @param messageIds 弹幕id列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteMessage")
    @ApiOperation(value = "删除弹幕")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "messageIds", value = "弹幕id列表", required = true, dataTypeClass = List.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除弹幕", code = "deleteMessage")
    public Result deleteMessage(@RequestBody List<Long> messageIds) {
        messageService.deleteMessage(messageIds);
        return Result.success();
    }

    /**
     * 弹幕审核
     *
     * @param messageIds 弹幕id列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/auditMessage")
    @ApiOperation(value = "弹幕审核")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "messageIds", value = "弹幕id列表", required = true, dataTypeClass = List.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "弹幕审核", code = "auditMessage")
    public Result auditMessage(@RequestBody List<Long> messageIds) {
        messageService.auditMessage(messageIds);
        return Result.success();
    }
}
