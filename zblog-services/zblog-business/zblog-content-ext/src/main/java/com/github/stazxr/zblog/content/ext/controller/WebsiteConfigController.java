package com.github.stazxr.zblog.content.ext.controller;

import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.content.ext.domain.dto.WebsiteConfigDto;
import com.github.stazxr.zblog.content.ext.domain.vo.WebsiteConfigVo;
import com.github.stazxr.zblog.content.ext.service.WebsiteConfigService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 网站配置
 *
 * @author SunTao
 * @since 2026-08-20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/websiteConfig")
@Api(value = "WebsiteConfigController", tags = { "网站配置" })
public class WebsiteConfigController {
    private final WebsiteConfigService websiteConfigService;

    /**
     * 查询网站配置详情
     *
     * @return WebsiteConfigVo
     */
    @GetMapping(value = "/detail")
    @ApiOperation(value = "查询网站配置详情")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询网站配置详情", code = "WEBCQ001")
    public WebsiteConfigVo queryWebsiteConfigDetail() {
        return websiteConfigService.queryWebsiteConfigDetail();
    }

    /**
     * 编辑网站配置
     *
     * @param websiteConfigDto 网站配置信息
     */
    @Log
    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑网站配置")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "编辑网站配置", code = "WEBCU001")
    public void editWebsiteConfig(@RequestBody @Validated WebsiteConfigDto websiteConfigDto) {
        websiteConfigService.editWebsiteConfig(websiteConfigDto);
    }
}
