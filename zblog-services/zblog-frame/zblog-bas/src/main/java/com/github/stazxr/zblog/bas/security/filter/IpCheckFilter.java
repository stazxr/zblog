package com.github.stazxr.zblog.bas.security.filter;

import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.msg.util.ResponseUtils;
import com.github.stazxr.zblog.bas.security.cache.BlackWhiteListCache;
import com.github.stazxr.zblog.util.net.IpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * IP 检查过滤器，用于拦截请求并根据 IP 黑白名单规则进行过滤。
 * <p>
 * 黑名单优先于白名单。被列入黑名单的 IP 请求将被拒绝；
 * 被列入白名单的 IP 请求将直接通过，其他请求将继续执行后续的过滤器链。
 * </p>
 *
 * @author SunTao
 * @since 2024-11-17
 */
@Component
public class IpCheckFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求IP
        String requestIp = IpUtils.getIp(request);

        // 检查白名单
        if (BlackWhiteListCache.isWhitelisted(requestIp)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 检查黑名单
        if (BlackWhiteListCache.isBlacklisted(requestIp)) {
            handleBlacklistedIp(response, requestIp);
            return;
        }

        // 默认行为：非白名单和黑名单的请求继续过滤链
        handleDefault(request, response, filterChain);
    }

    /**
     * 处理被列入黑名单的 IP 请求。
     *
     * @param response   响应对象
     * @param requestIp  请求的 IP 地址
     * @throws IOException 写入响应时可能抛出的异常
     */
    private void handleBlacklistedIp(HttpServletResponse response, String requestIp) throws IOException {
        response.getWriter().write(String.format("Access denied for IP: %s (Blacklisted)", requestIp));
        Result result = Result.failure("拒绝访问");
        ResponseUtils.responseJsonWriter(response, result, HttpStatus.FORBIDDEN);
    }

    /**
     * 默认请求处理行为。
     * <p>
     * 非白名单或黑名单的请求通过过滤器链继续处理。
     * </p>
     *
     * @param request     请求对象
     * @param response    响应对象
     * @param filterChain 过滤器链
     * @throws ServletException 请求处理时可能抛出的异常
     * @throws IOException      请求处理时可能抛出的异常
     */
    private void handleDefault(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }
}
