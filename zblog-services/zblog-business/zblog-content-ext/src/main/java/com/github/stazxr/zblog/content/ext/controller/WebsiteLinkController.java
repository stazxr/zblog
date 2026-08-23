package com.github.stazxr.zblog.content.ext.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.content.ext.domain.dto.WebsiteLinkConfigDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.WebsiteLinkQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.WebsiteLinkConfigVo;
import com.github.stazxr.zblog.content.ext.service.WebsiteLinkService;
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
 * 网站链接管理
 *
 * @author SunTao
 * @since 2026-08-23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/websiteLinks")
@Api(value = "WebsiteLinkController", tags = { "网站链接管理" })
public class WebsiteLinkController {
    private final WebsiteLinkService websiteLinkService;

    /**
     * 分页查询网站链接列表
     *
     * @param queryDto 查询参数
     * @return IPage<WebsiteLinkConfigVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询网站链接列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询网站链接列表", code = "WEBLQ001")
    public IPage<WebsiteLinkConfigVo> queryWebsiteLinkListByPage(WebsiteLinkQueryDto queryDto) {
        return websiteLinkService.queryWebsiteLinkListByPage(queryDto);
    }

    /**
     * 查询网站链接详情
     *
     * @param websiteLinkId 网站链接id
     * @return WebsiteLinkConfigVo
     */
    @GetMapping(value = "/queryWebsiteLinkDetail")
    @ApiOperation(value = "查询网站链接详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "websiteLinkId", value = "网站链接id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询网站链接详情", code = "WEBLQ002")
    public WebsiteLinkConfigVo queryWebsiteLinkDetail(@RequestParam Long websiteLinkId) {
        return websiteLinkService.queryWebsiteLinkDetail(websiteLinkId);
    }

    /**
     * 新增网站链接
     *
     * @param websiteLinkConfigDto 网站链接信息
     */
    @Log
    @PostMapping(value = "/addWebsiteLink")
    @ApiOperation(value = "新增网站链接")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "新增网站链接", code = "WEBLA001")
    public void addWebsiteLink(@RequestBody @Validated(Create.class) WebsiteLinkConfigDto websiteLinkConfigDto) {
        websiteLinkService.addWebsiteLink(websiteLinkConfigDto);
    }

    /**
     * 编辑网站链接
     *
     * @param websiteLinkConfigDto 网站链接信息
     */
    @Log
    @PostMapping(value = "/editWebsiteLink")
    @ApiOperation(value = "编辑网站链接")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "编辑网站链接", code = "WEBLU001")
    public void editWebsiteLink(@RequestBody @Validated(Update.class) WebsiteLinkConfigDto websiteLinkConfigDto) {
        websiteLinkService.editWebsiteLink(websiteLinkConfigDto);
    }

    /**
     * 删除网站链接
     *
     * @param websiteLinkId 网站链接id
     */
    @Log
    @PostMapping(value = "/deleteWebsiteLink")
    @ApiOperation(value = "删除网站链接")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "websiteLinkId", value = "网站链接id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除网站链接", code = "WEBLD001")
    public void deleteWebsiteLink(@RequestParam Long websiteLinkId) {
        websiteLinkService.deleteWebsiteLink(websiteLinkId);
    }
}
