package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.converter.ArticleColumnConverter;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.domain.dto.ArticleColumnDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleColumnQueryDto;
import com.github.stazxr.zblog.domain.entity.ArticleCategory;
import com.github.stazxr.zblog.domain.entity.ArticleColumn;
import com.github.stazxr.zblog.domain.vo.ArticleColumnVo;
import com.github.stazxr.zblog.mapper.ArticleColumnMapper;
import com.github.stazxr.zblog.mapper.ArticleColumnRelationMapper;
import com.github.stazxr.zblog.service.ArticleColumnService;
import com.github.stazxr.zblog.util.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章栏目业务实现层
 *
 * @author SunTao
 * @since 2022-11-22
 */
@Service
@RequiredArgsConstructor
public class ArticleColumnServiceImpl extends ServiceImpl<ArticleColumnMapper, ArticleColumn> implements ArticleColumnService {
    private final ArticleColumnRelationMapper articleColumnRelationMapper;

    private final ArticleColumnConverter articleColumnConverter;

    /**
     * 查询栏目列表
     *
     * @param queryDto 查询参数
     * @return ColumnVoList
     */
    @Override
    public PageInfo<ArticleColumnVo> queryColumnListByPage(ArticleColumnQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(baseMapper.selectColumnList(queryDto));
    }

    /**
     * 查询栏目详情
     *
     * @param columnId 标签ID
     * @return ColumnVo
     */
    @Override
    public ArticleColumnVo queryColumnDetail(Long columnId) {
        Assert.notNull(columnId, "参数【columnId】不能为空");
        ArticleColumnVo columnVo = baseMapper.selectColumnDetail(columnId);
        Assert.notNull(columnVo, "查询栏目信息失败，栏目【" + columnId + "】不存在");
        return columnVo;
    }

    /**
     * 新增栏目
     *
     * @param columnDto 栏目信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addColumn(ArticleColumnDto columnDto) {
        columnDto.setId(null);
        ArticleColumn articleColumn = articleColumnConverter.dtoToEntity(columnDto);
        checkArticleColumn(articleColumn);
        Assert.isTrue(baseMapper.insert(articleColumn) != 1, "新增失败");
    }

    /**
     * 编辑栏目
     *
     * @param columnDto 栏目信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editColumn(ArticleColumnDto columnDto) {
        Assert.notNull(columnDto.getId(), "参数【id】不能为空");
        ArticleColumn articleColumn = articleColumnConverter.dtoToEntity(columnDto);
        checkArticleColumn(articleColumn);
        Assert.isTrue(baseMapper.updateById(articleColumn) != 1, "修改失败");
    }

    /**
     * 删除栏目
     *
     * @param columnId 栏目ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteColumn(Long columnId) {
        Assert.notNull(columnId, "参数【columnId】不能为空");
        Assert.isTrue(baseMapper.deleteById(columnId) != 1, "删除失败");

        // 删除中间表的数据
        articleColumnRelationMapper.deleteByColumnId(columnId);
    }

    private void checkArticleColumn(ArticleColumn articleColumn) {
        Assert.notNull(articleColumn.getName(), "栏目名称不能为空");
        Assert.notNull(articleColumn.getSort(), "栏目排序不能为空");
        Assert.notNull(articleColumn.getEnabled(), "栏目状态不能为空");

        ArticleCategory dbColumn = baseMapper.selectByColumnName(articleColumn.getName());
        boolean isExist = dbColumn != null && !dbColumn.getId().equals(articleColumn.getId());
        DataValidated.isTrue(isExist, "栏目名称[" + articleColumn.getName() + "]已存在");
    }
}
