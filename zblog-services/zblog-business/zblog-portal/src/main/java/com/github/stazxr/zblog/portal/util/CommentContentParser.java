package com.github.stazxr.zblog.portal.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 评论内容解析工具类
 *
 * <p>
 * 支持：
 * <ul>
 *     <li>[emoji:撇嘴]      -> 表情图片</li>
 *     <li>[image:123456]   -> 评论图片</li>
 * </ul>
 *
 * <p>
 * 查询不到对应资源时，保留原始占位符。
 *
 * @author suntao
 * @since 2026-09-09
 */
public final class CommentContentParser {
    /**
     * 匹配评论中的资源占位符
     */
    private static final Pattern RESOURCE_PATTERN = Pattern.compile("\\[emoji:(.+?)]|\\[image:(\\d+)]");

    private CommentContentParser() {
    }

    /**
     * 解析评论内容
     *
     * @param content       评论内容
     * @param emojiUrlQuery emoji URL 查询函数，参数为 emoji name
     * @param imageUrlQuery 图片 URL 查询函数，参数为图片 ID
     * @return 解析后的 HTML
     */
    public static String parse(String content, Function<Set<String>,
            Map<String, String>> emojiUrlQuery, Function<Set<Long>, Map<Long, String>> imageUrlQuery) {
        if (StringUtils.isBlank(content)) {
            return content;
        }

        Set<String> emojiNames = new HashSet<>();
        Set<Long> imageIds = new HashSet<>();

        // 第一次扫描：提取所有需要查询的 emoji 和 image
        Matcher matcher = RESOURCE_PATTERN.matcher(content);
        while (matcher.find()) {
            // 表情
            if (matcher.group(1) != null) {
                emojiNames.add(matcher.group(1));
            }

            // 照片
            else if (matcher.group(2) != null) {
                imageIds.add(Long.valueOf(matcher.group(2)));
            }
        }

        // 查询资源
        Map<String, String> emojiUrlMap = emojiNames.isEmpty() ? Collections.emptyMap() : emojiUrlQuery.apply(emojiNames);
        Map<Long, String> imageUrlMap = imageIds.isEmpty() ? Collections.emptyMap() : imageUrlQuery.apply(imageIds);

        StringBuffer result = new StringBuffer(content.length() + 128);

        // 第二次扫描：根据查询结果替换内容
        matcher = RESOURCE_PATTERN.matcher(content);
        while (matcher.find()) {
            String replacement = matcher.group();

            // 表情
            if (matcher.group(1) != null) {
                String url = emojiUrlMap.get(matcher.group(1));
                if (StringUtils.isNotBlank(url)) {
                    replacement = buildEmojiHtml(url);
                }
            }

            // 照片
            else if (matcher.group(2) != null) {
                String url = imageUrlMap.get(Long.valueOf(matcher.group(2)));
                if (StringUtils.isNotBlank(url)) {
                    replacement = buildImageHtml(url);
                }
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * 构建表情 HTML
     */
    private static String buildEmojiHtml(String url) {
        return "<img src='"
                + escapeHtmlAttribute(url)
                + "' alt='' width='24' height='24' "
                + "style='margin:0 1px;vertical-align:text-bottom' />";
    }

    /**
     * 构建图片 HTML
     */
    private static String buildImageHtml(String url) {
        return "<img src='"
                + escapeHtmlAttribute(url)
                + "' "
                + "alt='' "
                + "class='comment-image' "
                + "loading='lazy' />";
    }

    /**
     * HTML 属性转义
     */
    private static String escapeHtmlAttribute(String value) {
        if (value == null) {
            return null;
        }

        return value.replace("&", "&amp;")
            .replace("'", "&#39;")
            .replace("\"", "&quot;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }
}
