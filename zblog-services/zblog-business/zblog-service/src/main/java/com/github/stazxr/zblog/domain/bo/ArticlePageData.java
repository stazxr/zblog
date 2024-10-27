package com.github.stazxr.zblog.domain.bo;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文章列表数据
 *
 * @author SunTao
 * @since 2023-01-03
 */
@Getter
@Setter
@Builder
@ToString
public class ArticlePageData {
    /**
     * 分页数据
     */
    private PageInfo<ArticleVo> dataList;

    /**
     * 数量统计信息
     */
    private ArticleCountData countInfo;
}
