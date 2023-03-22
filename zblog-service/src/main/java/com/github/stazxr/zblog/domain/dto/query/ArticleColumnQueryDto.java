package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 专栏查询参数
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ToString
@ApiModel("专栏查询参数")
public class ArticleColumnQueryDto extends PageParam {
    /**
     * 专栏名称
     */
    @ApiModelProperty("专栏名称")
    private String name;

    /**
     * 专栏状态
     */
    @ApiModelProperty("专栏状态")
    private Boolean enabled;
}
