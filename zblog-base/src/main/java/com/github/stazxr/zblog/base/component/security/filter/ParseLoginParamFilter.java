package com.github.stazxr.zblog.base.component.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stazxr.zblog.base.component.security.config.CustomWebSecurityConfiguration;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.secret.RsaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
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
 * 设置登录参数
 *
 * @author SunTao
 * @since 2022-06-15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ParseLoginParamFilter extends OncePerRequestFilter {
    private static final String REMEMBER_ME_PARAMETER = "rememberMe";

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (CustomWebSecurityConfiguration.LOGIN_PROCESSING_URL.equals(request.getRequestURI())
                && HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod())) {
            // 如果是登录请求，将参数存储到请求中
            if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                try {
                    // get param
                    ObjectMapper mapper = new ObjectMapper();
                    ServletInputStream inputStream = request.getInputStream();
                    JSONObject param = mapper.readValue(inputStream, JSONObject.class);

                    // username
                    String username = param.getString(SPRING_SECURITY_FORM_USERNAME_KEY);
                    username = (username != null) ? username : "";
                    username = username.trim();
                    request.setAttribute(SPRING_SECURITY_FORM_USERNAME_KEY, username);

                    // password
                    String password = param.getString(SPRING_SECURITY_FORM_PASSWORD_KEY);
                    password = (password != null) ? password : "";

                    // 对密码进行解密
                    Resource resource = new ClassPathResource("pri.key");
                    String priKeyBase64 = FileUtils.readFileFromStream(resource.getInputStream());
                    password = RsaUtils.decryptByPrivateKey(priKeyBase64, password);
                    request.setAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY, password);

                    // remember me
                    String rememberMe = param.getString(REMEMBER_ME_PARAMETER);
                    if (StringUtils.isNotBlank(rememberMe)) {
                        request.setAttribute(AbstractRememberMeServices.DEFAULT_PARAMETER, rememberMe);
                    }

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

                    log.info("user {} login info: {}", username, param);
                } catch (Exception e) {
                    log.error("read login parameter error", e);
                    throw new AuthenticationServiceException("读取登录参数失败");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
