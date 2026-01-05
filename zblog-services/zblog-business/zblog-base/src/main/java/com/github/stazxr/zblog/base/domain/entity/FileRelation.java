package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 逻辑文件业务关联
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Getter
@Setter
@TableName("file_relation")
public class FileRelation implements Serializable {
    private static final long serialVersionUID = 3984919053728739970L;

    /**
     * 逻辑文件ID
     */
    private Long fileId;

    /**
     * 业务ID
     */
    private Long businessId;

    /**
     * 业务类型
     */
    private Integer businessType;
}
