package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件与业务绑定关系表
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Getter
@Setter
@TableName("file_relation")
public class FileRelation extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 业务ID
     */
    private Long businessId;
}
