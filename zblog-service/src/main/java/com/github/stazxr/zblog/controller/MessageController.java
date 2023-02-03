package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.query.MessageQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 留言管理
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    /**
     * 分页查询后台留言列表
     *
     * @param queryDto 查询参数
     * @return MessageVoList
     */
    @GetMapping(value = "/pageList")
    @Router(name = "分页查询后台留言列表", code = "pageMessageList")
    public Result pageMessageList(MessageQueryDto queryDto) {
        return Result.success().data(messageService.pageMessageList(queryDto));
    }

    /**
     * 删除留言
     *
     * @param messageIds 留言列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteMessage")
    @Router(name = "删除留言", code = "deleteMessage")
    public Result deleteMessage(@RequestBody List<Long> messageIds) {
        messageService.deleteMessage(messageIds);
        return Result.success();
    }

    /**
     * 审核留言
     *
     * @param messageIds 留言列表
     * @return Result
     */
    @Log
    @PostMapping(value = "/auditMessage")
    @Router(name = "审核留言", code = "auditMessage")
    public Result auditMessage(@RequestBody List<Long> messageIds) {
        messageService.auditMessage(messageIds);
        return Result.success();
    }
}
