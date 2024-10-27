package com.github.stazxr.zblog.base.component.security.filter;

import lombok.extern.slf4j.Slf4j;
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
 * 登录认证拦截器，用于拦截登录请求
 *
 * @author SunTao
 * @since 2022-03-25
 */
@Slf4j
public class CustomLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // JSON
        if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            String username = (String) request.getAttribute(SPRING_SECURITY_FORM_USERNAME_KEY);
            String password = (String) request.getAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            // Allow subclasses to set the "details" property
            setDetails(request, authenticationToken);
            return this.getAuthenticationManager().authenticate(authenticationToken);
        }

        // 使用官方默认处理方式
        return super.attemptAuthentication(request, response);
    }
}
