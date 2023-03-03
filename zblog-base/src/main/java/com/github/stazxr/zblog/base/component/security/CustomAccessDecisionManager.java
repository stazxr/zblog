package com.github.stazxr.zblog.base.component.security;

import com.github.stazxr.zblog.base.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.stazxr.zblog.base.util.Constants.SecurityRole.*;

/**
 * 接受当前请求Url对应的权限列表与当前登录的用户信息，决定用户是否可以访问该Url
 * Security需要用到一个实现了AccessDecisionManager接口的类
 * 类功能：根据当前用户的信息，和目标url涉及到的权限，判断用户是否可以访问
 * 判断规则：用户只要匹配到目标url权限中的一个role就可以访问
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Slf4j
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {
    /**
     * 重写权限决策方法
     *
     *  Security原有实现如下（原为投票决策）
     *  a、AffirmativeBased: 基于肯定的决策器, 用户持有一个同意访问的角色就能通过
     *  b、ConsensusBased: 基于共识的决策器, 用户持有同意的角色数量多于禁止的角色数
     *  c、UnanimousBased: 基于一致的决策器, 用户持有的所有角色都同意访问才能放行
     *
     * @param authentication   UserDetails中循环添加到GrantedAuthority对象中的权限信息集合.
     * @param object           包含客户端发起的请求信息，可转换为
     *                         HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
     * @param configAttributes SecurityMetadataSourceImpl的getAttributes(Object object)方法返回的结果
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if (Constants.USER_ADMIN.equalsIgnoreCase(authentication.getName())) {
            // 系统内置管理员不受权限管控
            return;
        }

        // 获取允许访问的角色列表
        List<String> allowRoles = new ArrayList<>();
        configAttributes.forEach(attribute -> allowRoles.add(attribute.getAttribute()));

        // 测试用户不允许调用 Constants.USER_TEST 标记的资源
        if (Constants.USER_TEST.equalsIgnoreCase(authentication.getName())) {
            if (allowRoles.contains(Constants.SecurityRole.NO_TEST)) {
                throw new AccessDeniedException("测试用户被拒绝执行此操作");
            }
        }

        // 判断是否允许访问资源
        if (allowRoles.contains(NONE)) {
            throw new AccessDeniedException("没有权限");
        } else if (allowRoles.contains(NULL)) {
            throw new AccessDeniedException("请求资源不存在");
        } else if (allowRoles.contains(FORBIDDEN)) {
            throw new AccessDeniedException("请求资源被禁止访问");
        } else if (allowRoles.contains(OPEN)) {
            return;
        } else if (allowRoles.contains(PUBLIC)) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                throw new BadCredentialsException("未登录");
            } else {
                return;
            }
        } else {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (String roleCode : allowRoles) {
                for (GrantedAuthority authority : authorities) {
                    if (authority.getAuthority().equalsIgnoreCase(roleCode)) {
                        // 如果有一个角色拥有访问此角色的权限,则放行
                        return;
                    }
                }
            }
        }

        throw new AccessDeniedException("权限不足，请联系管理员");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        // 以确定AccessDecisionManager是否可以处理传递的ConfigAttribute
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        // 以确保配置的AccessDecisionManager支持安全拦截器将呈现的安全object类型
        return true;
    }
}
