package com.github.stazxr.zblog.base.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录认证拦截器，用于拦截登录请求
 *
 * @author SunTao
 * @since 2022-03-25
 */
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String POST_METHOD = "POST";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        if (!POST_METHOD.equalsIgnoreCase(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // JSON
        if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            String username;
            String password;
            try {
                // get param
                ObjectMapper mapper = new ObjectMapper();
                ServletInputStream inputStream = request.getInputStream();
                JSONObject param = mapper.readValue(inputStream, JSONObject.class);

                // username
                username = param.getString(SPRING_SECURITY_FORM_USERNAME_KEY);
                username = (username != null) ? username : "";
                username = username.trim();
                request.setAttribute(SPRING_SECURITY_FORM_USERNAME_KEY, username);

                // password
                password = param.getString(SPRING_SECURITY_FORM_PASSWORD_KEY);
                password = (password != null) ? password : "";
                request.setAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY, password);
            } catch (Exception e) {
                logger.error("read username and password parameter error", e);
                throw new AuthenticationServiceException("读取登录参数异常");
            }

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }

        // 使用官方默认处理方式
        return super.attemptAuthentication(request, response);
    }
}
