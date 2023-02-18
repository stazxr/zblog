package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.core.base.BaseVo;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 说说页面展示信息
 *
 * @author SunTao
 * @since 2022-12-12
 */
@Getter
@Setter
public class TalkVo extends BaseVo {
    /**
     * 多个图片分割的符号
     */
    private static final String IMAGES_SPLIT = ",";

    /**
     * 主键
     */
    private Long id;

    /**
     * 说说内容
     */
    private String content;

    /**
     * 说说内容
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
     * 用户序号
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 图片文件列表
     */
    private List<File> imagesFileList;

    /**
     * 图片地址列表
     */
    private List<String> imagesList;

    /**
     * 点赞数
     */
    private int likeCount;

    /**
     * 评论数
     */
    private int commentCount;

    public List<String> getImagesList() {
        imagesList = new ArrayList<>();

        // 设置图片列表
        if (StringUtils.isNotBlank(this.images)) {
            imagesList.addAll(Arrays.asList(this.images.split(IMAGES_SPLIT)));
        }
        return imagesList;
    }
}
