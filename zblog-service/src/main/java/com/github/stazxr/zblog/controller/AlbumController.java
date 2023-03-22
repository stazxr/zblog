package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.AlbumDto;
import com.github.stazxr.zblog.domain.dto.AlbumPhotoDto;
import com.github.stazxr.zblog.domain.dto.query.AlbumQueryDto;
import com.github.stazxr.zblog.domain.dto.query.AlbumPhotoQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.AlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 相册管理模块
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/albums")
@Api(value = "AlbumController", tags = { "相册控制器" })
public class AlbumController {
    private final AlbumService albumService;

    /**
     * 分页查询相册列表
     *
     * @param queryDto 查询参数
     * @return PageVoList
     */
    @GetMapping(value = "/pageAlbumList")
    @ApiOperation(value = "分页查询相册列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "分页查询相册列表", code = "pageAlbumList")
    public Result pageAlbumList(AlbumQueryDto queryDto) {
        return Result.success().data(albumService.pageAlbumList(queryDto));
    }

    /**
     * 查询相册详情
     *
     * @param albumId 相册id
     * @return AlbumVo
     */
    @GetMapping(value = "/queryAlbumDetail")
    @ApiOperation(value = "查询相册详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "albumId", value = "相册id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "查询相册详情", code = "queryAlbumDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryAlbumDetail(@RequestParam Long albumId) {
        return Result.success().data(albumService.queryAlbumDetail(albumId));
    }

    /**
     * 新增或编辑相册
     *
     * @param albumDto 相册信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addOrEditAlbum")
    @ApiOperation(value = "新增或编辑相册")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "新增或编辑相册", code = "addOrEditAlbum")
    public Result addOrEditAlbum(@RequestBody AlbumDto albumDto) {
        albumService.addOrEditAlbum(albumDto);
        return Result.success();
    }

    /**
     * 删除相册
     *
     * @param albumId 相册ID
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteAlbum")
    @ApiOperation(value = "删除相册")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "albumId", value = "相册id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = " 删除相册", code = "deleteAlbum")
    public Result deleteAlbum(@RequestParam Long albumId) {
        albumService.deleteAlbum(albumId);
        return Result.success();
    }

    /**
     * 分页查询照片列表
     *
     * @param queryDto 查询参数
     * @return PhotoVoList
     */
    @GetMapping(value = "/pagePhotoList")
    @ApiOperation(value = "分页查询照片列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "分页查询照片列表", code = "pagePhotoList")
    public Result pagePhotoList(AlbumPhotoQueryDto queryDto) {
        return Result.success().data(albumService.pagePhotoList(queryDto));
    }

    /**
     * 查询用户相册列表
     *
     * @param queryDto 查询参数
     * @return AlbumVoList
     */
    @GetMapping(value = "/queryUserAlbumList")
    @ApiOperation(value = "查询用户相册列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "查询用户相册列表", code = "queryUserAlbumList", level = BaseConst.PermLevel.PUBLIC)
    public Result queryUserAlbumList(AlbumQueryDto queryDto) {
        return Result.success().data(albumService.queryUserAlbumList(queryDto));
    }

    /**
     * 上传相册照片
     *
     * @param albumPhotoDto 相册照片信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/saveAlbumPhoto")
    @ApiOperation(value = "上传相册照片")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "上传相册照片", code = "saveAlbumPhoto")
    public Result saveAlbumPhoto(@RequestBody AlbumPhotoDto albumPhotoDto) {
        albumService.saveAlbumPhoto(albumPhotoDto);
        return Result.success();
    }

    /**
     * 移动相册照片
     *
     * @param albumPhotoDto 相册照片信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/moveAlbumPhoto")
    @ApiOperation(value = "移动相册照片")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "移动相册照片", code = "moveAlbumPhoto")
    public Result moveAlbumPhoto(@RequestBody AlbumPhotoDto albumPhotoDto) {
        albumService.moveAlbumPhoto(albumPhotoDto);
        return Result.success();
    }

    /**
     * 删除相册照片
     *
     * @param albumPhotoDto 相册照片信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteAlbumPhoto")
    @ApiOperation(value = "删除相册照片")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "删除相册照片", code = "deleteAlbumPhoto")
    public Result deleteAlbumPhoto(@RequestBody AlbumPhotoDto albumPhotoDto) {
        albumService.deleteAlbumPhoto(albumPhotoDto);
        return Result.success();
    }

    /**
     * 分页查询回收站照片列表
     *
     * @param queryDto 查询参数
     * @return PhotoVoList
     */
    @GetMapping(value = "/pageDeletePhotoList")
    @ApiOperation(value = "分页查询回收站照片列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "分页查询回收站照片列表", code = "pageDeletePhotoList")
    public Result pageDeletePhotoList(AlbumPhotoQueryDto queryDto) {
        return Result.success().data(albumService.pageDeletePhotoList(queryDto));
    }

    /**
     * 永久删除相册照片
     *
     * @param albumPhotoDto 相册照片信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteAlbumPhotoForever")
    @ApiOperation(value = "永久删除相册照片")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "永久删除相册照片", code = "deleteAlbumPhotoForever")
    public Result deleteAlbumPhotoForever(@RequestBody AlbumPhotoDto albumPhotoDto) {
        albumService.deleteAlbumPhotoForever(albumPhotoDto);
        return Result.success();
    }

    /**
     * 恢复相册照片
     *
     * @param albumPhotoDto 相册照片信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/recoverAlbumPhoto")
    @ApiOperation(value = "恢复相册照片")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_1_0 })
    @Router(name = "恢复相册照片", code = "recoverAlbumPhoto")
    public Result recoverAlbumPhoto(@RequestBody AlbumPhotoDto albumPhotoDto) {
        albumService.recoverAlbumPhoto(albumPhotoDto);
        return Result.success();
    }
}
