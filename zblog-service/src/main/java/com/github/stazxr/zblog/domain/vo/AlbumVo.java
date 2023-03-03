package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 相册页面展示信息
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Getter
@Setter
public class AlbumVo extends BaseVo {
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

    /**
     * 照片数（未删除）
     */
    private int photoCount;

    /**
     * 作者
     */
    private Long userId;

    /**
     * 作者昵称
     */
    private String userNickname;
}
