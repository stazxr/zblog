package com.github.stazxr.zblog.bas.security.authz;

import com.github.stazxr.zblog.bas.security.core.SecurityRole;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.core.UserType;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CustomAccessDecisionManager 是一个自定义的 {@link AccessDecisionManager} 实现，用于根据当前请求的 URL 对应权限列表和
 * 用户的角色信息，判断用户是否具有访问权限。
 *
 * <p>该类支持多种决策策略，主要根据用户是否具有目标资源的访问权限角色进行决策。系统内置管理员用户始终拥有所有权限，
 * 而测试用户禁止进行增删改操作。</p>
 *
 * @see AccessDecisionManager
 * @see CustomSecurityMetadataSource
 * @see ConfigAttribute
 *
 * @author SunTao
 * @since 2024-11-13
 */
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {
    /**
     * 权限决策方法，根据请求的角色列表和用户的角色信息决定是否允许访问。
     * <p>决策规则：</p>
     * <ul>
     * <li>若为系统管理员用户，则直接放行。</li>
     * <li>若为测试用户，且资源含 NO_TEST 标记，禁止访问。</li>
     * <li>若资源角色包含 NULL、NONE、FORBIDDEN 等标记，则根据标记拒绝访问。</li>
     * <li>若资源角色包含 PUBLIC 且用户为匿名，要求用户登录。</li>
     * <li>若资源角色包含 OPEN，直接放行。</li>
     * <li>其他情况下，用户只需拥有目标资源中任意一个角色即可访问。</li>
     * </ul>
     *
     * @param authentication 当前用户的身份信息，包含用户角色等信息
     * @param object         包含请求信息的对象
     * @param configAttributes 资源所需的角色列表 see {@link CustomSecurityMetadataSource#getAttributes}
     * @throws AccessDeniedException 如果用户没有权限访问资源
     * @throws InsufficientAuthenticationException 如果用户身份验证不足
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        // 获取登录用户类型和用户角色列表
        Integer userType = null;
        Collection<? extends SecurityRole> userRoles = new ArrayList<>();
        if (authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            userType = securityUser.getUserType();
            userRoles = securityUser.getAuthorities();
        }

        // 系统内置管理员拥有全部权限
        if (UserType.ADMIN_USER.getType().equals(userType)) {
            return;
        }

        // 获取允许访问的角色列表
        List<String> allowRoles = configAttributes.stream().map(ConfigAttribute::getAttribute).collect(Collectors.toList());

        // 禁止测试用户执行增删改操作
        if (UserType.TEST_USER.getType().equals(userType) && allowRoles.contains(DefaultRoleCode.NO_TEST)) {
            throw new AccessDeniedException("测试用户被禁止执行该操作，请使用其他用户操作");
        }

        // 特定角色判断
        if (allowRoles.contains(DefaultRoleCode.NULL)) {
            throw new AccessDeniedException("访问资源不存在");
        } else if (allowRoles.contains(DefaultRoleCode.NONE)) {
            throw new AccessDeniedException("没有权限");
        } else if (allowRoles.contains(DefaultRoleCode.FORBIDDEN)) {
            throw new AccessDeniedException("资源被禁止访问");
        } else if (allowRoles.contains(DefaultRoleCode.PUBLIC)) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                throw new BadCredentialsException("未登录");
            } else {
                return;
            }
        } else if (allowRoles.contains(DefaultRoleCode.OPEN)) {
            return;
        } else {
            // 用户角色校验，至少拥有目标资源中的一个角色
            for (String roleCode : allowRoles) {
                for (SecurityRole userRole : userRoles) {
                    if (userRole.isEnabled() && userRole.getAuthority().equalsIgnoreCase(roleCode)) {
                        return;
                    }
                }
            }
        }

        throw new AccessDeniedException("权限不足，请联系管理员");
    }

    /**
     * 确定当前 {@link AccessDecisionManager} 是否能够处理给定的 {@link ConfigAttribute}。
     *
     * @param attribute 资源所需的角色
     * @return 返回 {@code true} 表示支持
     */
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    /**
     * 确定此 AccessDecisionManager 是否支持提供的类类型。
     *
     * @param clazz 待支持的安全对象类型
     * @return 返回 {@code true} 表示支持
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
