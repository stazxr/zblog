package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.domain.dto.AlbumDto;
import com.github.stazxr.zblog.domain.dto.AlbumPhotoDto;
import com.github.stazxr.zblog.domain.dto.query.AlbumQueryDto;
import com.github.stazxr.zblog.domain.dto.query.AlbumPhotoQueryDto;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.service.AlbumService;
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
public class AlbumController {
    private final AlbumService albumService;

    /**
     * 分页查询相册列表
     *
     * @param queryDto 查询参数
     * @return PageVoList
     */
    @GetMapping(value = "/pageAlbumList")
    @Router(name = "分页查询相册列表", code = "pageAlbumList")
    public Result pageAlbumList(AlbumQueryDto queryDto) {
        return Result.success().data(albumService.pageAlbumList(queryDto));
    }

    /**
     * 查询相册详情
     *
     * @param albumId 相册ID
     * @return AlbumVo
     */
    @GetMapping(value = "/queryAlbumDetail")
    @Router(name = "查询相册详情", code = "queryAlbumDetail")
    public Result queryAlbumDetail(Long albumId) {
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
    @Router(name = " 删除相册", code = "deleteAlbum")
    public Result deleteAlbum(@RequestPostSingleParam Long albumId) {
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
    @Router(name = "分页查询照片列表", code = "pagePhotoList")
    public Result pagePhotoList(AlbumPhotoQueryDto queryDto) {
        return Result.success().data(albumService.pagePhotoList(queryDto));
    }

    /**
     * 保存相册照片列表
     *
     * @param albumPhotoDto 相册照片信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/saveAlbumPhoto")
    @Router(name = "保存相册照片列表", code = "saveAlbumPhoto")
    public Result saveAlbumPhoto(@RequestBody AlbumPhotoDto albumPhotoDto) {
        albumService.saveAlbumPhoto(albumPhotoDto);
        return Result.success();
    }
}
