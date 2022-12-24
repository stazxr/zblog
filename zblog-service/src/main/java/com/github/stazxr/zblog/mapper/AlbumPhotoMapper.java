package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.dto.query.AlbumPhotoQueryDto;
import com.github.stazxr.zblog.domain.entity.AlbumPhoto;
import com.github.stazxr.zblog.domain.vo.AlbumPhotoVo;

import java.util.List;

/**
 * 照片数据持久层
 *
 * @author SunTao
 * @since 2022-12-15
 */
public interface AlbumPhotoMapper extends BaseMapper<AlbumPhoto> {
    /**
     * 分页查询照片列表
     *
     * @param queryDto 查询参数
     * @return PhotoVoList
     */
    List<AlbumPhotoVo> selectAlbumPhotoList(AlbumPhotoQueryDto queryDto);

    /**
     * 批量插入照片列表
     *
     * @param photos 批量插入照片列表
     */
    void batchInsert(List<AlbumPhoto> photos);
}
