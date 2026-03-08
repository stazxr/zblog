package com.github.stazxr.zblog.bas.security.authn.userpass.numcode;

import com.github.stazxr.zblog.bas.captcha.handler.CaptchaHandler;
import com.github.stazxr.zblog.util.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码校验过滤器，此过滤器拦截登录请求，验证用户提交的验证码是否正确，以防止恶意登录。
 *
 * @author SunTao
 * @since 2024-11-18
 */
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
     * 登录地址
     */
    private String loginPath;

    /**
     * 认证失败处理逻辑
     */
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 判断是否为登录请求，并且请求方法为 POST
        if (!isLoginRequest(request)) {
            // 验证通过，继续后续过滤链
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 验证登录验证码
            validateCaptcha(request);
        } catch (AuthenticationException ex) {
            // 验证失败处理
            authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
        } catch (Exception ex) {
            // 未知异常
            LoginNumCodeException loginNumCodeException = new LoginNumCodeException(LoginNumCodeErrorCode.ELOGNC000, ex);
            authenticationFailureHandler.onAuthenticationFailure(request, response, loginNumCodeException);
        }
    }

    /**
     * 判断是否为登录请求
     *
     * @param request 当前请求对象
     * @return true 如果是登录请求，false 否则
     */
    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().equals(loginPath) && HttpMethod.POST.name().equalsIgnoreCase(request.getMethod());
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
            throw new LoginNumCodeException(LoginNumCodeErrorCode.ELOGNC000);
        }

        // 获取用户输入的验证码
        String code = (String) request.getAttribute(DEFAULT_CODE_KEY);
        if (StringUtils.isEmpty(code)) {
            throw new LoginNumCodeException(LoginNumCodeErrorCode.ELOGNC001);
        }

        // 获取后台缓存的验证码并校验
        String cacheCode = captchaHandler.getCaptchaText(uuid);
        if (cacheCode == null) {
            throw new LoginNumCodeException(LoginNumCodeErrorCode.ELOGNC002);
        }

        // 验证用户输入的验证码是否正确
        if (!captchaHandler.verifyCaptcha(uuid, code)) {
            throw new LoginNumCodeException(LoginNumCodeErrorCode.ELOGNC003);
        }
    }

    public void setCaptchaHandler(CaptchaHandler captchaHandler) {
        this.captchaHandler = captchaHandler;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
