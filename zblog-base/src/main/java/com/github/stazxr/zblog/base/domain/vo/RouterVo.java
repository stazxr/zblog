package com.github.stazxr.zblog.base.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;

/**
 * 路由综合信息
 *
 * @author SunTao
 * @since 2022-06-24
 */
@Data
@ToString
public class RouterVo {
    /**
     * 路由名称
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 路由默认访问级别
     * {@link com.github.stazxr.zblog.core.base.BaseConst.PermLevel}
     */
    private Integer defaultLevel;

    /**
     * 路由备注
     */
    private String remark;

    /**
     * 权限访问级别
     */
    private Integer permLevel;

    /**
     * 权限编码
     */
    private Long permId;

    /**
     * 权限是否启用
     */
    private Boolean permEnabled;
}
