package com.github.stazxr.zblog.content.ext.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.content.ext.domain.dto.ThemeDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.ThemeQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemeVo;
import com.github.stazxr.zblog.content.ext.service.ThemeService;
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
 * 主题管理
 *
 * @author SunTao
 * @since 2026-06-12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/themes")
@Api(value = "ThemeController", tags = { "主题管理" })
public class ThemeController {
    private final ThemeService themeService;

    /**
     * 分页查询主题列表
     *
     * @param queryDto 查询参数
     * @return IPage<ThemeVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询主题列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "分页查询主题列表", code = "THEMQ001")
    public IPage<ThemeVo> queryThemeListByPage(ThemeQueryDto queryDto) {
        return themeService.queryThemeListByPage(queryDto);
    }

    /**
     * 查询主题详情
     *
     * @param themeId 主题id
     * @return ThemeVo
     */
    @GetMapping(value = "/queryThemeDetail")
    @ApiOperation(value = "查询主题详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "themeId", value = "主题id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询主题详情", code = "THEMQ002")
    public ThemeVo queryThemeDetail(@RequestParam Long themeId) {
        return themeService.queryThemeDetail(themeId);
    }

    /**
     * 新增主题
     *
     * @param themeDto 主题信息
     */
    @Log
    @PostMapping(value = "/addTheme")
    @ApiOperation(value = "新增主题")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "新增主题", code = "THEMA001")
    public void addTheme(@RequestBody @Validated(Create.class) ThemeDto themeDto) {
        themeService.addTheme(themeDto);
    }

    /**
     * 编辑主题
     *
     * @param themeDto 主题信息
     */
    @Log
    @PostMapping(value = "/editTheme")
    @ApiOperation(value = "编辑主题")
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "编辑主题", code = "THEMU001")
    public void editTheme(@RequestBody @Validated(Update.class) ThemeDto themeDto) {
        themeService.editTheme(themeDto);
    }

    /**
     * 删除主题
     *
     * @param themeId 主题id
     */
    @Log
    @PostMapping(value = "/deleteTheme")
    @ApiOperation(value = "删除主题")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "themeId", value = "主题id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除主题", code = "THEMD001")
    public void deleteTheme(@RequestParam Long themeId) {
        themeService.deleteTheme(themeId);
    }
}
