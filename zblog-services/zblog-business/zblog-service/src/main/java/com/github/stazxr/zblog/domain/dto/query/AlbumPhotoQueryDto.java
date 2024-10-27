package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 照片查询
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Getter
@Setter
@ToString
@ApiModel("照片查询参数")
public class AlbumPhotoQueryDto extends PageParam {
    /**
     * 相册id
     */
    @ApiModelProperty("相册id")
    private Long albumId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;
}
