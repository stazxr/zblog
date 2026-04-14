package com.github.stazxr.zblog.content.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.content.domain.dto.TagDto;
import com.github.stazxr.zblog.content.domain.dto.query.TagQueryDto;
import com.github.stazxr.zblog.content.domain.vo.TagVo;
import com.github.stazxr.zblog.content.service.TagService;
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
 * 标签管理
 *
 * @author SunTao
 * @since 2020-11-19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
@Api(value = "TagController", tags = { "标签管理" })
public class TagController {
    private final TagService tagService;

    /**
     * 分页查询标签列表
     *
     * @param queryDto 查询参数
     * @return IPage<TagVo>
     */
    @GetMapping(value = "/pageList")
    @ApiOperation(value = "分页查询标签列表")
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "分页查询标签列表", code = "TAGSQ001")
    public IPage<TagVo> queryTagListByPage(TagQueryDto queryDto) {
        return tagService.queryTagListByPage(queryDto);
    }

    /**
     * 查询标签详情
     *
     * @param tagId 标签id
     * @return TagVo
     */
    @GetMapping(value = "/queryTagDetail")
    @ApiOperation(value = "查询标签详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "tagId", value = "标签id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "查询标签详情", code = "TAGSQ002")
    public TagVo queryTagDetail(@RequestParam Long tagId) {
        return tagService.queryTagDetail(tagId);
    }

    /**
     * 新增标签
     *
     * @param tagDto 标签信息
     */
    @Log
    @PostMapping(value = "/addTag")
    @ApiOperation(value = "新增标签")
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "新增标签", code = "TAGSA001")
    public void addTag(@RequestBody @Validated(Create.class) TagDto tagDto) {
        tagService.addTag(tagDto);
    }

    /**
     * 编辑标签
     *
     * @param tagDto 标签信息
     */
    @Log
    @PostMapping(value = "/editTag")
    @ApiOperation(value = "编辑标签")
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "编辑标签", code = "TAGSU001")
    public void editTag(@RequestBody @Validated(Update.class) TagDto tagDto) {
        tagService.editTag(tagDto);
    }

    /**
     * 删除标签
     *
     * @param tagId 标签ID
     */
    @Log
    @PostMapping(value = "/deleteTag")
    @ApiOperation(value = "删除标签")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "tagId", value = "标签id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(value = BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "删除标签", code = "TAGSD001")
    public void deleteTag(@RequestParam Long tagId) {
        tagService.deleteTag(tagId);
    }
}
