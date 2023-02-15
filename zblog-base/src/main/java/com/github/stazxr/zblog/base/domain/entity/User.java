package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * 系统用户
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
@TableName("user")
public class User extends BaseEntity implements UserDetails {
    /**
     * 密码有效期, 单位天数，默认一年
     */
    @JsonIgnore
    private static final int PASSWORD_VALID_TIME = 365;

    /**
     * 主键
     */
    @TableId
    @Getter
    @Setter
    private Long id;

    /**
     * 昵称
     */
    @Getter
    @Setter
    private String nickname;

    /**
     * 用户名（用于登录）
     */
    @Getter
    @Setter
    private String username;

    /**
     * 密码
     */
    @Getter
    @Setter
    private String password;

    /**
     * 邮箱
     */
    @Getter
    @Setter
    private String email;

    /**
     * 手机号
     */
    @Getter
    @Setter
    private String telephone;

    /**
     * 企鹅号
     */
    @Getter
    @Setter
    private String qq;

    /**
     * 性别
     */
    @Getter
    @Setter
    private Integer gender;

    /**
     * 签名
     */
    @Getter
    @Setter
    private String signature;

    /**
     * 个人网站
     */
    @Getter
    @Setter
    private String website;

    /**
     * 头像地址
     */
    @Getter
    @Setter
    private String headImgUrl;

    /**
     * 登录时间
     */
    @Getter
    @Setter
    private String loginTime;

    /**
     * 修改密码时间
     */
    @Getter
    @Setter
    private String changePwdTime;

    /**
     * 是否需要登陆修改密码
     * 后台创建的用户第一次登录需要修改密码
     */
    @Getter
    @Setter
    private Boolean changePwd;

    /**
     * 账户是否登录锁定, 登录次数失败太多则锁定
     */
    @Getter
    @Setter
    private Boolean locked;

    /**
     * 是否是临时账户
     */
    @Getter
    @Setter
    private Boolean temp;

    /**
     * 如果是临时账户，需要注明过期时间
     * 格式：{@link DateUtils#defaultPattern}
     */
    @Getter
    @Setter
    private String expiredTime;

    /**
     * 是否是系统管理员
     */
    @Getter
    @Setter
    private Boolean admin;

    /**
     * 是否是内置用户, 默认否, 内置用户无法删除
     */
    @Getter
    @Setter
    private Boolean buildIn;

    /**
     * 用户是否启用
     */
    @Setter
    private Boolean enabled;

    /**
     * 是否有效
     */
    @Getter
    @Setter
    @TableLogic
    private Boolean deleted;

    /**
     * 角色列表
     */
    @Setter
    @TableField(exist = false)
    List<Role> authorities;

    /**
     * 权限列表
     */
    @Getter
    @Setter
    @TableField(exist = false)
    Set<String> perms;

    /**
     * 账户是否未过期，过期无法验证
     *
     * @return true: 没有过期
     */
    @Override
    public boolean isAccountNonExpired() {
        if (temp) {
            try {
                return new Date().after(DateUtils.parse(expiredTime));
            } catch (ParseException e) {
                log.error("check account {} is non expired catch eor", username, e);
                return false;
            }
        }
        return true;
    }

    /**
     * 指定用户是否解锁，锁定的用户无法进行身份验证
     *
     * @return true: 没有锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    /**
     * 指示用户的凭据（密码）是否未过期，对于过期的不允许身份验证
     * 密码有效期为 {@link #PASSWORD_VALID_TIME}，认证失败系统会抛出CredentialsExpiredException异常
     *
     * @return true 凭据有效
     */
    @Override
    public boolean isCredentialsNonExpired() {
        if (admin || Constants.USER_TEST.equals(username)) {
            // admin / test not check this.
            return true;
        }

        try {
            String lastChangePwdTime = StringUtils.isNotBlank(changePwdTime) ? changePwdTime : super.getCreateTime();
            Date expiredDate = DateUtils.addDays(DateUtils.parse(lastChangePwdTime), PASSWORD_VALID_TIME);
            // 已过期
            return !new Date().after(expiredDate);
        } catch (ParseException e) {
            log.error("check account {} is credentials non expired catch eor", username, e);
            return false;
        }
    }

    /**
     * 指示用户是否启用，禁用的用户不能身份验证
     *
     * @return true 启用
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 返回用户的授权信息
     *
     * @return GrantedAuthorityList
     */
    @Override
    public Collection<? extends Role> getAuthorities() {
        List<Role> enabledAuthorities = new ArrayList<>();
        if (authorities == null) {
            return enabledAuthorities;
        }

        // 封装角色列表
        authorities.forEach(role -> {
            if (role.getEnabled()) {
                // 校验角色状态，达成实时判断用户当前角色的功能
                enabledAuthorities.add(role);
            }
        });

        // return
        return Collections.unmodifiableSet(sortAuthorities(enabledAuthorities));
    }

    private SortedSet<Role> sortAuthorities(List<Role> authorities) {
        SortedSet<Role> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
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
        return !StringUtils.isEmpty(username) && username.equalsIgnoreCase(other.getUsername());
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        private static final long serialVersionUID = 4921529298932942546L;

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            }
            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

    @Override
    public String toString() {
        // 密码脱敏
        // setPassword("[PROTECT]");
        return super.toString();
    }
}
