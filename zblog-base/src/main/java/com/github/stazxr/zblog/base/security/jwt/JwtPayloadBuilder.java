package com.github.stazxr.zblog.base.security.jwt;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Payload build
 *
 * @author SunTao
 * @since 2022-01-19
 */
public class JwtPayloadBuilder {
    /**
     * jwt info
     */
    private final Map<String, String> payload = new HashMap<>();

    /**
     * 附加的属性
     */
    private Map<String, String> additional;

    /**
     * 签发者
     */
    private String iss;

    /**
     * 面向的用户
     */
    private String sub;

    /**
     * 接收方
     */
    private String aud;

    /**
     * 签发时间
     */
    private final LocalDateTime iat = LocalDateTime.now();

    /**
     * 过期时间，这个过期时间必须要大于签发时间 {@link #iat}
     **/
    private LocalDateTime exp;

    /**
     * 唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     */
    private final String jti = IdUtil.simpleUUID();

    /**
     * 权限集
     */
    private Set<String> roles = new HashSet<>();

    /**
     * 配置签发者
     *
     * @param iss 签发者
     * @return JwtPayloadBuilder
     */
    public JwtPayloadBuilder iss(String iss) {
        this.iss = iss;
        return this;
    }

    /**
     * 配置面向的用户
     *
     * @param sub 面向的用户
     * @return JwtPayloadBuilder
     */
    public JwtPayloadBuilder sub(String sub) {
        this.sub = sub;
        return this;
    }

    /**
     * 配置接收方
     *
     * @param aud 接收方
     * @return JwtPayloadBuilder
     */
    public JwtPayloadBuilder aud(String aud) {
        this.aud = aud;
        return this;
    }

    public JwtPayloadBuilder expDays(int days) {
        Assert.isTrue(days > 0, "jwt expireDate must after now");
        this.exp = this.iat.plusSeconds(days);
        return this;
    }

    public JwtPayloadBuilder additional(Map<String, String> additional) {
        this.additional = additional;
        return this;
    }

    public JwtPayloadBuilder roles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public String builder() {
        payload.put("iss", this.iss);
        payload.put("sub", this.sub);
        payload.put("aud", this.aud);
        payload.put("exp", this.exp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        payload.put("iat", this.iat.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        payload.put("jti", this.jti);

        if (!CollectionUtils.isEmpty(additional)) {
            payload.putAll(additional);
        }

        payload.put("roles", JSONUtil.toJsonStr(this.roles));
        return JSONUtil.toJsonStr(JSONUtil.parse(payload));
    }
}
