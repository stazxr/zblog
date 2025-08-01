package com.github.stazxr.zblog.bas.security.core;

import com.github.stazxr.zblog.bas.context.util.SpringContextUtil;
import com.github.stazxr.zblog.bas.mask.MaskType;
import com.github.stazxr.zblog.bas.mask.core.FieldMask;
import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

/**
 * SecurityUser 类实现了 Spring Security 的 UserDetails 接口，表示认证系统中的用户信息。
 * <p>
 * 此类包含用户的基本信息（如用户名、密码、用户类型和状态），以及用户的权限角色列表。
 * 同时，实现了用户账户和凭据的状态检查。
 * </p>
 * <p>
 * 使用 {@code @FieldMask} 注解来对敏感字段（如密码）进行掩码处理。
 * </p>
 *
 * @author SunTao
 * @since 2024-11-10
 */
@Slf4j
public class SecurityUser implements UserDetails {
    private static final long serialVersionUID = -7721668082752933835L;

    /**
     * 用户唯一标识
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @FieldMask(type = MaskType.PASSWORD)
    private String password;

    /**
     * 用户类型
     *
     * @see com.github.stazxr.zblog.bas.security.core.UserType
     */
    private Integer userType;

    /**
     * 用户状态
     *
     * @see com.github.stazxr.zblog.bas.security.core.UserStatus
     */
    private Integer userStatus;

    /**
     * 如果是临时账户，需要注明过期时间
     * 格式：{@link DateUtils#defaultPattern}
     */
    private String expiredTime;

    /**
     * 用户修改密码时间
     */
    private String changePwdTime;

    /**
     * 角色列表
     */
    private List<? extends SecurityRole> authorities;

    /**
     * 权限列表
     */
    private List<String> perms;

    /**
     * 获取用户唯一标识
     *
     * @return 用户唯一标识
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置用户唯一标识
     *
     * @param id 用户唯一标识
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取用户密码
     *
     * @return 用户密码
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户密码
     *
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取用户类型
     *
     * @return 用户类型
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * 设置用户类型
     *
     * @param userType 用户类型
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    /**
     * 获取用户状态
     *
     * @return 用户状态
     */
    public Integer getUserStatus() {
        return userStatus;
    }

    /**
     * 设置用户状态
     *
     * @param userStatus 用户状态
     */
    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * 获取用户过期时间
     *
     * @return 用户过期时间
     */
    public String getExpiredTime() {
        return expiredTime;
    }

    /**
     * 设置用户过期时间
     *
     * @param expiredTime 用户过期时间
     */
    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    /**
     * 获取用户密码修改时间
     *
     * @return 用户密码修改时间
     */
    public String getChangePwdTime() {
        return changePwdTime;
    }

    /**
     * 设置用户密码修改时间
     *
     * @param changePwdTime 用户密码修改时间
     */
    public void setChangePwdTime(String changePwdTime) {
        this.changePwdTime = changePwdTime;
    }

    /**
     * 获取用户的角色列表
     *
     * @return 用户的角色列表
     */
    @Override
    public List<? extends SecurityRole> getAuthorities() {
        return authorities;
    }

    /**
     * 设置用户的角色列表
     *
     * @param authorities 用户的角色列表
     */
    public void setAuthorities(List<? extends SecurityRole> authorities) {
        this.authorities = authorities;
    }

    /**
     * 获取用户的权限列表
     *
     * @return 用户的权限列表
     */
    public List<String> getPerms() {
        return perms;
    }

    /**
     * 设置用户的权限列表
     *
     * @param perms 用户的权限列表
     */
    public void setPerms(List<String> perms) {
        this.perms = perms;
    }

    /**
     * 检查账户是否未过期
     *
     * @return 如果账户未过期，则返回 true；否则返回 false
     */
    @Override
    public boolean isAccountNonExpired() {
        if (UserType.TEMP_USER.getType().equals(getUserType())) {
            try {
                if (StringUtils.isNotBlank(expiredTime)) {
                    return new Date().after(DateUtils.parse(expiredTime));
                }
            } catch (Exception e) {
                log.error("check account {} is non expired catch eor", username, e);
            }
            return false;
        }
        return true;
    }

    /**
     * 检查账户是否未被锁定
     *
     * @return 如果账户未锁定，则返回 true；否则返回 false
     */
    @Override
    public boolean isAccountNonLocked() {
        return !UserStatus.LOCKED.getStatus().equals(getUserStatus());
    }

    /**
     * 检查凭据是否未过期
     *
     * @return 如果凭据未过期，则返回 true；否则返回 false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        if (UserType.NORMAL_USER.getType().equals(getUserType())) {
            try {
                if (StringUtils.isNotBlank(changePwdTime)) {
                    // 获取密码有效天数，校验密码是否过期
                    SecurityExtProperties properties = SpringContextUtil.getBean(SecurityExtProperties.class);
                    Date expiredDate = DateUtils.addDays(DateUtils.parse(changePwdTime), properties.getPasswordLimitedDay());
                    return !new Date().after(expiredDate);
                }
            } catch (Exception e) {
                log.error("check account {} is credentials non expired catch eor", username, e);
            }

            // 默认用户密码是过期的
            return false;
        }

        // Do not check credential expiration except for normal users
        return true;
    }

    /**
     * 检查用户账户是否启用
     *
     * @return 如果账户已启用，则返回 true；否则返回 false
     */
    @Override
    public boolean isEnabled() {
        return UserStatus.NORMAL.getStatus().equals(getUserStatus());
    }
}
