package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.domain.dto.PageDto;
import com.github.stazxr.zblog.domain.entity.Page;
import com.github.stazxr.zblog.domain.vo.PageVo;

import java.util.List;

/**
 * 页面服务层
 *
 * @author SunTao
 * @since 2022-12-14
 */
public interface PageService extends IService<Page> {
    /**
     * 查询页面配置列表
     *
     * @return PageVoList
     */
    List<PageVo> queryPageList();

    /**
     * 查询页面详情
     *
     * @param pageId 页面ID
     * @return PageVo
     */
    PageVo queryPageDetail(Long pageId);

    /**
     * 新增或编辑页面
     *
     * @param pageDto 页面信息
     */
    void addOrEditPage(PageDto pageDto);

    /**
     * 删除页面
     *
     * @param pageId 页面ID
     */
    void deletePage(Long pageId);
}
