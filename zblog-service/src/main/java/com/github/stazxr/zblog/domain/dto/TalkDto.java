package com.github.stazxr.zblog.domain.dto;

import com.github.stazxr.zblog.base.domain.entity.File;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 说说信息
 *
 * @author SunTao
 * @since 2022-12-12
 */
@Getter
@Setter
@ToString
public class TalkDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 说说内容
     */
    private String content;

    /**
     * 图片列表
     */
    private String images;

    /**
     * 说说状态: {@link com.github.stazxr.zblog.domain.enums.TalkStatus}
     */
    private Integer status;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 图片列表
     */
    private List<File> imagesList;
}
