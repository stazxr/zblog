package com.github.stazxr.zblog.util.collection;

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
}
