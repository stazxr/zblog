package com.github.stazxr.zblog.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 拼音字符串转换工具类
 *
 * @author SunTao
 * @since 2024-10-21
 */
public class PingYinUtil {
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
