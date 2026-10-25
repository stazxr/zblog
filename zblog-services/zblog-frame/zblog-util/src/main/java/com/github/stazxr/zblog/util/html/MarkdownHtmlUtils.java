package com.github.stazxr.zblog.util.html;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.Arrays;

/**
 * Markdown 转 HTML 工具类。
 *
 * @author Sun Tao
 * @since 2026-10-24
 */
public final class MarkdownHtmlUtils {
    /**
     * Markdown 解析器。
     */
    private static final Parser PARSER;

    /**
     * HTML 渲染器。
     */
    private static final HtmlRenderer RENDERER;

    static {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(
            TablesExtension.create(),
            StrikethroughExtension.create(),
            AutolinkExtension.create()
        ));

        PARSER = Parser.builder(options).build();
        RENDERER = HtmlRenderer.builder(options).build();
    }

    private MarkdownHtmlUtils() {
    }

    /**
     * 将 Markdown 转换为 HTML。
     *
     * @param markdown Markdown 内容
     * @return HTML 内容
     */
    public static String toHtml(String markdown) {
        if (markdown == null || markdown.trim().isEmpty()) {
            return "";
        }

        return RENDERER.render(PARSER.parse(markdown));
    }
}