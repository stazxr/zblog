package com.github.stazxr.zblog.util.collection;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author SunTao
 * @since 2022-01-15
 */
public class CollectionUtils {
    /**
     * 计算hashMap的初始容量
     *
     * @param expectedSize 期待的容量
     * @return (int) ((float) expectedSize / 0.75F + 1.0F)
     */
    public static int mapSize(int expectedSize) {
        return (int) ((float) expectedSize / 0.75f + 1.0f);
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return Return {@code true} if the supplied Collection is {@code null} or empty.
     */
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断字典是否为空
     *
     * @param map 字典
     * @return Return {@code true} if the supplied Map is {@code null} or empty.
     */
    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }
}
