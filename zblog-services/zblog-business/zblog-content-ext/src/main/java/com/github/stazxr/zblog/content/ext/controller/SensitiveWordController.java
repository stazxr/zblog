package com.github.stazxr.zblog.content.ext.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.content.ext.domain.dto.SensitiveWordDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.SensitiveWordQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.SensitiveWordVo;
import com.github.stazxr.zblog.content.ext.service.SensitiveWordService;
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
 * 敏感词管理
 *
 * @author SunTao
 * @since 2026-07-22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensitiveWords")
@Api(value = "SensitiveWordController", tags = "敏感词管理")
public class SensitiveWordController {
    private final SensitiveWordService sensitiveWordService;

    /**
     * 分页查询敏感词列表
     *
     * @param queryDto 查询参数
     * @return IPage<SensitiveWordVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询敏感词列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询敏感词列表", code = "SEWDQ001")
    public IPage<SensitiveWordVo> querySensitiveWordListByPage(SensitiveWordQueryDto queryDto) {
        return sensitiveWordService.querySensitiveWordListByPage(queryDto);
    }

    /**
     * 新增敏感词
     *
     * @param sensitiveWordDto 敏感词信息
     */
    @Log
    @PostMapping(value = "/addSensitiveWord")
    @ApiOperation(value = "新增敏感词")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "新增敏感词", code = "SEWDA001")
    public void addSensitiveWord(@RequestBody @Validated(Create.class) SensitiveWordDto sensitiveWordDto) {
        sensitiveWordService.addSensitiveWord(sensitiveWordDto);
    }

    /**
     * 编辑敏感词
     *
     * @param sensitiveWordDto 敏感词信息
     */
    @Log
    @PostMapping(value = "/editSensitiveWord")
    @ApiOperation(value = "编辑敏感词")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "编辑敏感词", code = "SEWDU001")
    public void editSensitiveWord(@RequestBody @Validated(Update.class) SensitiveWordDto sensitiveWordDto) {
        sensitiveWordService.editSensitiveWord(sensitiveWordDto);
    }

    /**
     * 删除敏感词
     *
     * @param sensitiveWordId 敏感词id
     */
    @Log
    @PostMapping(value = "/deleteSensitiveWord")
    @ApiOperation(value = "删除敏感词")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sensitiveWordId", value = "敏感词id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除敏感词", code = "SEWDD001")
    public void deleteSensitiveWord(@RequestParam Long sensitiveWordId) {
        sensitiveWordService.deleteSensitiveWord(sensitiveWordId);
    }
}
