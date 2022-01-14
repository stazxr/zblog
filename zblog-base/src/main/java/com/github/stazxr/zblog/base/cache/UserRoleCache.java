package com.github.stazxr.zblog.base.cache;

import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户-角色信息缓存器
 *
 * @author SunTao
 * @since 2020-12-28
 */
@Slf4j
public class UserRoleCache {
    /**
     * Cache user - role.
     */
    private static final Map<String, List<Role>> userRoleCache = new ConcurrentHashMap<>();

    public static void put(String username, List<Role> roles) {
        Assert.notNull(roles, "user roles must not be null!");

        log.info("cache user role [user-{}, roles-{}]", username, roles);
        userRoleCache.put(username, roles);
    }

    public static void remove(String username) {
        userRoleCache.remove(username);
    }

    public static List<Role> get(String username) {
        return userRoleCache.get(username);
    }

    public static void clear() {
        userRoleCache.clear();
    }
}
