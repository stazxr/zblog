package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.FriendLinkDto;
import com.github.stazxr.zblog.domain.dto.query.FriendLinkQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.FriendLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "FriendLinkController", tags = { "友链控制器" })
public class FriendLinkController {
    private final FriendLinkService friendLinkService;

    /**
     * 分页查询友链列表
     *
     * @param queryDto 查询参数
     * @return FriendLinkVoList
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询友链列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询友链列表", code = "queryFriendLinkListByPage")
    public Result queryFriendLinkListByPage(FriendLinkQueryDto queryDto) {
        return Result.success().data(friendLinkService.queryFriendLinkListByPage(queryDto));
    }

    /**
     * 查询友链详情
     *
     * @param friendLinkId 友链id
     * @return FriendLinkVo
     */
    @GetMapping(value = "/queryFriendLinkDetail")
    @ApiOperation(value = "查询友链详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "friendLinkId", value = "友链id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询友链详情", code = "queryFriendLinkDetail")
    public Result queryFriendLinkDetail(@RequestParam Long friendLinkId) {
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
    @ApiOperation(value = "新增友链")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
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
    @ApiOperation(value = "编辑友链")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
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
    @ApiOperation(value = "删除友链")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "friendLinkId", value = "友链id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除友链", code = "deleteFriendLink")
    public Result deleteFriendLink(@RequestParam Long friendLinkId) {
        friendLinkService.deleteFriendLink(friendLinkId);
        return Result.success();
    }
}
