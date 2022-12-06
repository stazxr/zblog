package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.ArticleTagDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleTagQueryDto;
import com.github.stazxr.zblog.domain.entity.ArticleTag;
import com.github.stazxr.zblog.domain.vo.ArticleTagVo;

import java.util.List;

/**
 * 文章标签服务层
 *
 * @author SunTao
 * @since 2021-01-17
 */
public interface ArticleTagService extends IService<ArticleTag> {
    /**
     * 分页查询标签列表
     *
     * @param queryDto 查询参数
     * @return TagVoList
     */
    PageInfo<ArticleTagVo> queryTagListByPage(ArticleTagQueryDto queryDto);

    /**
     * 查询标签列表
     *
     * @param queryDto 查询参数
     * @return TagVoList
     */
    List<ArticleTagVo> queryTagList(ArticleTagQueryDto queryDto);

    /**
     * 查询标签详情
     *
     * @param tagId 标签ID
     * @return TagVo
     */
    ArticleTagVo queryTagDetail(Long tagId);

    /**
     * 新增标签
     *
     * @param tagDto 标签信息
     */
    void addTag(ArticleTagDto tagDto);

    /**
     * 编辑标签
     *
     * @param tagDto 标签信息
     */
    void editTag(ArticleTagDto tagDto);

    /**
     * 删除标签
     *
     * @param tagId 标签ID
     */
    void deleteTag(Long tagId);
}
