package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.dto.query.AlbumPhotoQueryDto;
import com.github.stazxr.zblog.domain.entity.AlbumPhoto;
import com.github.stazxr.zblog.domain.vo.AlbumPhotoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 照片数据持久层
 *
 * @author SunTao
 * @since 2022-12-15
 */
public interface AlbumPhotoMapper extends BaseMapper<AlbumPhoto> {
    /**
     * 查询照片列表
     *
     * @param queryDto 查询参数
     * @return PhotoVoList
     */
    List<AlbumPhotoVo> selectAlbumPhotoList(AlbumPhotoQueryDto queryDto);

    /**
     * 查询回收站照片列表
     *
     * @param queryDto 查询参数
     * @return PhotoVoList
     */
    List<AlbumPhotoVo> selectDeletePhotoList(AlbumPhotoQueryDto queryDto);

    /**
     * 批量插入照片列表
     *
     * @param photos 批量插入照片列表
     */
    void batchInsert(List<AlbumPhoto> photos);

    /**
     * 删除相册照片
     *
     * @param photoIdList 相册照片ID列表
     */
    void deleteAlbumPhoto(List<Long> photoIdList);

    /**
     * 移动相册照片
     *
     * @param albumId 相册ID
     * @param photoIdList 相册照片ID列表
     */
    void moveAlbumPhoto(@Param("albumId") Long albumId, List<Long> photoIdList);

    /**
     * 恢复相册照片
     *
     * @param photoIdList 相册照片信息
     */
    void recoverAlbumPhoto(List<Long> photoIdList);
}
