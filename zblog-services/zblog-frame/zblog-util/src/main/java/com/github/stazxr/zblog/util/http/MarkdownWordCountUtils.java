package com.github.stazxr.zblog.util.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown 文章字数统计工具类。
 *
 * @author SunTao
 * @since 2026-10-14
 */
public final class MarkdownWordCountUtils {
    /**
     * 匹配围栏代码块的开始和结束标记。
     * 支持反引号和波浪号围栏。
     */
    private static final Pattern FENCED_CODE_PATTERN =
        Pattern.compile("(?m)^\\s*(`{3,}|~{3,})[^\\r\\n]*\\r?\\n|(?m)^\\s*(`{3,}|~{3,})\\s*$");

    /**
     * 匹配 Markdown 图片。
     */
    private static final Pattern IMAGE_PATTERN =
        Pattern.compile("!\\[([^\\]]*)\\]\\([^)]*\\)|!\\[([^\\]]*)\\]\\[[^\\]]*\\]");

    /**
     * 匹配 Markdown 链接，保留链接文本。
     */
    private static final Pattern LINK_PATTERN =
        Pattern.compile("\\[([^\\]]+)\\]\\([^)]*\\)|\\[([^\\]]+)\\]\\[[^\\]]*\\]");

    /**
     * 匹配 HTML 标签。
     */
    private static final Pattern HTML_TAG_PATTERN =
        Pattern.compile("<!--.*?-->|</?[a-zA-Z][^>]*>", Pattern.DOTALL);

    /**
     * 匹配行首 Markdown 标题标记。
     */
    private static final Pattern HEADING_PATTERN =
        Pattern.compile("(?m)^\\s{0,3}#{1,6}\\s+");

    /**
     * 匹配行首引用标记。
     */
    private static final Pattern BLOCKQUOTE_PATTERN =
        Pattern.compile("(?m)^\\s{0,3}>\\s?");

    /**
     * 匹配行首无序列表标记。
     */
    private static final Pattern UNORDERED_LIST_PATTERN =
        Pattern.compile("(?m)^\\s{0,3}[-+*]\\s+");

    /**
     * 匹配行首有序列表标记。
     */
    private static final Pattern ORDERED_LIST_PATTERN =
        Pattern.compile("(?m)^\\s{0,3}\\d+[.)、]\\s+");

    /**
     * 匹配 Markdown 强调、删除线等标记。
     */
    private static final Pattern EMPHASIS_PATTERN =
        Pattern.compile("(\\*\\*|__|~~|\\*|_|==)");

    /**
     * 匹配行内代码标记。
     */
    private static final Pattern INLINE_CODE_PATTERN =
        Pattern.compile("(`+)(.*?)\\1", Pattern.DOTALL);

    /**
     * 匹配 Markdown 表格分隔行。
     */
    private static final Pattern TABLE_SEPARATOR_PATTERN =
        Pattern.compile("(?m)^\\s*\\|?\\s*:?-{3,}:?\\s*(\\|\\s*:?-{3,}:?\\s*)+\\|?\\s*$");

    /**
     * 匹配常见 HTML 字符实体。
     */
    private static final Pattern HTML_ENTITY_PATTERN =
        Pattern.compile("&(amp|lt|gt|quot|apos|nbsp|#\\d+|#x[0-9a-fA-F]+);");

    /**
     * 工具类不允许实例化。
     */
    private MarkdownWordCountUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 统计 Markdown 正文的字数。
     *
     * @param markdown Markdown 原文
     * @return 字数；空内容返回 0
     */
    public static int countMarkdownWords(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return 0;
        }

