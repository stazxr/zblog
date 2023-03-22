package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 相册查询参数
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Getter
@Setter
@ToString
@ApiModel("相册查询参数")
public class AlbumQueryDto extends PageParam {
    /**
     * 当前登录用户
     */
    @ApiModelProperty("当前登录用户")
    private String loginUser;

    /**
     * 当前登录用户id
     */
    @ApiModelProperty("当前登录用户id")
    private String loginUserId;

    /**
     * 相册名称
     */
    @ApiModelProperty("相册名称")
    private String albumName;

    /**
     * 相册状态: {@link com.github.stazxr.zblog.domain.enums.AlbumStatus}
     */
    @ApiModelProperty("相册状态，1：公开、2：私密")
    private Integer status;
}
