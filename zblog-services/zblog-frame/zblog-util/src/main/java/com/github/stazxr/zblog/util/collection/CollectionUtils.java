package com.github.stazxr.zblog.util.collection;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author SunTao
 * @since 2022-01-15
 */
public class CollectionUtils {
    private static final String[] EMPTY_STRING_ARRAY = {};

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
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断字典是否为空
     *
     * @param map 字典
     * @return Return {@code true} if the supplied Map is {@code null} or empty.
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 集合转为字符串数组
     *
     * @param collection 集合
     * @return 字符串数组
     */
    public static String[] toStringArray(Collection<String> collection) {
        return (!isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }
}