        String plainText = stripMarkdown(markdown);
        return countPlainText(plainText);
    }

    /**
     * 统计纯文本的字符数。
     *
     * <p>统计所有非空白 Unicode 字符。
     * 使用 codePoint 统计，避免将一个补充平面字符
     * （例如部分 emoji）拆成两个 UTF-16 字符。
     *
     * @param text 纯文本
     * @return 字符数；空内容返回 0
     */
    public static int countPlainText(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        int count = 0;

        for (int offset = 0; offset < text.length();) {
            int codePoint = text.codePointAt(offset);
            offset += Character.charCount(codePoint);

            if (!Character.isWhitespace(codePoint)
                    && !Character.isSpaceChar(codePoint)) {
                count++;
            }
        }

        return count;
    }

    /**
     * 将 Markdown 转换为用于字数统计的文本。
     *
     * <p>此方法仅移除常见 Markdown 语法，不负责渲染 Markdown。
     *
     * @param markdown Markdown 原文
     * @return 去除常见语法标记后的文本
     */
    public static String stripMarkdown(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }

        String text = markdown;

        // 1. 移除代码围栏标记，保留代码内容。
        text = FENCED_CODE_PATTERN.matcher(text).replaceAll("");

        // 2. 移除图片语法，不统计图片及其替代文本。
        text = IMAGE_PATTERN.matcher(text).replaceAll("");

        // 3. 链接只保留可见文本。
        text = replaceLinkText(text);

        // 4. 移除 HTML 标签，保留标签之间的文本。
        text = HTML_TAG_PATTERN.matcher(text).replaceAll("");

        // 5. 移除 Markdown 表格分隔行。
        text = TABLE_SEPARATOR_PATTERN.matcher(text).replaceAll("");

        // 6. 移除行首标题、引用及列表标记。
        text = HEADING_PATTERN.matcher(text).replaceAll("");
        text = BLOCKQUOTE_PATTERN.matcher(text).replaceAll("");
        text = UNORDERED_LIST_PATTERN.matcher(text).replaceAll("");
        text = ORDERED_LIST_PATTERN.matcher(text).replaceAll("");

        // 7. 行内代码保留代码内容，移除反引号。
        text = replaceInlineCode(text);

        // 8. 移除常见强调、删除线标记。
        text = EMPHASIS_PATTERN.matcher(text).replaceAll("");

        // 9. 移除常见 HTML 字符实体，避免将实体名称算作正文。
        text = decodeHtmlEntities(text);

        // 10. 移除 Markdown 转义符的反斜杠，保留被转义字符。
        text = text.replaceAll("\\\\([\\\\`*_{}\\[\\]()#+\\-.!>|~])", "$1");

        return text;
    }

    /**
     * 替换 Markdown 链接，仅保留链接显示文本。
     *
     * @param text Markdown 文本
     * @return 替换后的文本
     */
    private static String replaceLinkText(String text) {
        Matcher matcher = LINK_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String label = matcher.group(1) != null
                    ? matcher.group(1)
                    : matcher.group(2);

            matcher.appendReplacement(
                    result, Matcher.quoteReplacement(label == null ? "" : label));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * 替换行内代码标记，保留代码内容。
     *
     * @param text Markdown 文本
     * @return 替换后的文本
     */
    private static String replaceInlineCode(String text) {
        Matcher matcher = INLINE_CODE_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String code = matcher.group(2);
            matcher.appendReplacement(
                    result, Matcher.quoteReplacement(code == null ? "" : code));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * 解码字数统计中常见的 HTML 字符实体。
     *
     * @param text 待处理文本
     * @return 解码后的文本
     */
    private static String decodeHtmlEntities(String text) {
        Matcher matcher = HTML_ENTITY_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String entity = matcher.group(1);
            String replacement;

            switch (entity) {
                case "amp":
                    replacement = "&";
                    break;
                case "lt":
                    replacement = "<";
                    break;
                case "gt":
                    replacement = ">";
                    break;
                case "quot":
                    replacement = "\"";
                    break;
                case "apos":
                    replacement = "'";
                    break;
                case "nbsp":
                    replacement = " ";
                    break;
                default:
                    replacement = decodeNumericEntity(entity);
                    break;
            }

            matcher.appendReplacement(
                    result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * 解码十进制或十六进制数字实体。
     *
     * @param entity 数字实体内容，不包含 & 和 ;
     * @return 解码结果；非法实体保持原样
     */
    private static String decodeNumericEntity(String entity) {
        try {
            int codePoint;

            if (entity.startsWith("#x") || entity.startsWith("#X")) {
                codePoint = Integer.parseInt(entity.substring(2), 16);
            } else {
                codePoint = Integer.parseInt(entity.substring(1));
            }

            if (!Character.isValidCodePoint(codePoint)
                    || (codePoint >= Character.MIN_SURROGATE
                    && codePoint <= Character.MAX_SURROGATE)) {
                return "&" + entity + ";";
            }

            return new String(Character.toChars(codePoint));
        } catch (NumberFormatException ex) {
            return "&" + entity + ";";
        }
    }
}
