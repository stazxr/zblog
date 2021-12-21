package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.base.domain.enums.Gender;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * 用户，自定义UserDetails作为Security的认证用户
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
@TableName("sys_user")
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {
    /**
     * serialId
     */
    private static final long serialVersionUID = -1950750778826944911L;

    /**
     * 密码有效期
     */
    private static final int PASSWORD_VALID_TIME = 90;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户名（用于登录，第一次用户名根据姓名或邮箱生成，用户名可修改一次）
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Gender gender;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 签名
     */
    private String signature;

    /**
     * 头像 URL
     */
    private String headPortrait;

    /**
     * 上次登录时间
     */
    private String lastLoginTime;

    /**
     * 上次修改密码时间
     */
    private String lastChangePwdTime;

    /**
     * 用户是否启用
     */
    private Boolean active;

    /**
     * 是否需要登陆修改密码
     * 后台创建的用户第一次登录需要修改密码
     */
    private Boolean changePwd;

    /**
     * 是否允许登录, 默认是
     */
    private Boolean allowLogin;

    /**
     * 是否是内置用户, 默认否
     * 内置用户无法删除
     */
    private Boolean buildIn;

    /**
     * 是否已删除（逻辑操作，保护数据）
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 用户对应的角色列表
     */
    @TableField(exist = false)
    private List<Role> roles;

    /**
     * 用户对应的权限列表
     */
    @TableField(exist = false)
    private Set<Permission> permissions;

    /**
     * 用户对应的菜单列表
     */
    @TableField(exist = false)
    private Set<Permission> menus;

    /**
     * 返回授予用户的权限
     *
     * @return GrantedAuthorityList
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles != null) {
            for (Role role : roles) {
                if (role.getActive()) {
                    authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
                }
            }
        }

        return Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    /**
     * 指示用户的帐户是否已过期
     *
     * @return true: 没有过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指示用户是被锁定还是未锁定
     *
     * @return true: 没有锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示用户的凭据（密码）是否已过期，对于过期的不允许身份验证
     * 密码有效期为 PASSWORD_VALID_TIME，单位天数
     * 认证系统会抛出 CredentialsExpiredException
     *
     * @return 正常: false; 已过期: true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        Date expiredDate = new Date(0);
        if (StringUtils.isNotBlank(lastChangePwdTime)) {
            try {
                Date changePwdDate = DateUtils.parse(lastChangePwdTime);
                expiredDate = DateUtils.addDays(changePwdDate, PASSWORD_VALID_TIME);
            } catch (ParseException e) {
                return false;
            }
        }

        return !new Date().after(expiredDate);
    }

    /**
     * 指示用户是启用还是禁用
     *
     * @return 可用: true; 不可用: false
     */
    @Override
    public boolean isEnabled() {
        return active;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", nickname='" + nickname + '\'' +
                ", signature='" + signature + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", lastChangePwdTime='" + lastChangePwdTime + '\'' +
                ", active=" + active +
                ", changePwd=" + changePwd +
                ", allowLogin=" + allowLogin +
                ", buildIn=" + buildIn +
                '}';
    }

    private SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    private static class AuthorityComparator  implements Comparator<GrantedAuthority>, Serializable {
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
}
