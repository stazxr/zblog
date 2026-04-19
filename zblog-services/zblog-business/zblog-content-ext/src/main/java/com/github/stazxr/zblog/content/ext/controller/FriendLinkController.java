package com.github.stazxr.zblog.content.ext.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.content.ext.domain.dto.FriendLinkDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.FriendLinkQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.FriendLinkVo;
import com.github.stazxr.zblog.content.ext.service.FriendLinkService;
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
 * 友链管理
 *
 * @author SunTao
 * @since 2020-03-16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friendLinks")
@Api(value = "FriendLinkController", tags = { "友链管理" })
public class FriendLinkController {
    private final FriendLinkService friendLinkService;

    /**
     * 分页查询友链列表
     *
     * @param queryDto 查询参数
     * @return IPage<FriendLinkVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询友链列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询友链列表", code = "LINKQ001")
    public IPage<FriendLinkVo> queryFriendLinkListByPage(FriendLinkQueryDto queryDto) {
        return friendLinkService.queryFriendLinkListByPage(queryDto);
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
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询友链详情", code = "LINKQ002")
    public FriendLinkVo queryFriendLinkDetail(@RequestParam Long friendLinkId) {
        return friendLinkService.queryFriendLinkDetail(friendLinkId);
    }

    /**
     * 新增友链
     *
     * @param friendLinkDto 友链信息
     */
    @Log
    @PostMapping(value = "/addFriendLink")
    @ApiOperation(value = "新增友链")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "新增友链", code = "LINKA001")
    public void addFriendLink(@RequestBody @Validated(Create.class) FriendLinkDto friendLinkDto) {
        friendLinkService.addFriendLink(friendLinkDto);
    }

    /**
     * 编辑友链
     *
     * @param friendLinkDto 友链信息
     */
    @Log
    @PostMapping(value = "/editFriendLink")
    @ApiOperation(value = "编辑友链")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "编辑友链", code = "LINKU001")
    public void editFriendLink(@RequestBody @Validated(Update.class) FriendLinkDto friendLinkDto) {
        friendLinkService.editFriendLink(friendLinkDto);
    }

    /**
     * 删除友链
     *
     * @param friendLinkId 友链ID
     */
    @Log
    @PostMapping(value = "/deleteFriendLink")
    @ApiOperation(value = "删除友链")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "friendLinkId", value = "友链id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除友链", code = "LINKD001")
    public void deleteFriendLink(@RequestParam Long friendLinkId) {
        friendLinkService.deleteFriendLink(friendLinkId);
    }
}
