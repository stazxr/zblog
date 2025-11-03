package com.github.stazxr.zblog.bas.router;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.validation.Assert;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Resource 类表示一个 API 路由的元数据，包含q、名称、代码和权限等级等信息。
 *
 * 这些信息可以用于权限控制、API 文档生成、路由注册等用途
 *
 * @author SunTao
 * @since 2024-11-24
 * @version 5.0
 */
@Getter
@Setter
public class Resource implements Serializable {
    private static final long serialVersionUID = -3231561995447372882L;

    /**
     * 允许配置的路由级别
     */
    private static final List<Integer> ALLOWS_LEVELS = Arrays.asList(
        RouterLevel.OPEN,
        RouterLevel.PUBLIC,
        RouterLevel.PERM,
        RouterExtLevel.FORBIDDEN
    );

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源请求地址
     */
    private String resourceUri;

    /**
     * 资源请求方式
     */
    private String resourceMethod;

    /**
     * 资源权限编码
     *
     * 表示访问资源需要的权限
     * @see com.github.stazxr.zblog.bas.router.Router#code()
     */
    private String resourceCode;

    /**
     * 资源访问等级
     *
     * 表示资源的默认访问等级
     * @see com.github.stazxr.zblog.bas.router.Router#level()
     */
    private int resourceLevel;

    public void setResourceLevel(int resourceLevel) {
        Assert.isTrue(!ALLOWS_LEVELS.contains(resourceLevel), ExpMessageCode.of("valid.resource.resourceLevel.configError"));
        this.resourceLevel = resourceLevel;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
