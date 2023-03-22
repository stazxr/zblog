package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.setting.OtherInfo;
import com.github.stazxr.zblog.domain.dto.setting.SocialInfo;
import com.github.stazxr.zblog.domain.dto.setting.WebInfo;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.WebSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 网站设置
 *
 * @author SunTao
 * @since 2022-12-08
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webSetting")
@Api(value = "WebSettingController", tags = { "网站设置控制器" })
public class WebSettingController {
    private final WebSettingService webSettingService;

    /**
     * 查询网站配置信息
     *
     * @return WebInfo
     */
    @GetMapping(value = "/queryWebInfo")
    @ApiOperation(value = "查询网站配置信息")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询网站配置信息", code = "queryWebInfo")
    public Result queryWebInfo() {
        return Result.success().data(webSettingService.queryWebInfo());
    }

    /**
     * 修改网站配置信息
     *
     * @param webInfo 网站信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/updateWebInfo")
    @ApiOperation(value = "修改网站配置信息")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "修改网站配置信息", code = "updateWebInfo")
    public Result updateWebInfo(@RequestBody WebInfo webInfo) {
        webSettingService.updateWebInfo(webInfo);
        return Result.success();
    }

    /**
     * 查询网站社交信息
     *
     * @return SocialInfo
     */
    @GetMapping(value = "/querySocialInfo")
    @ApiOperation(value = "查询网站社交信息")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询网站社交信息", code = "querySocialInfo")
    public Result querySocialInfo() {
        return Result.success().data(webSettingService.querySocialInfo());
    }

    /**
     * 修改网站社交信息
     *
     * @param socialInfo 社交信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/updateSocialInfo")
    @ApiOperation(value = "修改网站社交信息")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "修改网站社交信息", code = "updateSocialInfo")
    public Result updateSocialInfo(@RequestBody SocialInfo socialInfo) {
        webSettingService.updateSocialInfo(socialInfo);
        return Result.success();
    }

    /**
     * 查询网站其他信息
     *
     * @return OtherInfo
     */
    @GetMapping(value = "/queryOtherInfo")
    @ApiOperation(value = "查询网站其他信息")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询网站其他信息", code = "queryOtherInfo")
    public Result queryOtherInfo() {
        return Result.success().data(webSettingService.queryOtherInfo());
    }

    /**
     * 修改网站其他信息
     *
     * @param otherInfo 其他信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/updateOtherInfo")
    @ApiOperation(value = "修改网站其他信息")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "修改网站其他信息", code = "updateOtherInfo")
    public Result updateOtherInfo(@RequestBody OtherInfo otherInfo) {
        webSettingService.updateOtherInfo(otherInfo);
        return Result.success();
    }
}
