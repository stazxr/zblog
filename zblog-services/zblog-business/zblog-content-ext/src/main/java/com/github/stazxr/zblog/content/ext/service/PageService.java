package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.content.ext.domain.dto.PageDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.PageQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Page;
import com.github.stazxr.zblog.content.ext.domain.vo.PageVo;

import java.util.List;

/**
 * 页面管理业务层
 *
 * @author SunTao
 * @since 2026-06-13
 */
public interface PageService extends IService<Page> {
    /**
     * 分页查询页面列表
     *
     * @param queryDto 查询参数
     * @return IPage<PageVo>
     */
    IPage<PageVo> queryPageListByPage(PageQueryDto queryDto);

    /**
     * 查询页面列表
     *
     * @param queryDto 查询参数
     * @return List<PageVo>
     */
    List<PageVo> queryPageList(PageQueryDto queryDto);

    /**
     * 查询页面详情
     *
     * @param pageId 页面id
     * @return PageVo
     */
    PageVo queryPageDetail(Long pageId);

    /**
     * 新增页面
     *
     * @param pageDto 页面信息
     */
    void addPage(PageDto pageDto);

    /**
     * 编辑页面
     *
     * @param pageDto 页面信息
     */
    void editPage(PageDto pageDto);

    /**
     * 删除页面
     *
     * @param pageId 页面id
     */
    void deletePage(Long pageId);
}
