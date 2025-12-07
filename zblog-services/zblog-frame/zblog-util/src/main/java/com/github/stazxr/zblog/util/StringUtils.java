package com.github.stazxr.zblog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author SunTao
 * @since 2020-11-14
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }

    /**
     * 判断字符串是否为空
     * 条件（字符串null或者字符串trim后长度为0）
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isBlank(String str) {
        return null == str || "".equals(str.trim());
    }

    /**
     * 判断字符串是否不为空
     * 条件（字符串null或者字符串trim后长度为0）
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断字符串数组中是否存在字符串为空
     * 条件（字符串null或者length等于0）
     *
     * @param ss 字符串数组
     * @return ss包含空字符串则返回true
     */
    public static boolean hasEmpty(String... ss) {
        for (String s : ss) {
            if (isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串数组中是否存在字符串为空
     * 条件（字符串null或者字符串trim后长度为0）
     *
     * @param ss 字符串数组
     * @return ss包含空字符串则返回true
     */
    public static boolean hasBlank(String... ss) {
        for (String s : ss) {
            if (isBlank(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 驼峰 转下划线
     *
     * @param camelCase 驼峰风格的字符串
     * @return 下划线风格
     */
    public static String cameCaseToSubLine(String camelCase) {
        String az = "[A-Z]";
        Pattern pattern = Pattern.compile(az);
        Matcher matcher = pattern.matcher(camelCase);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Masks the first character of a string with an asterisk.
     *
     * @param data The string to mask
     * @return The masked string
     */
    public static String hideFirstChar(String data) {
        if (data != null && data.length() > 0) {
            return "*" + (data.length() < 2 ? "" : data.substring(1));
        }
        return data;
    }
}
