package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.content.ext.domain.dto.query.PageQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Page;
import com.github.stazxr.zblog.content.ext.domain.vo.PageVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 页面管理数据层
 *
 * @author SunTao
 * @since 2022-12-14
 */
public interface PageMapper extends BaseMapper<Page> {
    /**
     * 分页查询页面列表
     *
     * @param queryDto 查询参数
     * @return IPage<PageVo>
     */
    IPage<PageVo> selectPageList(@Param("page") com.baomidou.mybatisplus.extension.plugins.pagination.Page<PageVo> page, @Param("query") PageQueryDto queryDto);

    /**
     * 查询页面详情
     *
     * @param pageId 页面id
     * @return PageVo
     */
    PageVo selectPageDetail(@Param("pageId") Long pageId);
}
