package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.converter.ArticleTagConverter;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.domain.dto.ArticleTagDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleTagQueryDto;
import com.github.stazxr.zblog.domain.entity.ArticleTag;
import com.github.stazxr.zblog.domain.enums.ArticleTagType;
import com.github.stazxr.zblog.domain.vo.ArticleTagVo;
import com.github.stazxr.zblog.mapper.ArticleTagMapper;
import com.github.stazxr.zblog.mapper.ArticleTagRelationMapper;
import com.github.stazxr.zblog.service.ArticleTagService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文章标签业务实现层
 *
 * @author SunTao
 * @since 2021-01-17
 */
@Service
@RequiredArgsConstructor
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
    private final ArticleTagRelationMapper articleTagRelationMapper;

    private final ArticleTagConverter articleTagConverter;

    /**
     * 分页查询标签列表
     *
     * @param queryDto 查询参数
     * @return TagVoList
     */
    @Override
    public PageInfo<ArticleTagVo> queryTagListByPage(ArticleTagQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(baseMapper.selectTagList(queryDto));
    }

    /**
     * 查询标签列表
     *
     * @param queryDto 查询参数
     * @return TagVoList
     */
    @Override
    public List<ArticleTagVo> queryTagList(ArticleTagQueryDto queryDto) {
        // 查询当前用户信息
        return baseMapper.selectTagList(queryDto);
    }

    /**
     * 查询标签详情
     *
     * @param tagId 标签ID
     * @return TagVo
     */
    @Override
    public ArticleTagVo queryTagDetail(Long tagId) {
        Assert.notNull(tagId, "参数【tagId】不能为空");
        ArticleTagVo tagVo = baseMapper.selectTagDetail(tagId);
        Assert.notNull(tagVo, "查询标签信息失败，标签【" + tagId + "】不存在");
        return tagVo;
    }

    /**
     * 新增标签
     *
     * @param tagDto 标签信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTag(ArticleTagDto tagDto) {
        tagDto.setId(null);
        ArticleTag articleTag = articleTagConverter.dtoToEntity(tagDto);
        checkArticleTag(articleTag);
        Assert.isTrue(baseMapper.insert(articleTag) != 1, "新增失败");
    }

    /**
     * 编辑标签
     *
     * @param tagDto 标签信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editTag(ArticleTagDto tagDto) {
        Assert.notNull(tagDto.getId(), "参数【id】不能为空");
        ArticleTag articleTag = articleTagConverter.dtoToEntity(tagDto);
        checkArticleTag(articleTag);
        Assert.isTrue(baseMapper.updateById(articleTag) != 1, "修改失败");
    }

    /**
     * 删除标签
     *
     * @param tagId 标签ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long tagId) {
        Assert.notNull(tagId, "参数【tagId】不能为空");

        // 校验文章数
        Long articleCount = baseMapper.selectArticleCountByTag(tagId);
        DataValidated.isTrue(articleCount > 0, "该标签下存在文章，无法删除");
        Assert.isTrue(baseMapper.deleteById(tagId) != 1, "删除失败");

        // 删除中间表的数据
        articleTagRelationMapper.deleteByTagId(tagId);
    }

    private void checkArticleTag(ArticleTag articleTag) {
        Assert.isTrue(StringUtils.isBlank(articleTag.getName()), "标签名称不能为空");
        Assert.notNull(articleTag.getEnabled(), "标签状态不能为空");

        // 新增检查标签类型
        if (articleTag.getId() == null) {
            Assert.notNull(ArticleTagType.of(articleTag.getType()), "标签类型不正确，取值范围[1, 2]");
        }

        ArticleTag dbTag = baseMapper.selectByTagName(articleTag.getName());
        boolean isExist = dbTag != null && !dbTag.getId().equals(articleTag.getId());
        DataValidated.isTrue(isExist, "标签名称[" + articleTag.getName() + "]已存在");
    }
}
