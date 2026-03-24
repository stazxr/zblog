package com.github.stazxr.zblog.bas.security.jwt.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.context.Context;
import com.github.stazxr.zblog.bas.context.constant.TagConstants;
import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.security.SecurityConstant;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.security.authz.metadata.SecurityResourceService;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.core.TokenError;
import com.github.stazxr.zblog.bas.security.jwt.JwtConstants;
import com.github.stazxr.zblog.bas.security.jwt.JwtContext;
import com.github.stazxr.zblog.bas.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.bas.security.jwt.autoconfigure.properties.JwtProperties;
import com.github.stazxr.zblog.bas.security.jwt.decoder.JwtDecoder;
import com.github.stazxr.zblog.bas.security.jwt.decoder.JwtDecodingException;
import com.github.stazxr.zblog.bas.security.jwt.exception.JwtAuthenticationException;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.bas.security.jwt.storage.TokenPayload;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * jwt认证拦截器，用于拦截请求，提取jwt认证
 *
 * @author SunTao
 * @since 2022-01-29
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final Cache<String, Date> REFRESH_TOKEN_CACHE = Caffeine.newBuilder()
            .expireAfter(new Expiry<String, Date>() {
                @Override
                public long expireAfterCreate(@NonNull String key, @NonNull Date value, long currentTime) {
                    long millis = value.getTime() - System.currentTimeMillis();
                    return millis > 0 ? TimeUnit.MILLISECONDS.toNanos(millis) : 0;
                }

                @Override
                public long expireAfterUpdate(@NonNull String key, @NonNull Date value, long currentTime, long currentDuration) {
                    return currentDuration;
                }

                @Override
                public long expireAfterRead(@NonNull String key, @NonNull Date value, long currentTime, long currentDuration) {
                    return currentDuration;
                }
            })
            .maximumSize(10000)
            .build();

    /**
     * 用户续签 Future 控制（单用户单续签）
     */
    private static final ConcurrentHashMap<String, CompletableFuture<String>> RENEW_FUTURE_MAP = new ConcurrentHashMap<>();

    /**
     * 单用户续签限流
     */
    private static final Cache<String, Boolean> RENEW_LIMIT_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .maximumSize(10000)
            .build();

    /**
     * 宽限期缓存（超时后允许旧 token 继续访问）
     */
    private static final Cache<String, Boolean> TOKEN_GRACE_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(10000)
            .build();

    /**
     * 最大等待续签时间 / 令牌被弃用后的一个最大存活期，单位秒
     */
    private static final int MAX_RENEW_WAIT_SECONDS = 30;

    private static final String AUTHENTICATION_PREFIX = SecurityConstant.AUTHENTICATION_PREFIX;

    private static final String ERROR_PREFIX = "Token 认证异常: ";

    private static final String KICK_OUT_USER_CACHE_KEY = "kick-out-user:%s";

    private static final AtomicInteger THREAD_COUNT = new AtomicInteger(1);

    private static final ThreadPoolExecutor RENEW_EXECUTOR = new ThreadPoolExecutor(
        2,
        10,
        60,
        TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(1000),
        r -> {
            Thread t = new Thread(r);
            t.setName("token续签-" + THREAD_COUNT.getAndIncrement());
            t.setDaemon(true);
            t.setUncaughtExceptionHandler((thread, ex) ->
                log.error("线程 [{}] 执行异常", thread.getName(), ex)
            );
            return t;
        },
        new ThreadPoolExecutor.CallerRunsPolicy()
    );

    private JwtDecoder jwtDecoder;

    private JwtTokenGenerator jwtTokenGenerator;

    private JwtTokenStorage jwtTokenStorage;

    private JwtProperties jwtProperties;

    private SecurityUserService securityUserService;

    private SecurityResourceService securityResourceService;

    private AuthenticationEntryPoint authenticationEntryPoint;

    private String errorPath;

    private String loginPath;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws IOException, ServletException {
        // 登录接口或 ERROR 接口直接访问
        String requestURI = request.getRequestURI();
        if (requestURI.equals(loginPath) || requestURI.equals(errorPath)) {
            chain.doFilter(request, response);
            return;
        }

        // 非系统请求不鉴权
        if (!requestURI.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        // 令牌解析
        String jwtToken = getTokenFromHeader(request);
        if (jwtToken != null) {
            try {
                // 验证 Token 并将用户信息写入上下文
                authenticationTokenHandle(jwtToken, request, response);
            } catch (AuthenticationException ex) {
                authenticationEntryPoint.commence(request, response, ex);
                return;
            }

            try {
                // 继续请求
                chain.doFilter(request, response);
            } finally {
                SecurityContextHolder.clearContext();
            }
        } else {
            // 未携带 Authorization 请求头，视为用户未登录
            authenticationWithoutToken(chain, request, response);
        }
    }

    @PostConstruct
    public void monitorThreadPool() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            int poolSize = RENEW_EXECUTOR.getPoolSize();
            int active = RENEW_EXECUTOR.getActiveCount();
            int queueSize = RENEW_EXECUTOR.getQueue().size();
            long completed = RENEW_EXECUTOR.getCompletedTaskCount();
            log.info("【Token续签线程池】线程数={}, 活跃={}, 队列={}, 已完成={}",
                    poolSize, active, queueSize, completed);

            // 🚨 告警（关键）
            if (queueSize > 500) {
                log.error("Token续签线程池队列积压严重: {}", queueSize);
            }

            if (active == RENEW_EXECUTOR.getMaximumPoolSize()) {
                log.error("Token续签线程池已满载");
            }

        }, 10, 10, TimeUnit.SECONDS);
    }

    private void authenticationTokenHandle(String jwtToken, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 解码 token 获取 JWTClaimsSet 对象
            Date requestTime = new Date();
            JWTClaimsSet claimsSet = jwtDecoder.decode(jwtToken);

            // 获取用户信息
            String userId = claimsSet.getSubject();
            SecurityUser loginUser = loadUserFromClaimSet(userId);
            String username = loginUser.getUsername();

            // 检查 token 缓存信息
            TokenPayload tokenPayload = jwtTokenStorage.get(userId);
            if (tokenPayload == null) {
                log.debug("{} 用户 [{}] Token 已过期（缓存失效）", ERROR_PREFIX, username);
                throw new JwtAuthenticationException(TokenError.TE006);
            }

            // 检查 access token 信息
            checkAccessToken(tokenPayload, claimsSet, username);
            tokenPayload.setLastAccessTime(requestTime);

            // 检查是否已被踢出
            checkUserLoginStatus(claimsSet.getJWTID(), username);

            // 检查网络是否发生变化
            checkTokenLoginIp(request, claimsSet, username);

            // 检查 token 是否过期
            boolean expired = requestTime.after(claimsSet.getExpirationTime());
            if (expired) {
                Boolean grace = TOKEN_GRACE_CACHE.getIfPresent(userId);
                if (grace != null) {
                    // 宽限期
                    log.warn("用户 {} 处于宽限期内，允许访问", username);
                } else {
                    String refreshToken = tokenPayload.getRefreshToken();
                    if (jwtProperties.isAllowedRenewToken() && refreshToken != null) {
                        // 检查 refresh token 是否过期
                        checkRefreshTokenExpired(username, refreshToken, requestTime);
                        // 检查是否触发限流
                        checkRenewLimit(userId, username);
                        // 续签
                        handleRenew(response, userId, username, claimsSet);
                    } else {
                        // 已过期
                        log.debug("{} 用户 [{}] Token 已过期（ATK）", ERROR_PREFIX, username);
                        throw new JwtAuthenticationException(TokenError.TE006);
                    }
                }
            }

            // 设置 Authentication 上下文
            setContextAuthentication(request, loginUser);
        } catch (JwtAuthenticationException ex) {
            throw ex;
        } catch (JwtDecodingException ex) {
            throw new JwtAuthenticationException(TokenError.TE003, ex);
        } catch (Exception ex) {
            throw new JwtAuthenticationException(TokenError.TE099, ex);
        }
    }

    private void checkRenewLimit(String userId, String username) {
        Boolean lastRenewTime = RENEW_LIMIT_CACHE.getIfPresent(userId);
        if (lastRenewTime != null) {
            log.debug("用户 [{}] 触发续签限流", username);
            // 如果在宽限期则放行（允许继续用旧 token）
            if (TOKEN_GRACE_CACHE.getIfPresent(userId) != null) {
                log.warn("用户 [{}] 触发限流，但处于宽限期，允许访问", username);
                return;
            }
            log.warn("用户 [{}] 触发限流且不在宽限期，拒绝访问", username);
            throw new JwtAuthenticationException(TokenError.TE012);
        }

        // 标记本次续签（用于限流）
        RENEW_LIMIT_CACHE.put(userId, Boolean.TRUE);
    }

    protected SecurityUser loadUserFromClaimSet(String userId) {
        try {
            if (StringUtils.isNotBlank(userId)) {
                SecurityUser user = SecurityUserCache.getOrLoad(userId, securityUserService::loadUserInfo);
                if (user != null) {
                    return user;
                }
            }

            throw new JwtAuthenticationException(TokenError.TE005);
        } catch (Exception ex) {
            log.error("{} 查询用户信息失败: {}", ERROR_PREFIX, userId, ex);
            throw new JwtAuthenticationException(TokenError.TE099, ex);
        }
    }

    private void checkAccessToken(TokenPayload tokenPayload, JWTClaimsSet claimsSet, String username) {
        String jwtId = claimsSet.getJWTID();
        String cacheJwtId = tokenPayload.getAccessTokenId();
        if (!jwtId.equals(cacheJwtId)) {
            throw new JwtAuthenticationException(TokenError.TE007);
        }
    }

    private void checkUserLoginStatus(String jwtId, String username) {
        String cacheKey = String.format(KICK_OUT_USER_CACHE_KEY, jwtId);
        String kickOutUsername = GlobalCache.get(cacheKey);
        if (username.equals(kickOutUsername)) {
            throw new JwtAuthenticationException(TokenError.TE008);
        }
    }

    private void checkTokenLoginIp(HttpServletRequest request, JWTClaimsSet claimsSet, String username) {
        if (jwtProperties.isCheckIpChange()) {
            try {
                String loginIp = claimsSet.getStringClaim(JwtConstants.LOGIN_IP_KEY);
                String requestIp = IpUtils.getIp(request);
                if (loginIp.equals(requestIp)) {
                    return;
                }
                log.error("{} 用户 [{}] IP地址发生变化 {} >>> {}", ERROR_PREFIX, username, loginIp, requestIp);
            } catch (Exception ex) {
                log.error("{} 用户 [{}] 登录IP检查发生异常", ERROR_PREFIX, username, ex);
            }

            throw new JwtAuthenticationException(TokenError.TE009);
        }
    }

    private void checkRefreshTokenExpired(String username, String refreshToken, Date requestTime) {
        try {
            Date expireTime = REFRESH_TOKEN_CACHE.getIfPresent(refreshToken);
            if (expireTime == null) {
                JWTClaimsSet refreshClaimsSet = jwtDecoder.decode(refreshToken);
                expireTime = refreshClaimsSet.getExpirationTime();
                REFRESH_TOKEN_CACHE.put(refreshToken, expireTime);
            }

            boolean expired = requestTime.after(expireTime);
            if (!expired) {
                return;
            }
            log.debug("{} 用户 [{}] Token 已过期（RTK）", ERROR_PREFIX, username);
            throw new JwtAuthenticationException(TokenError.TE006);
        } catch (JwtAuthenticationException e) {
            throw e;
        } catch (Exception ex) {
            log.error("{} 检查用户 [{}] 刷新令牌是否过期发生异常", ERROR_PREFIX, username, ex);
            throw new JwtAuthenticationException(TokenError.TE099, ex);
        }
    }

    private void handleRenew(HttpServletResponse response, String userId, String username, JWTClaimsSet claimsSet) {
        CompletableFuture<String> future = RENEW_FUTURE_MAP.computeIfAbsent(userId, key -> {
            log.info("用户 [{}] 创建续签任务", username);
            CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> doRenewToken(userId, claimsSet), RENEW_EXECUTOR);
            f.whenComplete((r, e) -> RENEW_FUTURE_MAP.remove(userId));
            return f;
        });

        if (RENEW_FUTURE_MAP.get(userId) != future) {
            log.debug("用户 [{}] 复用续签任务", username);
        }

        try {
            String newToken = future.get(MAX_RENEW_WAIT_SECONDS, TimeUnit.SECONDS);
            if (StringUtils.isNotBlank(newToken)) {
                response.addHeader(JwtConstants.NEW_TOKEN_HEADER, AUTHENTICATION_PREFIX.concat(newToken));
            }
            log.info("用户 [{}] 续签成功", username);
        } catch (TimeoutException e) {
            log.warn("用户 [{}] 续签等待超时，进入宽限期", username);
            future.cancel(true); // 尝试中断线程
            TOKEN_GRACE_CACHE.put(userId, Boolean.TRUE);
        } catch (JwtAuthenticationException e) {
            log.error("用户 [{}] 续签失败", username);
            throw e;
        } catch (Exception e) {
            log.error("用户 [{}] 续签异常", username);
            throw new JwtAuthenticationException(TokenError.TE010, e);
        }
    }

    private String doRenewToken(String userId, JWTClaimsSet claimsSet) {
        try {
            JwtContext jwtContext = new JwtContext();
            jwtContext.setUserId(userId);
            jwtContext.setLoginIp(claimsSet.getStringClaim(JwtConstants.LOGIN_IP_KEY));
            return jwtTokenGenerator.refreshToken(jwtContext);
        } catch (Exception e) {
            throw new JwtAuthenticationException(TokenError.TE010, e);
        }
    }

    private void setContextAuthentication(HttpServletRequest request, SecurityUser user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 登录用户id
        String loginId = String.valueOf(SecurityUtils.getLoginId());
        Context.put(new ContextTag(TagConstants.LOGIN_ID_TAG, loginId));
    }

    /** 从 header 中获取 jwt token */
    protected String getTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(AUTHENTICATION_PREFIX)) {
            String jwtToken = authorizationHeader.replace(AUTHENTICATION_PREFIX, "");
            if (StringUtils.isNotBlank(jwtToken)) {
                return jwtToken;
            }
        }
        return null;
    }

    /** 未携带 jwt token 时的处理逻辑 */
    private void authenticationWithoutToken(FilterChain chain,
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 判断接口是否是开放接口
        int resourceLevel = securityResourceService.getResourceLevel(request.getRequestURI(), request.getMethod());
        if (RouterLevel.OPEN == resourceLevel) {
            // 可直接访问的接口忽略令牌校验
            chain.doFilter(request, response);
            return;
        }

        // 抛出异常
        authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException(TokenError.TE001.getMessage()));
    }

    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public void setJwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    public void setJwtTokenStorage(JwtTokenStorage jwtTokenStorage) {
        this.jwtTokenStorage = jwtTokenStorage;
    }

    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public void setSecurityUserService(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    public void setSecurityResourceService(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }
}
