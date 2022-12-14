package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.entity.Page;
import com.github.stazxr.zblog.domain.vo.PageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 页面数据持久层
 *
 * @author SunTao
 * @since 2022-12-14
 */
public interface PageMapper extends BaseMapper<Page> {
    /**
     * 查询页面配置列表
     *
     * @return PageVoList
     */
    List<PageVo> selectPageList();

    /**
     * 查询页面详情
     *
     * @param pageId 页面ID
     * @return PageVo
     */
    PageVo selectPageDetail(@Param("pageId") Long pageId);

    /**
     * 根据页面标签查询页面信息
     *
     * @param pageLabel 页面标签
     * @return Page
     */
    Page selectByPageLabel(@Param("pageLabel") String pageLabel);
}
