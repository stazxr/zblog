package com.github.stazxr.zblog.util.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组工具类
 *
 * @author SunTao
 * @since 2022-01-15
 */
public class ArrayUtils {
    /**
     * the unit of split big array to batch save.
     */
    private static final int GREAT_SPLIT_COUNT = 50;

    /**
     * 分割List
     *
     * @param source 目标集合
     * @param <T> 元素类型
     * @return List<List<T>>
     */
    public static <T> List<List<T>> averageAssign(List<T> source) {
        return averageAssign(source, GREAT_SPLIT_COUNT);
    }

    /**
     * 分割List
     *
     * @param source 目标集合
     * @param count 分割块数
     * @param <T> 元素类型
     * @return List<List<T>>
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int count) {
        List<List<T>> result = new ArrayList<>();

        // 余数
        int remainder = source.size() % count;

        // 商
        int number = source.size() / count;

        // 偏移量
        int offset = 0;
        for (int i = 0; i < count; i++) {
            List<T> value;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }
}
