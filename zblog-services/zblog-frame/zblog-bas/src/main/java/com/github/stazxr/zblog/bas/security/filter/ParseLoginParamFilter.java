package com.github.stazxr.zblog.bas.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stazxr.zblog.bas.encryption.util.RsaUtils;
import com.github.stazxr.zblog.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
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
 * 登录参数解析过滤器
 *
 * @author SunTao
 * @since 2022-06-15
 */
public class ParseLoginParamFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(ParseLoginParamFilter.class);

    private final String secretKey;

    private final String loginPath;

    private static final String LOGIN_FORM_PARAM = "_l";

    public ParseLoginParamFilter(String secretKey, String loginPath) {
        this.loginPath = loginPath;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(loginPath) && HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod())) {
            // 如果是登录请求，将参数存储到请求中
            if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                try {
                    // get param
                    ObjectMapper mapper = new ObjectMapper();
                    ServletInputStream inputStream = request.getInputStream();
                    JSONObject param = mapper.readValue(inputStream, JSONObject.class);
                    String encryptFormData = param.getString(LOGIN_FORM_PARAM);
                    String jsonStr = RsaUtils.decryptByPrivateKey(secretKey, encryptFormData);
                    JSONObject formData = JSON.parseObject(jsonStr);

                    // username
                    String username = formData.getString(SPRING_SECURITY_FORM_USERNAME_KEY);
                    request.setAttribute(SPRING_SECURITY_FORM_USERNAME_KEY, username);

                    // password
                    String password = formData.getString(SPRING_SECURITY_FORM_PASSWORD_KEY);
                    request.setAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY, password);

                    // code
                    String code = formData.getString(ValidateLoginCodeFilter.DEFAULT_CODE_KEY);
                    if (StringUtils.isNotBlank(code)) {
                        request.setAttribute(ValidateLoginCodeFilter.DEFAULT_CODE_KEY, code);
                    }

                    // uuid
                    String uuid = formData.getString(ValidateLoginCodeFilter.DEFAULT_CACHE_KEY);
                    if (StringUtils.isNotBlank(uuid)) {
                        request.setAttribute(ValidateLoginCodeFilter.DEFAULT_CACHE_KEY, uuid);
                    }
                } catch (Exception e) {
                    log.error("解析用户登录参数失败", e);
                    throw new AuthenticationServiceException("系统异常，请稍后再试");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
