package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.TalkDto;
import com.github.stazxr.zblog.domain.dto.query.TalkQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.TalkService;
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
public class TalkController {
    private final TalkService talkService;

    /**
     * 分页查询说说列表
     *
     * @param queryDto 查询参数
     * @return TalkVoList
     */
    @GetMapping(value = "/pageList")
    @Router(name = "分页查询说说列表", code = "queryTalkListByPage")
    public Result queryTalkListByPage(TalkQueryDto queryDto) {
        return Result.success().data(talkService.queryTalkListByPage(queryDto));
    }

    /**
     * 查询说说详情
     *
     * @param talkId 说说ID
     * @return TalkVo
     */
    @GetMapping(value = "/queryTalkDetail")
    @Router(name = "查询说说详情", code = "queryTalkDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryTalkDetail(Long talkId) {
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
    @Router(name = "删除说说", code = "deleteTalk")
    public Result deleteTalk(@RequestPostSingleParam Long talkId) {
        talkService.deleteTalk(talkId);
        return Result.success();
    }
}
