package com.github.stazxr.zblog.bas.security.authn.userpass.numcode;

import com.github.stazxr.zblog.bas.captcha.handler.CaptchaHandler;
import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.exception.LoginNumCodeException;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码校验过滤器
 *
 * <p>
 * 此过滤器拦截登录请求，验证用户提交的验证码是否正确，以防止恶意登录。继承自 {@link OncePerRequestFilter}，
 * 确保在每个请求中只执行一次。
 * </p>
 *
 * @author SunTao
 * @since 2024-11-18
 */
@Slf4j
@Component
public class ValidateLoginCodeFilter extends OncePerRequestFilter {
    /**
     * 默认验证码字段名称
     */
    public static final String DEFAULT_CODE_KEY = "code";

    /**
     * 默认缓存字段名称
     */
    public static final String DEFAULT_CACHE_KEY = "uuid";

    /**
     * 验证码管理器
     */
    private CaptchaHandler captchaHandler;

    /**
     * 扩展参数
     */
    private SecurityExtProperties securityExtProperties;

    /**
     * 认证失败处理逻辑
     */
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 判断是否为登录请求，并且请求方法为 POST
        if (isLoginRequest(request)) {
            try {
                // 验证验证码
                validateCaptcha(request);
            } catch (AuthenticationException ex) {
                // 验证失败处理
                authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
                return;
            }
        }

        // 验证通过，继续后续过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 判断是否为登录请求
     *
     * @param request 当前请求对象
     * @return true 如果是登录请求，false 否则
     */
    private boolean isLoginRequest(HttpServletRequest request) {
        return securityExtProperties.getLoginUrl().equals(request.getRequestURI()) && HttpMethod.POST.name().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 验证验证码
     *
     * @param request 当前请求对象
     * @throws AuthenticationException 如果验证失败
     */
    private void validateCaptcha(HttpServletRequest request) throws AuthenticationException {
        // 获取验证码缓存的唯一标识
        String uuid = (String) request.getAttribute(DEFAULT_CACHE_KEY);
        if (StringUtils.isEmpty(uuid)) {
            throw new LoginNumCodeException("验证码验证失败，缺失参数 " + DEFAULT_CACHE_KEY + "，请检查前端代码");
        }

        // 获取用户输入的验证码
        String code = (String) request.getAttribute(DEFAULT_CODE_KEY);
        if (StringUtils.isEmpty(code)) {
            throw new LoginNumCodeException("请输入验证码");
        }

        // 获取后台缓存的验证码并校验
        String cacheCode = captchaHandler.getCaptchaText(uuid);
        if (cacheCode == null) {
            throw new LoginNumCodeException("验证码已过期");
        }

        // 验证用户输入的验证码是否正确
        if (!captchaHandler.verifyCaptcha(uuid, code)) {
            throw new LoginNumCodeException("验证码不正确");
        }
    }

    @Autowired
    public void setCaptchaHandler(CaptchaHandler captchaHandler) {
        this.captchaHandler = captchaHandler;
    }

    @Autowired
    public void setSecurityExtProperties(SecurityExtProperties securityExtProperties) {
        this.securityExtProperties = securityExtProperties;
    }

    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
