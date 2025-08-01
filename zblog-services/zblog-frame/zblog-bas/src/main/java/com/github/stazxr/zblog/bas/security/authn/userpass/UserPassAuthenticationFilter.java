package com.github.stazxr.zblog.bas.security.authn.userpass;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义用户密码认证过滤器，用于处理 JSON 格式的用户名和密码认证请求。
 * <p>
 * 该过滤器扩展自 {@link UsernamePasswordAuthenticationFilter}，以支持通过 JSON 传递用户名和密码的方式。
 * 当请求内容类型为 JSON 时，提取并认证请求体中的用户名和密码。
 * 否则，使用默认的表单方式处理。
 * </p>
 *
 * @author SunTao
 * @since 2024-11-11
 */
public class UserPassAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /**
     * 尝试进行认证处理。此方法首先检查请求方法是否为 POST，并处理两种请求内容类型：
     * - 如果请求为 JSON 格式，则解析 JSON 内容以提取用户名和密码。
     * - 如果请求为表单提交，则使用默认表单处理机制。
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @return 认证结果的 {@link Authentication} 对象
     * @throws AuthenticationException 当认证失败时抛出
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 检查请求方法是否为 POST，否则抛出异常
        if (!HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // JSON 格式请求处理
        if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            String username = (String) request.getAttribute(SPRING_SECURITY_FORM_USERNAME_KEY);
            String password = (String) request.getAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            // Allow subclasses to set the "details" property
            setDetails(request, authenticationToken);
            return getAuthenticationManager().authenticate(authenticationToken);
        }

        // 其他内容类型使用默认表单方式处理
        return super.attemptAuthentication(request, response);
    }
}
