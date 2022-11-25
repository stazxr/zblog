package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.ArticleColumnDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleColumnQueryDto;
import com.github.stazxr.zblog.domain.entity.ArticleColumn;
import com.github.stazxr.zblog.domain.vo.ArticleColumnVo;

/**
 * 文章栏目服务层
 *
 * @author SunTao
 * @since 2022-11-22
 */
public interface ArticleColumnService extends IService<ArticleColumn> {
    /**
     * 查询栏目列表
     *
     * @param queryDto 查询参数
     * @return ColumnVoList
     */
    PageInfo<ArticleColumnVo> queryColumnListByPage(ArticleColumnQueryDto queryDto);

    /**
     * 查询栏目详情
     *
     * @param columnId 标签ID
     * @return TagVo
     */
    ArticleColumnVo queryColumnDetail(Long columnId);

    /**
     * 新增栏目
     *
     * @param columnDto 栏目信息
     */
    void addColumn(ArticleColumnDto columnDto);

    /**
     * 编辑栏目
     *
     * @param columnDto 栏目信息
     */
    void editColumn(ArticleColumnDto columnDto);

    /**
     * 删除栏目
     *
     * @param columnId 栏目ID
     */
    void deleteColumn(Long columnId);
}
