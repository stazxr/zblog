package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.AlbumDto;
import com.github.stazxr.zblog.domain.dto.AlbumPhotoDto;
import com.github.stazxr.zblog.domain.dto.query.AlbumPhotoQueryDto;
import com.github.stazxr.zblog.domain.dto.query.AlbumQueryDto;
import com.github.stazxr.zblog.domain.entity.Album;
import com.github.stazxr.zblog.domain.vo.AlbumPhotoVo;
import com.github.stazxr.zblog.domain.vo.AlbumVo;

/**
 * 相册服务层
 *
 * @author SunTao
 * @since 2022-12-15
 */
public interface AlbumService extends IService<Album> {
    /**
     * 分页查询相册列表
     *
     * @param queryDto 查询参数
     * @return AlbumVoList
     */
    PageInfo<AlbumVo> pageAlbumList(AlbumQueryDto queryDto);

    /**
     * 查询相册详情
     *
     * @param albumId 相册ID
     * @return AlbumVo
     */
    AlbumVo queryAlbumDetail(Long albumId);

    /**
     * 新增或编辑相册
     *
     * @param albumDto 相册信息
     */
    void addOrEditAlbum(AlbumDto albumDto);

    /**
     * 删除相册
     *
     * @param albumId 相册ID
     */
    void deleteAlbum(Long albumId);

    /**
     * 分页查询照片列表
     *
     * @param queryDto 查询参数
     * @return PhotoVoList
     */
    PageInfo<AlbumPhotoVo> pagePhotoList(AlbumPhotoQueryDto queryDto);

    /**
     * 保存相册照片列表
     *
     * @param albumPhotoDto 相册照片信息
     */
    void saveAlbumPhoto(AlbumPhotoDto albumPhotoDto);
}
