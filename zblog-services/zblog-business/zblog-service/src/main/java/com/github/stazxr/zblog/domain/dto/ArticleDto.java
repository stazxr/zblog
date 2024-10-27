package com.github.stazxr.zblog.domain.dto;

import com.github.stazxr.zblog.base.domain.entity.File;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 文章信息
 *
 * @author SunTao
 * @since 2022-12-03
 */
@Getter
@Setter
@ToString
@ApiModel("文章信息")
public class ArticleDto {
    /**
     * 操作类型：add/edit
     */
    @ApiModelProperty("操作类型，add/edit")
    private String action;

    /**
     * 文章id
     */
    @ApiModelProperty("文章id")
    private Long id;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String title;

    /**
     * 文章概要
     */
    @ApiModelProperty("文章概要")
    private String remark;

    /**
     * 文章内容
     */
    @ApiModelProperty("文章内容")
    private String content;

    /**
     * 文章内容（去除标签）
     */
    @ApiModelProperty("文章内容（去除标签）")
    private String contentMd;

    /**
     * 文章关键字
     */
    @ApiModelProperty("文章关键字")
    private String keywords;

    /**
     * 原文链接（转载或翻译需要填写该字段）
     */
    @ApiModelProperty("原文链接（转载或翻译需要填写该字段）")
    private String reprintLink;

    /**
     * 封面类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleImgType}
     */
    @ApiModelProperty("封面类型，1：单封面、2：多封面、3：默认封面、4：自动生成封面、5：无封面")
    private Integer coverImageType;

    /**
     * 文章类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleType}
     */
    @ApiModelProperty("文章类型，1：原创、2：转载、3：翻译")
    private Integer articleType;

    /**
     * 文章权限: see {@link com.github.stazxr.zblog.domain.enums.ArticlePerm}
     */
    @ApiModelProperty("文章权限：1：全部可见、3：仅我可见")
    private Integer articlePerm;

    /**
     * 文章状态: see {@link com.github.stazxr.zblog.domain.enums.ArticleStatus}
     */
    @ApiModelProperty("文章状态，1：草稿、2：待审核、3：待审核（定时发布）、4：审核不通过、5：已发布、6：临时下线、7：待整改、8：回收站、9：待发布")
    private Integer articleStatus;

    /**
     * 文章分类
     */
    @ApiModelProperty("文章分类")
    private Long categoryId;

    /**
     * 作者
     */
    @ApiModelProperty("作者")
    private Long authorId;

    /**
     * 是否开启评论
     */
    @ApiModelProperty("是否开启评论")
    private Boolean commentFlag;

    /**
     * 文章标签列表
     */
    @ApiModelProperty("文章标签列表")
    private List<String> articleTag;

    /**
     * 文章封面列表
     */
    @ApiModelProperty("文章封面列表")
    private List<File> articleImg;

    /**
     * 编辑器类型
     */
    @ApiModelProperty("编辑器类型")
    private String extend1;

    /**
     * 文章字数
     */
    @ApiModelProperty("文章字数")
    private String extend2;

    /**
     * 自动发布时间
     */
    @ApiModelProperty("自动发布时间")
    private String autoPublishTime;
}
