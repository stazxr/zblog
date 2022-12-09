package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 访客
 *
 * @author SunTao
 * @since 2022-12-09
 */
@Getter
@Setter
@Builder
@TableName("visitor")
public class Visitor extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 访问地址
     */
    private String addressIp;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 浏览器名称
     */
    private String browserName;

    /**
     * 访问省市
     */
    private String province;

    /**
     * 区域编码
     */
    private String areaCode;
}
