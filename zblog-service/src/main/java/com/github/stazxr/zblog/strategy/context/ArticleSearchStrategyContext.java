package com.github.stazxr.zblog.strategy.context;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.domain.dto.setting.OtherInfo;
import com.github.stazxr.zblog.domain.entity.WebsiteConfig;
import com.github.stazxr.zblog.domain.enums.WebsiteConfigType;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import com.github.stazxr.zblog.mapper.WebSettingMapper;
import com.github.stazxr.zblog.strategy.ArticleSearchStrategy;
import com.github.stazxr.zblog.strategy.enums.ArticleSearchMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 文章搜索策略上下文
 *
 * @author SunTao
 * @since 2023-02-08
 */
@Component
@RequiredArgsConstructor
public class ArticleSearchStrategyContext {
    private final Map<String, ArticleSearchStrategy> searchStrategyMap;

    private final WebSettingMapper webSettingMapper;

    /**
     * 执行搜索策略
     *
     * @param keywords 关键字
     * @return {@link List<ArticleVo>} 搜索文章
     */
    public List<ArticleVo> executeSearchStrategy(String keywords) {
        // 查询搜索策略
        WebsiteConfig websiteConfig = webSettingMapper.selectById(WebsiteConfigType.OTHER_INFO.value());
        OtherInfo otherInfo = websiteConfig == null ? new OtherInfo() : JSON.parseObject(websiteConfig.getConfig(), OtherInfo.class);
        String strategy = otherInfo.getArticleSearchStrategy();

        // 搜索文章
        ArticleSearchStrategy searchStrategy = searchStrategyMap.get(ArticleSearchMode.getStrategy(strategy));
        DataValidated.notNull(searchStrategy, "请先配置文章搜索策略");
        return searchStrategy.searchArticle(keywords);
    }
}
