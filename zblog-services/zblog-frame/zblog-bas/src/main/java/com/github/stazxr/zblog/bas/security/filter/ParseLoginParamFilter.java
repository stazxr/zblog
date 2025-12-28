package com.github.stazxr.zblog.bas.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stazxr.zblog.bas.encryption.util.RsaUtils;
import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.authn.userpass.numcode.ValidateLoginCodeFilter;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

/**
 * 登录参数解析
 *
 * @author SunTao
 * @since 2022-06-15
 */
@Slf4j
@Component
public class ParseLoginParamFilter extends OncePerRequestFilter {
    private SecurityExtProperties securityExtProperties;

    @Value("${zblog.security.PrivateKey}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (securityExtProperties.getLoginUrl().equals(request.getRequestURI()) && HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod())) {
            // 如果是登录请求，将参数存储到请求中
            if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                try {
                    // get param
                    ObjectMapper mapper = new ObjectMapper();
                    ServletInputStream inputStream = request.getInputStream();
                    JSONObject param = mapper.readValue(inputStream, JSONObject.class);

                    // username
                    String username = param.getString(SPRING_SECURITY_FORM_USERNAME_KEY);
                    request.setAttribute(SPRING_SECURITY_FORM_USERNAME_KEY, username);

                    // password
                    String password = param.getString(SPRING_SECURITY_FORM_PASSWORD_KEY);
                    password = RsaUtils.decryptByPrivateKey(secretKey, password);
                    request.setAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY, password);

                    // code
                    String code = param.getString(ValidateLoginCodeFilter.DEFAULT_CODE_KEY);
                    if (StringUtils.isNotBlank(code)) {
                        request.setAttribute(ValidateLoginCodeFilter.DEFAULT_CODE_KEY, code);
                    }

                    // uuid
                    String uuid = param.getString(ValidateLoginCodeFilter.DEFAULT_CACHE_KEY);
                    if (StringUtils.isNotBlank(uuid)) {
                        request.setAttribute(ValidateLoginCodeFilter.DEFAULT_CACHE_KEY, uuid);
                    }
                } catch (Exception e) {
                    log.error("解析用户登录参数失败", e);
                    throw new AuthenticationServiceException("解析用户登录参数失败");
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Autowired
    public void setSecurityExtProperties(SecurityExtProperties securityExtProperties) {
        this.securityExtProperties = securityExtProperties;
    }
}
