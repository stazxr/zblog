package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.bo.PageInfo;
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
     * 查询前台页面列表
     *
     * @return PageList
     */
    List<PageInfo> selectWebPageList();
}
