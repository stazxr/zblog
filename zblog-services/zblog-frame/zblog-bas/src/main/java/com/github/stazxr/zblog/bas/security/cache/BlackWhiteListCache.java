package com.github.stazxr.zblog.bas.security.cache;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理 {IP} 和 {URI} 黑白名单的缓存类。
 * <p>
 * 提供线程安全的操作，用于检查 {IP} 和 {URI} 是否存在于黑名单或白名单中，
 * 并支持对名单的批量更新。
 * </p>
 *
 * @author SunTao
 * @since 2024-11-17
 */
public class BlackWhiteListCache {
    /**
     * 白名单集合。
     */
    private static final Set<String> WHITE_LIST = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * 黑名单集合。
     */
    private static final Set<String> BLACK_LIST = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * 判断检查对象是否在白名单中。
     *
     * @param check 检查对象
     * @return 如果存在于白名单中返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isWhitelisted(String check) {
        return WHITE_LIST.contains(check);
    }

    /**
     * 判断检查对象是否在黑名单中。
     *
     * @param check 检查对象
     * @return 如果存在于黑名单中返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isBlacklisted(String check) {
        return BLACK_LIST.contains(check);
    }

    /**
     * 更新白名单，将白名单替换为指定的新集合。
     *
     * @param newWhitelist 包含新的白名单集合
     * @throws NullPointerException 如果传入的集合为 {@code null} 时抛出
     */
    public synchronized void updateWhitelist(Set<String> newWhitelist) {
        WHITE_LIST.clear();
        if (newWhitelist != null && !newWhitelist.isEmpty()) {
            WHITE_LIST.addAll(newWhitelist);
        }
    }

    /**
     * 更新黑名单，将黑名单替换为指定的新集合。
     *
     * @param newBlacklist 包含新的黑名单集合
     * @throws NullPointerException 如果传入的集合为 {@code null} 时抛出
     */
    public synchronized void updateBlacklist(Set<String> newBlacklist) {
        BLACK_LIST.clear();
        if (newBlacklist != null && !newBlacklist.isEmpty()) {
            BLACK_LIST.addAll(newBlacklist);
        }
    }
}

