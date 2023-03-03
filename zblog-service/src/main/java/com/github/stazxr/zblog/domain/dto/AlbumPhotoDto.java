package com.github.stazxr.zblog.domain.dto;

import com.github.stazxr.zblog.base.domain.entity.File;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 新增相册照片信息
 *
 * @author SunTao
 * @since 2022-12-24
 */
@Getter
@Setter
@ToString
public class AlbumPhotoDto {
    /**
     * 主键
     */
    private Long albumId;

    /**
     * 照片列表
     */
    private List<File> photoList;

    /**
     * 照片ID列表
     */
    private List<Long> photoIdList;
}
