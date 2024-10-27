package com.github.stazxr.zblog.bas.jexl.function;

import com.github.stazxr.zblog.bas.jexl.ExpressionUtils;
import com.github.stazxr.zblog.util.collection.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Jexl 扩展: List
 *
 * @author SunTao
 * @since 2022-05-23
 */
public class ListHandler {
    private static final int MAX_EXTEND = 6;

    private final static ThreadLocal<Map<String, Object>> KEYS = new ThreadLocal<>();

    private final static ThreadLocal<List<Map<String, Object>>> ORIGIN_DATA = new ThreadLocal<>();

    private final static ThreadLocal<Map<String, List<Map<String, Object>>>> RANK_KEYS = new ThreadLocal<>();

    public ListHandler() {
    }

    public static void setKeys(Map<String, Object> data) {
        KEYS.set(data);
    }

    public static void removeKeys() {
        KEYS.remove();
    }

    public static void setOriginData(List<Map<String, Object>> data) {
        ORIGIN_DATA.set(data);
    }

    public static void removeOriginData() {
        ORIGIN_DATA.remove();
    }

    public static void setRankKeys(Map<String, List<Map<String, Object>>> data) {
        RANK_KEYS.set(data);
    }

    public static void removeRankKeys() {
        RANK_KEYS.remove();
    }

    /**
     * 求最大值
     *
     * @param fieldName 字段名称
     * @return BigDecimal
     */
    public static BigDecimal max(String fieldName) {
        Object result = getDataFromLocal("MAX", fieldName);
        if (result != null) {
            return new BigDecimal(result.toString());
        } else {
            List<Map<String, Object>> mapList = ORIGIN_DATA.get();
            Optional<Map<String, Object>> max = mapList.stream().max(Comparator.comparing((data) -> getBigDecimal(fieldName, data)));
            if (max.isPresent()) {
                Map<String, Object> maxObject = max.get();
                Object maxResult = getResult(fieldName, maxObject);
                if (maxResult != null) {
                    setDataFromLocal("MAX", fieldName, maxResult);
                    return new BigDecimal(maxResult.toString());
                }
            }

            return null;
        }
    }

    /**
     * 求最小值
     *
     * @param fieldName 字段名称
     * @return BigDecimal
     */
    public static BigDecimal min(String fieldName) {
        Object result = getDataFromLocal("MIN", fieldName);
        if (result != null) {
            return new BigDecimal(result.toString());
        } else {
            List<Map<String, Object>> mapList = ORIGIN_DATA.get();
            Optional<Map<String, Object>> min = mapList.stream().min(Comparator.comparing((data) -> getBigDecimal(fieldName, data)));
            if (min.isPresent()) {
                Map<String, Object> minObject = min.get();
                Object minResult = getResult(fieldName, minObject);
                if (minResult != null) {
                    setDataFromLocal("MIN", fieldName, minResult);
                    return new BigDecimal(minResult.toString());
                }
            }

            return null;
        }
    }

    /**
     * 计数,参与统计的数据数
     *
     * @param fieldName 字段名称
     * @return BigDecimal
     */
    public static BigDecimal count(String fieldName) {
        Object result = getDataFromLocal("COUNT", fieldName);
        if (null != result) {
            return new BigDecimal(result.toString());
        } else {
            BigDecimal countResult = new BigDecimal((ORIGIN_DATA.get()).size());
            setDataFromLocal("COUNT", fieldName, countResult);
            return countResult;
        }
    }

    /**
     * 求和
     *
     * @param fieldName 字段名称
     * @return BigDecimal
     */
    public static BigDecimal sum(String fieldName) {
        Object result = getDataFromLocal("SUM", fieldName);
        if (result != null) {
            return new BigDecimal(result.toString());
        } else {
            List<Map<String, Object>> mapList = ORIGIN_DATA.get();
            BigDecimal sumResult = mapList.stream().map((data) -> getBigDecimal(fieldName, data)).reduce(BigDecimal.ZERO, BigDecimal::add);
            setDataFromLocal("SUM", fieldName, sumResult);
            return sumResult;
        }
    }

