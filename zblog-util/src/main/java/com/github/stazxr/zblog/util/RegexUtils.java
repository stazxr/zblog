package com.github.stazxr.zblog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 * @author SunTao
 * @since 2020-11-15
 */
public class RegexUtils {
    /**
     * 正则查找
     *
     * @param str   正则查找的字符串
     * @param regex 正则表达式
     * @return boolean
     */
    public static boolean find(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 正则匹配
     *
     * @param str   正则查找的字符串
     * @param regex 正则表达式
     * @return boolean
     */
    public static boolean match(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    static class RegexConst {
        /**
         * 邮箱正则表达式
         */
        public static final String EMAIL_REGEX = "^[0-9A-Za-z][\\.-_0-9A-Za-z]*@[0-9A-Za-z]+(\\.[0-9A-Za-z]+)+$";
    }
}
