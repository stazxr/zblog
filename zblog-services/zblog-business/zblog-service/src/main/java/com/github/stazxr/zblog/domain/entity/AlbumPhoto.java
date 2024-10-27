package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 照片
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Getter
@Setter
@TableName("album_photo")
public class AlbumPhoto extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 相册ID
     */
    private Long albumId;

    /**
     * 图片ID
     */
    private Long fileId;

    /**
     * 照片名称
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
    private Boolean isDeleted;
}
