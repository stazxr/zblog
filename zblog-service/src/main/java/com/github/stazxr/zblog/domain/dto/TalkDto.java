package com.github.stazxr.zblog.domain.dto;

import com.github.stazxr.zblog.base.domain.entity.File;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("说说信息")
public class TalkDto {
    /**
     * 说说id
     */
    @ApiModelProperty("说说id")
    private Long id;

    /**
     * 说说内容
     */
    @ApiModelProperty("说说内容")
    private String content;

    /**
     * 图片列表
     */
    @ApiModelProperty("图片列表")
    private String images;

    /**
     * 说说状态: {@link com.github.stazxr.zblog.domain.enums.TalkStatus}
     */
    @ApiModelProperty("说说状态，1：公开、2、私密")
    private Integer status;

    /**
     * 是否置顶
     */
    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    /**
     * 图片列表
     */
    @ApiModelProperty("图片列表")
    private List<File> imagesList;
}
