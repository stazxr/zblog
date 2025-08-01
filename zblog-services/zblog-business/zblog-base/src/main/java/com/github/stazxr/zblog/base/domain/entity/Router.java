package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.util.Assert;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * 系统路由
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Getter
@Setter
@TableName("router")
public class Router extends BaseEntity {
    private static final long serialVersionUID = -3970834842294808278L;

    /**
     * 允许配置的路由级别
     */
    private static final List<Integer> LEVELS = Arrays.asList(
            RouterLevel.OPEN,
            RouterLevel.PUBLIC,
            RouterLevel.PERM
    );

    /**
     * 主键
     */
    @TableId
    private Long id;

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
     * {@link RouterLevel}
     */
    private Integer defaultLevel;

    /**
     * 路由备注
     */
    private String remark;

    public Router() {
    }

    /**
     * 通过 {@link com.github.stazxr.zblog.bas.router.Router} 构造Route对象
     *
     * @param router 路由信息
     */
    public Router(com.github.stazxr.zblog.bas.router.Router router) {
        Assert.notNull(router, "router must not be null");
        Assert.isTrue(!LEVELS.contains(router.level()), "router level is out of range: " + LEVELS);

        name = router.name();
        code = router.code();
        defaultLevel = router.level();
    }
}
