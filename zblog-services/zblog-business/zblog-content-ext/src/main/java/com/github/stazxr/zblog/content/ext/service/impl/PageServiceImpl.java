package com.github.stazxr.zblog.content.ext.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.content.ext.converter.PageConverter;
import com.github.stazxr.zblog.content.ext.domain.dto.PageDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.PageQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Page;
import com.github.stazxr.zblog.content.ext.domain.error.PageErrorCode;
import com.github.stazxr.zblog.content.ext.domain.vo.PageVo;
import com.github.stazxr.zblog.content.ext.mapper.PageMapper;
import com.github.stazxr.zblog.content.ext.service.PageService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 页面管理业务实现层
 *
 * @author SunTao
 * @since 2026-06-13
 */
@Service
@RequiredArgsConstructor
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {
    private final PageConverter pageConverter;

    /**
     * 分页查询页面列表
     *
     * @param queryDto 查询参数
     * @return IPage<PageVo>
     */
    @Override
    public IPage<PageVo> queryPageListByPage(PageQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getPageName())) {
            queryDto.setPageName(queryDto.getPageName().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getPageLabel())) {
            queryDto.setPageLabel(queryDto.getPageLabel().trim());
        }

        // 分页查询
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<PageVo> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(queryDto.getPage(), queryDto.getPageSize());
        return baseMapper.selectPageList(page, queryDto);
    }

    /**
     * 查询页面详情
     *
     * @param pageId 页面id
     * @return PageVo
     */
    @Override
    public PageVo queryPageDetail(Long pageId) {
        PageVo pageVo = baseMapper.selectPageDetail(pageId);
        return ThrowUtils.requireNonNull(pageVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 新增页面
     *
     * @param pageDto 页面信息
     */
    @Override
    public void addPage(PageDto pageDto) {
        // 获取页面信息
        Page page = pageConverter.dtoToEntity(pageDto);
        // 新增时，不允许传入 PageId
        ThrowUtils.when(page.getId() != null).system(BaseErrorCode.SCOREB001);
        // 页面信息检查
        checkPage(page);
        // 新增页面
        ThrowUtils.when(!save(page)).system(BaseErrorCode.SCOREA001);
    }

    /**
     * 编辑页面
     *
     * @param pageDto 页面信息
     */
    @Override
    public void editPage(PageDto pageDto) {
        // 获取页面信息
        Page page = pageConverter.dtoToEntity(pageDto);
        // 判断页面是否存在
        Page dbPage = baseMapper.selectById(page.getId());
        ThrowUtils.throwIfNull(dbPage, BaseErrorCode.ECOREA001);
        // 页面信息检查
        checkPage(page);
        // 编辑页面
        ThrowUtils.when(!updateById(page)).system(BaseErrorCode.SCOREA002);
    }

    /**
     * 删除页面
     *
     * @param pageId 页面id
     */
    @Override
    public void deletePage(Long pageId) {
        // 判断页面是否存在
        Page dbPage = baseMapper.selectById(pageId);
        ThrowUtils.throwIfNull(dbPage, BaseErrorCode.ECOREA001);
        // 删除页面
        ThrowUtils.when(!removeById(pageId)).system(BaseErrorCode.SCOREA003);
    }

    private void checkPage(Page page) {
        // 检查页面标签
        page.setPageLabel(page.getPageLabel().trim());
        ThrowUtils.throwIf(checkPageLabelExist(page), PageErrorCode.EPAGEA001);
    }

    private boolean checkPageLabelExist(Page page) {
        if (page.getPageLabel() != null) {
            LambdaQueryWrapper<Page> queryWrapper = queryBuild().eq(Page::getPageLabel, page.getPageLabel());
            if (page.getId() != null) {
                queryWrapper.ne(Page::getId, page.getId());
            }
            return baseMapper.exists(queryWrapper);
        }
        return false;
    }

    private LambdaQueryWrapper<Page> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
