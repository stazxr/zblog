package com.github.stazxr.zblog.util;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * 正则查找符合条件的字符串列表
     *
     * @param str   正则查找的字符串
     * @param regex 正则表达式
     * @return 符合条件的字符串列表
     */
    public static List<String> findGroup(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    public static class Const {
        /**
         * ${变量}正则表达式
         */
        public static final String DOLLAR_VARIATE_REGEX = "\\$\\{[^}]+\\}";

        /**
         * 用户名正则表达式
         */
        public static final String USERNAME_REGEX = "^[0-9A-Za-z]{4,20}$";

        /**
         * 邮箱正则表达式
         */
        public static final String EMAIL_REGEX = "^[0-9A-Za-z][\\.-_0-9A-Za-z]*@[0-9A-Za-z]+(\\.[0-9A-Za-z]+)+$";

        /**
         * 手机号正则表达式
         */
        public static final String PHONE_REGEX = "^1([38][0-9]|4[014-9]|[59][0-35-9]|6[2567]|7[0-8])\\d{8}$";

        /**
         * 密码复杂度正则表达式
         *  密码设置要求: 密码长度必须大于等于6位且小于等于20位，需要包含大写字母，小写字母，数字，特殊字符中的至少三种
         */
        public static final String PWD_REGEX = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{6,20}$";
    }
}
