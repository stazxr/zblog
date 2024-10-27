package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 说说
 *
 * @author SunTao
 * @since 2022-12-12
 */
@Getter
@Setter
@TableName("talk")
public class Talk extends BaseEntity {
    /**
     * 主键
     */
    @TableId
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
     * 是否已删除
     */
    @TableLogic
    private Boolean deleted;
}
