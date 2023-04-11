package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.ArticleColumnDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleColumnQueryDto;
import com.github.stazxr.zblog.domain.entity.ArticleColumn;
import com.github.stazxr.zblog.domain.vo.ArticleColumnVo;
import com.github.stazxr.zblog.domain.vo.ArticleVo;

import java.util.List;

/**
 * 文章栏目服务层
 *
 * @author SunTao
 * @since 2022-11-22
 */
public interface ArticleColumnService extends IService<ArticleColumn> {
    /**
     * 分页查询专栏列表
     *
     * @param queryDto 查询参数
     * @return ColumnVoList
     */
    PageInfo<ArticleColumnVo> queryColumnListByPage(ArticleColumnQueryDto queryDto);

    /**
     * 查询专栏详情
     *
     * @param columnId 专栏id
     * @return ArticleColumnVo
     */
    ArticleColumnVo queryColumnDetail(Long columnId);

    /**
     * 新增专栏
     *
     * @param columnDto 专栏信息
     */
    void addColumn(ArticleColumnDto columnDto);

    /**
     * 编辑专栏
     *
     * @param columnDto 专栏信息
     */
    void editColumn(ArticleColumnDto columnDto);

    /**
     * 配置专栏
     *
     * @param columnDto 专栏信息
     */
    void configColumn(ArticleColumnDto columnDto);

    /**
     * 删除专栏
     *
     * @param columnId 专栏id
     */
    void deleteColumn(Long columnId);

    /**
     * 查询非专栏对应的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVo
     */
    List<ArticleVo> queryArticleListNotColumn(ArticleColumnQueryDto queryDto);
}