    /**
     * 求平均数
     *
     * @param fieldName 字段名称
     * @return BigDecimal
     */
    public static BigDecimal avg(String fieldName) {
        Object result = getDataFromLocal("AVG", fieldName);
        if (null != result) {
            return new BigDecimal(result.toString());
        } else {
            List<Map<String, Object>> mapList = ORIGIN_DATA.get();
            double sum = mapList.stream().mapToDouble((data) -> getDouble(fieldName, data)).sum();
            BigDecimal avgResult = new BigDecimal(sum / (double) mapList.size());
            setDataFromLocal("AVG", fieldName, avgResult);
            return avgResult;
        }
    }

    /**
     * 取排名为 number 的值
     *
     * @param fieldName 字段名称
     * @param number    排名
     * @return BigDecimal
     */
    public static BigDecimal rank(String fieldName, String number) {
        Object result = getDataFromLocal("RANK", fieldName);
        if (null != result) {
            return new BigDecimal(result.toString());
        } else {
            List<Map<String, Object>> mapList = ORIGIN_DATA.get();
            List<Map<String, Object>> newMapList = mapList.stream().sorted(Comparator.comparing((data) -> getBigDecimal(fieldName, data))).collect(Collectors.toList());
            Object rankAscResult = getResult(fieldName, newMapList.get(Integer.parseInt(number) - 1));
            if (rankAscResult != null) {
                setDataFromLocal("RANK", fieldName, rankAscResult);
                return new BigDecimal(rankAscResult.toString());
            }

            return null;
        }
    }

    private static Object getDataFromLocal(String funcName, String fieldName) {
        Map<String, Object> keysMap = KEYS.get();
        return keysMap == null ? null : keysMap.get(funcName + "_" + fieldName);
    }

    private static void setDataFromLocal(String funcName, String fieldName, Object result) {
        Map<String, Object> keysMap = KEYS.get();
        if (keysMap == null) {
            keysMap = new HashMap<>(CollectionUtils.mapSize(MAX_EXTEND));
        }

        keysMap.put(funcName + "_" + fieldName, result);
        KEYS.set(keysMap);
    }

    private static BigDecimal getNumeric(Object number) {
        try {
            return new BigDecimal(Objects.toString(number));
        } catch (Exception var2) {
            return null;
        }
    }

    private static BigDecimal getBigDecimal(String fieldName, Map<String, Object> data) {
        String pattern = "^[a-zA-Z\\d_\\u4e00-\\u9fa5]+$";
        if (!Pattern.matches(pattern, fieldName)) {
            String expression = "(" + fieldName + ")";
            String value = ExpressionUtils.executeExpression(expression, data);
            return new BigDecimal(value);
        } else {
            if (data.containsKey(fieldName)) {
                return new BigDecimal(data.get(fieldName).toString());
            } else {
                return BigDecimal.ZERO;
            }
        }
    }

    private static Double getDouble(String fieldName, Map<String, Object> data) {
        String pattern = "^[a-zA-Z\\d_\\u4e00-\\u9fa5]+$";
        if (!Pattern.matches(pattern, fieldName)) {
            String expression = "(" + fieldName + ")";
            String value = ExpressionUtils.executeExpression(expression, data);
            return Double.parseDouble(value);
        } else {
            if (data.containsKey(fieldName)) {
                return Double.parseDouble(data.get(fieldName).toString());
            } else {
                return Double.parseDouble("0");
            }
        }
    }

    private static Object getResult(String fieldName, Map<String, Object> minObject) {
        String pattern = "^[a-zA-Z\\d_\\u4e00-\\u9fa5]+$";
        return !Pattern.matches(pattern, fieldName) ? minObject.get("result") : minObject.get(fieldName);
    }
}
