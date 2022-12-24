package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 相册信息
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Getter
@Setter
@ToString
public class AlbumDto {
    /**
     * 主键
     */
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
}
