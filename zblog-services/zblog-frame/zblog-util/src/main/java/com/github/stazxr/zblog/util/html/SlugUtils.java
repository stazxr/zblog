package com.github.stazxr.zblog.util.html;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Slug 工具类。
 *
 * @author Sun Tao
 * @since 2026-10-10
 */
public final class SlugUtils {
    /**
     * 连续连字符匹配规则。
     */
    private static final Pattern MULTIPLE_HYPHENS = Pattern.compile("-{2,}");

    /**
     * 非法字符匹配规则，仅保留小写英文字母、数字和连字符。
     */
    private static final Pattern INVALID_CHARACTERS = Pattern.compile("[^a-z0-9-]");

    /**
     * 连字符匹配规则。
     */
    private static final Pattern EDGE_HYPHENS = Pattern.compile("^-|-$");

    /**
     * 拼音转换格式。
     */
    private static final HanyuPinyinOutputFormat PINYIN_FORMAT = new HanyuPinyinOutputFormat();

    static {
        // 使用小写拼音，不保留声调，使用标准 v 字符。
        PINYIN_FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        PINYIN_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        PINYIN_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    /**
     * 工具类不允许实例化。
     */
    private SlugUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 将字符串转换为 slug。
     *
     * <p>中文转换为拼音，英文转换为小写，
     * 空格及常见分隔符转换为连字符。</p>
     *
     * @param value 原始字符串
     * @return 生成的 slug；输入为空或无法生成有效 slug 时返回空字符串
     */
    public static String toSlug(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);

            // 英文字母及数字直接保留。
            if (isAsciiLetterOrDigit(ch)) {
                builder.append(Character.toLowerCase(ch));
                continue;
            }

            // 中文字符转换为拼音。
            if (isChinese(ch)) {
                appendPinyin(builder, ch);
                continue;
            }

            // 空白字符及常见分隔符统一转换为连字符。
            if (Character.isWhitespace(ch)
                    || ch == '-'
                    || ch == '_'
                    || ch == '.') {
                appendSeparator(builder);
            }
        }

        // 清理非法字符及多余分隔符。
        String slug = builder.toString().toLowerCase(Locale.ROOT);
        slug = INVALID_CHARACTERS.matcher(slug).replaceAll("");
        slug = MULTIPLE_HYPHENS.matcher(slug).replaceAll("-");
        slug = EDGE_HYPHENS.matcher(slug).replaceAll("");

        return slug;
    }

    /**
     * 判断字符是否为 ASCII 英文字母或数字。
     *
     * @param ch 待判断字符
     * @return true 表示是英文字母或数字
     */
    private static boolean isAsciiLetterOrDigit(char ch) {
        return (ch >= 'a' && ch <= 'z')
                || (ch >= 'A' && ch <= 'Z')
                || (ch >= '0' && ch <= '9');
    }

    /**
     * 判断字符是否位于常见汉字基本区。
     *
     * @param ch 待判断字符
     * @return true 表示是常见汉字
     */
    private static boolean isChinese(char ch) {
        return ch >= '\u4E00' && ch <= '\u9FA5';
    }

    /**
     * 将汉字转换为拼音并追加到结果中。
     *
     * @param builder 结果构建器
     * @param ch 待转换汉字
     */
    private static void appendPinyin(StringBuilder builder, char ch) {
        try {
            String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(ch, PINYIN_FORMAT);
            if (pinyin != null && pinyin.length > 0) {
                builder.append(pinyin[0]);
            } else {
                appendSeparator(builder);
            }
        } catch (Exception e) {
            // 遇到无法转换的字符时，以分隔符隔开。
            appendSeparator(builder);
        }
    }

    /**
     * 追加分隔符，避免连续追加连字符。
     *
     * @param builder 结果构建器
     */
    private static void appendSeparator(StringBuilder builder) {
        int length = builder.length();
        if (length > 0 && builder.charAt(length - 1) != '-') {
            builder.append('-');
        }
    }
}
