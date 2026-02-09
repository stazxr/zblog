package com.github.stazxr.zblog.bas.ratelimit.core;

import com.github.stazxr.zblog.util.net.IpUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 构建 Redis 限流 Key
 *
 * @author SunTao
 * @since 2025-02-08
 */
public class RateLimitKeyBuilder {
    /**
     * 根据规则生成多个维度 key
     */
    public static List<String> buildKeys(HttpServletRequest request, RateLimitRule rule, String userId) {
        List<String> keys = new ArrayList<>();
        String uri = request.getRequestURI().toLowerCase();
        String ip = IpUtils.getIp(request);

        // IP 维度
        if (rule.isEnableIp()) {
            keys.add("rl:ip:" + ip + ":" + uri);
        }

        // 用户维度
        if (rule.isEnableUser() && userId != null) {
            keys.add("rl:user:" + userId + ":" + uri);
        }

        // 接口维度
        if (rule.isEnableApi()) {
            keys.add("rl:api:" + uri);
        }

        return keys;
    }
}
