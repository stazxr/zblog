package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.TalkDto;
import com.github.stazxr.zblog.domain.dto.query.TalkQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.TalkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 说说模块
 *
 * @author SunTao
 * @since 2022-12-12
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/talks")
@Api(value = "TestController", tags = { "说说控制器" })
public class TalkController {
    private final TalkService talkService;

    /**
     * 分页查询说说列表
     *
     * @param queryDto 查询参数
     * @return TalkVoList
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询说说列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询说说列表", code = "queryTalkListByPage")
    public Result queryTalkListByPage(TalkQueryDto queryDto) {
        return Result.success().data(talkService.queryTalkListByPage(queryDto));
    }

    /**
     * 查询说说详情
     *
     * @param talkId 说说id
     * @return TalkVo
     */
    @GetMapping(value = "/queryTalkDetail")
    @ApiOperation(value = "查询说说详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询说说详情", code = "queryTalkDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryTalkDetail(@RequestParam Long talkId) {
        return Result.success().data(talkService.queryTalkDetail(talkId));
    }

    /**
     * 新增或编辑说说
     *
     * @param talkDto 说说信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addOrEditTalk")
    @ApiOperation(value = "新增或编辑说说")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增或编辑说说", code = "addOrEditTalk")
    public Result addOrEditTalk(@RequestBody TalkDto talkDto) {
        talkService.addOrEditTalk(talkDto);
        return Result.success();
    }

    /**
     * 删除说说
     *
     * @param talkId 说说ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteTalk")
    @ApiOperation(value = "删除说说")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除说说", code = "deleteTalk")
    public Result deleteTalk(@RequestParam Long talkId) {
        talkService.deleteTalk(talkId);
        return Result.success();
    }
}
