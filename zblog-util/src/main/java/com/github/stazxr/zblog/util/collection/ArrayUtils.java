package com.github.stazxr.zblog.util.collection;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {
    // the unit of split big array to batch save.
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
        int remainder = source.size() % count;  //(先计算出余数)
        int number = source.size() / count;  //然后是商
        int offset = 0;//偏移量
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
