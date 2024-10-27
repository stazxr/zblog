package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 照片页面展示信息
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Getter
@Setter
public class AlbumPhotoVo extends BaseVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 相册ID
     */
    private Long albumId;

    /**
     * 照片名
     */
    private String photoName;

    /**
     * 照片描述
     */
    private String photoDesc;

    /**
     * 照片地址
     */
    private String photoLink;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 相册名称
     */
    private String albumName;
}
