package com.github.stazxr.zblog.base.domain.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

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
     * 权限级别（依permission刷新）
     * {@link com.github.stazxr.zblog.core.base.BaseConst.PermLevel}
     */
    private Integer level;

    /**
     * 路由链接
     */
    private String url;

    /**
     * 路由请求方式
     */
    private String method;

    /**
     * 路由状态（依permission刷新）
     * {@link com.github.stazxr.zblog.base.util.Constants.RouterStatus}
     */
    private String status;

    /**
     * 路由备注
     */
    private String remark;

    public Router() {
    }

    /**
     * 通过 {@link com.github.stazxr.zblog.core.annotation.Router} 构造Route对象
     *
     * @param router 路由信息
     */
    public Router(com.github.stazxr.zblog.core.annotation.Router router) {
        name = router.name();
        code = router.code();
        level = router.level();
        remark = router.remark();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
