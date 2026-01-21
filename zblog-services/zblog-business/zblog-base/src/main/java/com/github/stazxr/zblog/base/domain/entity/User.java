package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.stazxr.zblog.bas.mask.MaskType;
import com.github.stazxr.zblog.bas.mask.core.FieldMask;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.core.UserStatus;
import com.github.stazxr.zblog.bas.security.core.UserType;
import com.github.stazxr.zblog.core.util.ToStringUtils;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
     * 用户到期时间（临时用户）
     */
    private LocalDateTime expireTime;

    /**
     * 密码修改时间
     */
    private LocalDateTime changePwdTime;

    /**
     * 密码到期时间
     */
    private LocalDateTime passwordExpireTime;

    /**
     * 登录失败次数
     */
    private Integer loginErrorCount;

    /**
     * 用户锁定到期时间
     */
    private LocalDateTime lockedExpireTime;

    /**
     * 成功登录时间
     */
    private LocalDateTime successLoginTime;

    /**
     * 上次登录时间
     */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastLoginTime;

    /**
     * 上次登录地点
     */
    @TableField(exist = false)
    private String lastLoginAddress;

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

    /**
     * 检查账户是否未过期
     *
     * @return 如果账户未过期，则返回 true；否则返回 false
     */
    @Override
    public boolean isAccountNonExpired() {
        // 非临时用户不检查用户是否过期
        if (!UserType.TEMP_USER.getType().equals(getUserType())) {
            return true;
        }
        // 未设置用户过期时间，默认用户已过期
        if (expireTime == null) {
            return false;
        }
        // 用户未过期返回 true，否则返回 false
        return LocalDateTime.now().isBefore(expireTime);
    }

    /**
     * 检查凭据是否未过期
     *
     * @return 如果凭据未过期，则返回 true；否则返回 false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // 测试用户和系统用户不检查密码过期
        if (UserType.TEST_USER.getType().equals(getUserType()) || UserType.SYSTEM_USER.getType().equals(getUserType())) {
            return true;
        }
        // 首次登录或密码未设置，需要重置密码
        if (passwordExpireTime == null) {
            return false;
        }
        // 密码未过期返回 true，否则返回 false
        return LocalDateTime.now().isBefore(passwordExpireTime);
    }

    /**
     * 检查账户是否未被锁定
     *
     * @return 如果账户未锁定，则返回 true；否则返回 false
     */
    @Override
    public boolean isAccountNonLocked() {
        if (UserStatus.LOCKED.getStatus().equals(getUserStatus())) {
            // 用户状态为锁定，继续判断锁定到期时间
            return lockedExpireTime == null || LocalDateTime.now().isAfter(lockedExpireTime);
        }
        // 用户未锁定
        return true;
    }

    /**
     * 检查用户账户是否启用
     *
     * @return 如果账户已启用，则返回 true；否则返回 false
     */
    @Override
    public boolean isEnabled() {
        if (isAccountNonLocked()) {
            // 用户未锁定
            return !UserStatus.FORBID.getStatus().equals(getUserStatus());
        }
        // 用户已锁定
        return true;
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
