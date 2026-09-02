package com.github.stazxr.zblog.content.ext.domain.dto;

import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 评论表情信息
 *
 * @author suntao
 * @since 2026-08-31
 */
@Getter
@Setter
@ApiModel("评论表情DTO")
public class CommentEmojiDto extends BaseDto {
    private static final long serialVersionUID = -558001014859778450L;

    /**
     * 表情名称
     */
    @NotNull(message = "{COMMENT_IMAGE_NAME_REQUIRED}")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,5}$", message = "{COMMENT_IMAGE_NAME_INVALID}")
    @ApiModelProperty("表情名称")
    private String name;

    /**
     * 表情图片路径
     */
    @NotNull(message = "{COMMENT_IMAGE_URL_REQUIRED}")
    @Pattern(regexp = "^https?://\\S+$", message = "{COMMENT_IMAGE_URL_INVALID}")
    @ApiModelProperty("表情图片路径")
    private String url;
}
