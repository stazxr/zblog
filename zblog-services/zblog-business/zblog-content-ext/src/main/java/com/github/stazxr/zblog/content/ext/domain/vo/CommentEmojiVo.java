package com.github.stazxr.zblog.content.ext.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 评论表情信息
 *
 * @author suntao
 * @since 2026-09-01
 */
@Getter
@Setter
@ApiModel("评论表情VO")
public class CommentEmojiVo implements Serializable {
    private static final long serialVersionUID = 4868571927848960136L;

    /**
     * 表情id
     */
    @ApiModelProperty("表情id")
    private Long id;

    /**
     * 表情名称
     */
    @ApiModelProperty("表情名称")
    private String name;

    /**
     * 表情编码
     */
    @ApiModelProperty("表情编码")
    private String code;

    /**
     * 表情图片路径
     */
    @ApiModelProperty("表情图片路径")
    private String url;
}
