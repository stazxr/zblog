package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.converter.PageConverter;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.domain.dto.PageDto;
import com.github.stazxr.zblog.domain.entity.Page;
import com.github.stazxr.zblog.domain.vo.PageVo;
import com.github.stazxr.zblog.mapper.PageMapper;
import com.github.stazxr.zblog.service.PageService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 页面服务实现层
 *
 * @author SunTao
 * @since 2022-12-14
 */
@Service
@RequiredArgsConstructor
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {
    private final PageConverter pageConverter;

    /**
     * 查询页面配置列表
     *
     * @return PageVoList
     */
    @Override
    public List<PageVo> queryPageList() {
        return baseMapper.selectPageList();
    }

    /**
     * 查询页面详情
     *
     * @param pageId 页面ID
     * @return PageVo
     */
    @Override
    public PageVo queryPageDetail(Long pageId) {
        Assert.notNull(pageId, "参数【pageId】不能为空");
        PageVo pageVo = baseMapper.selectPageDetail(pageId);
        Assert.notNull(pageVo, "查询页面信息失败，页面【" + pageId + "】不存在");
        return pageVo;
    }

    /**
     * 新增或编辑页面
     *
     * @param pageDto 页面信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrEditPage(PageDto pageDto) {
        Page page = pageConverter.dtoToEntity(pageDto);
        checkPage(page);
        Assert.nonTrue(saveOrUpdate(page), "操作失败");
    }

    /**
     * 删除页面
     *
     * @param pageId 页面ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePage(Long pageId) {
        Assert.notNull(pageId, "参数【pageId】不能为空");
        Assert.isTrue(baseMapper.deleteById(pageId) != 1, "删除失败");
    }

    private void checkPage(Page page) {
        if (page.getId() == null) {
            page.setId(GenerateIdUtils.getId());
        }

        Assert.isTrue(StringUtils.isBlank(page.getPageName()), "页面名称不能为空");
        Assert.isTrue(StringUtils.isBlank(page.getPageLabel()), "页面标签不能为空");
        Assert.isTrue(StringUtils.isBlank(page.getPageCover()), "页面封面不能为空");
        Assert.notNull(page.getPageSort(), "页面排序不能为空");

        // 参数处理
        page.setPageLabel(page.getPageLabel().trim());
        page.setPageSort(page.getPageSort() == null ? Constants.DEFAULT_SORT : page.getPageSort());
    }
}
