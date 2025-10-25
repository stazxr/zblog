package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.github.stazxr.zblog.bas.mask.MaskType;
import com.github.stazxr.zblog.bas.mask.core.FieldMask;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.core.util.ToStringUtils;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 系统用户
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Getter
@Setter
@TableName("user")
public class User extends SecurityUser {
    private static final long serialVersionUID = 4174772175234327859L;

    /**
     * 用户id
     */
    @TableId
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    @FieldMask(type = MaskType.EMAIL_WEAK)
    private String email;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 签名
     */
    private String signature;

    /**
     * 个人网站
     */
    private String website;

    /**
     * 头像地址
     */
    private String headImgUrl;

    /**
     * 登录时间（成功）
     */
    private String lastLoginTime;

    /**
     * 是否有效
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 创建用户，存储该实体的创建用户标识。
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间，存储该实体的创建时间。
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    /**
     * 修改用户，存储最后修改该实体的用户标识。
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 修改时间，存储该实体的最后修改时间。
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;

    public void setId(Long id) {
        super.setId(id);
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User other = (User) obj;
        return !StringUtils.isEmpty(getUsername()) && getUsername().equalsIgnoreCase(other.getUsername());
    }

    @Override
    public String toString() {
        return ToStringUtils.buildString(this);
    }
}
