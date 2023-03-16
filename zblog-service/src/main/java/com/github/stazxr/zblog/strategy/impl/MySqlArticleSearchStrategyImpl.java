package com.github.stazxr.zblog.strategy.impl;

import com.github.stazxr.zblog.domain.vo.ArticleVo;
import com.github.stazxr.zblog.mapper.ArticleMapper;
import com.github.stazxr.zblog.strategy.ArticleSearchStrategy;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.github.stazxr.zblog.util.YwConstants.POST_TAG;
import static com.github.stazxr.zblog.util.YwConstants.PRE_TAG;

/**
 * mysql搜索策略
 *
 * @author yezhiqiu
 * @date 2021/07/27
 */
@Component("mySqlArticleSearchStrategyImpl")
@RequiredArgsConstructor
public class MySqlArticleSearchStrategyImpl implements ArticleSearchStrategy {
    private final ArticleMapper articleMapper;

    /**
     * 搜索文章
     *
     * @param keywords 关键字
     * @return {@link List<ArticleVo>} 文章列表
     */
    @Override
    public List<ArticleVo> searchArticle(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }

        // 搜索文章
        List<ArticleVo> articleList = articleMapper.selectArticleByKeywords(keywords);

        // 高亮处理
        return articleList.stream().peek(item -> {
            // 获取关键词第一次出现的位置
            String articleContent = item.getContent();
            int index = item.getContent().toLowerCase(Locale.ROOT).indexOf(keywords.toLowerCase(Locale.ROOT));
            if (index != -1) {
                // 获取关键词前面的文字
                int preIndex = index > 25 ? index - 25 : 0;
                String preText = item.getContent().substring(preIndex, index);

                // 获取关键词到后面的文字
                int last = index + keywords.length();
                int postLength = item.getContent().length() - last;
                int postIndex = postLength > 175 ? last + 175 : last + postLength;
                String postText = item.getContent().substring(index, postIndex);
                articleContent = (preText + postText).replaceAll("(?i)" + keywords, PRE_TAG + keywords + POST_TAG);
            }

            // 文章标题高亮
            String articleTitle = item.getTitle().replaceAll("(?i)" + keywords, PRE_TAG + keywords + POST_TAG);
            item.setTitle(articleTitle);
            item.setContent(articleContent);
        }).collect(Collectors.toList());
    }

}
