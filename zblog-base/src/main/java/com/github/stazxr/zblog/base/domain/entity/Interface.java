package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.base.domain.enums.InterfaceType;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统接口
 *
 * @author SunTao
 * @since 2022-06-23
 */
@Getter
@Setter
@TableName("interface")
public class Interface extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 请求地址
     */
    private String uri;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 接口类型
     */
    private Integer type;
}
