package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 相册
 *
 * @author SunTao
 * @since 2022-12-14
 */
@Getter
@Setter
@TableName("album")
public class Album extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 相册名称
     */
    private String albumName;

    /**
     * 相册描述
     */
    private String albumDesc;

    /**
     * 相册封面
     */
    private String albumCover;

    /**
     * 相册状态: {@link com.github.stazxr.zblog.domain.enums.AlbumStatus}
     */
    private Integer status;

    /**
     * 是否已删除（使用逻辑操作，保护数据）
     */
    @TableLogic
    private Boolean deleted;
}
