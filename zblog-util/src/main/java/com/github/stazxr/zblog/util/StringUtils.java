package com.github.stazxr.zblog.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author SunTao
 * @since 2020-11-14
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String[] EMPTY_STRING_ARRAY = {};

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
            if (isEmpty(s.trim())) {
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
     * 集合转为字符串数组
     *
     * @param collection 集合
     * @return 字符串数组
     */
    public static String[] toStringArray(Collection<String> collection) {
        return (!CollectionUtils.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }

    /**
     * 将汉字转换为拼音
     *
     * @param chinese 汉字
     * @param isFull  true: 全拼；false: 首字母
     * @return 全拼或者首字母大写字符(串)
     */
    public static String getUpperCaseOfHanZhi(String chinese, boolean isFull) {
        return convertHanZhi2Pinyin(chinese, isFull).toUpperCase();
    }

    /**
     * 将汉字转换为拼音
     *
     * @param chinese 汉字
     * @param isFull  true: 全拼；false: 首字母
     * @return 全拼或者首字母小写字符(串)
     */
    public static String getLowerCaseOfHanZhi(String chinese, boolean isFull) {
        return convertHanZhi2Pinyin(chinese, isFull).toLowerCase();
    }

    /**
     * 将汉字转换为拼音
     *
     * @param hanZhi  汉字
     * @param isFull  true: 全拼；false: 首字母
     * @return 全拼或者首字母小写字符(串)
     */
    private static String convertHanZhi2Pinyin(String hanZhi, boolean isFull) {
        /*
         * ^[\u2E80-\u9FFF]+$ 匹配所有东亚区的语言
         * ^[\u4E00-\u9FFF]+$ 匹配简体和繁体
         * ^[\u4E00-\u9FA5]+$ 匹配简体
         */
        String regExp = "^[\u4E00-\u9FFF]+$";
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(hanZhi)) {
            return "";
        }
        String pinyin;
        for (int i = 0; i < hanZhi.length(); i++) {
            char unit = hanZhi.charAt(i);
            if (RegexUtils.match(String.valueOf(unit), regExp)) {
                // 是汉字，则转拼音
                pinyin = convertSingleHanZhi2Pinyin(unit);
                if (isFull) {
                    sb.append(pinyin);
                } else {
                    sb.append(pinyin.charAt(0));
                }
            } else {
                // 不是汉字，则不转
                sb.append(unit);
            }
        }
        return sb.toString();
    }

    /**
     * 将单个汉字转成拼音
     *
     * @param hanZhi  汉字
     * @return 全拼或者首字母小写字符(串)
     */
    private static String convertSingleHanZhi2Pinyin(char hanZhi) {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String[] res;
        StringBuilder sb = new StringBuilder();
        try {
            res = PinyinHelper.toHanyuPinyinStringArray(hanZhi, outputFormat);

            // 对于多音字，只用第一个拼音
            sb.append(res[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }
}
