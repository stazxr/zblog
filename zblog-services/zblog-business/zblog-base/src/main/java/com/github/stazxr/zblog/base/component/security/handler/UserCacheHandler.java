package com.github.stazxr.zblog.base.component.security.handler;

import com.github.stazxr.zblog.base.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义缓存处理器
 *
 * @author SunTao
 * @since 2022-10-11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserCacheHandler {
    private final Map<Long, User> cache = new ConcurrentHashMap<>();

    public User getUserFromCache(Long userId) {
        if (userId != null) {
            return cache.get(userId);
        }

        return null;
    }

    public void putUserInCache(Long userId, User user) {
        if (userId != null) {
            cache.put(userId, user);
        }
    }

    public void removeUserFromCache(Long userId) {
        if (userId != null) {
            cache.remove(userId);
        }
    }

    public void clean() {
        cache.clear();
    }
}
