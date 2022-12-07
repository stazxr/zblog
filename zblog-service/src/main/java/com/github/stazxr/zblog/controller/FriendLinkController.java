package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.FriendLinkDto;
import com.github.stazxr.zblog.domain.dto.query.FriendLinkQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.FriendLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 友链管理
 *
 * @author SunTao
 * @since 2020-03-16
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friendLinks")
public class FriendLinkController {
    private final FriendLinkService friendLinkService;

    /**
     * 分页查询友链列表
     *
     * @param queryDto 查询参数
     * @return FriendLinkVoList
     */
    @GetMapping(value = "/pageList")
    @Router(name = "分页查询友链列表", code = "queryFriendLinkListByPage")
    public Result queryFriendLinkListByPage(FriendLinkQueryDto queryDto) {
        return Result.success().data(friendLinkService.queryFriendLinkListByPage(queryDto));
    }

    /**
     * 查询友链详情
     *
     * @param friendLinkId 友链ID
     * @return FriendLinkVo
     */
    @GetMapping(value = "/queryFriendLinkDetail")
    @Router(name = "查询友链详情", code = "queryFriendLinkDetail")
    public Result queryFriendLinkDetail(Long friendLinkId) {
        return Result.success().data(friendLinkService.queryFriendLinkDetail(friendLinkId));
    }

    /**
     * 新增友链
     *
     * @param friendLinkDto 友链信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addFriendLink")
    @Router(name = "新增友链", code = "addFriendLink")
    public Result addFriendLink(@RequestBody FriendLinkDto friendLinkDto) {
        friendLinkService.addFriendLink(friendLinkDto);
        return Result.success();
    }

    /**
     * 编辑友链
     *
     * @param friendLinkDto 友链信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/editFriendLink")
    @Router(name = "编辑友链", code = "editFriendLink")
    public Result editFriendLink(@RequestBody FriendLinkDto friendLinkDto) {
        friendLinkService.editFriendLink(friendLinkDto);
        return Result.success();
    }

    /**
     * 删除友链
     *
     * @param friendLinkId 友链ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteFriendLink")
    @Router(name = "删除友链", code = "deleteFriendLink")
    public Result deleteFriendLink(@RequestPostSingleParam Long friendLinkId) {
        friendLinkService.deleteFriendLink(friendLinkId);
        return Result.success();
    }
}
