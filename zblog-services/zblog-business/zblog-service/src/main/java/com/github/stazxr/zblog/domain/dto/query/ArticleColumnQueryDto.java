package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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
     * 是否首页展示
     */
    @ApiModelProperty("是否首页展示")
    private Boolean pageShow;

    /**
     * 专栏状态
     */
    @ApiModelProperty("专栏状态")
    private Boolean enabled;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String articleTitle;

    /**
     * 文章id列表
     */
    @ApiModelProperty("文章id列表")
    private List<Long> articleIds;
}
