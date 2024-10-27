package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.converter.ArticleCategoryConverter;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.domain.dto.ArticleCategoryDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleCategoryQueryDto;
import com.github.stazxr.zblog.domain.entity.ArticleCategory;
import com.github.stazxr.zblog.domain.vo.ArticleCategoryVo;
import com.github.stazxr.zblog.mapper.ArticleCategoryMapper;
import com.github.stazxr.zblog.service.ArticleCategoryService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.math.MathUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文章类别服务实现层
 *
 * @author SunTao
 * @since 2021-01-17
 */
@Service
@RequiredArgsConstructor
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {
    private final ArticleCategoryConverter articleCategoryConverter;

    /**
     * 查询类别列表（树）
     *
     * @param queryDto 查询参数
     * @return CategoryVoList
     */
    @Override
    public List<ArticleCategoryVo> queryCategoryTreeList(ArticleCategoryQueryDto queryDto) {
        List<ArticleCategoryVo> categoryVoList = baseMapper.selectCategoryList(queryDto);
        Map<Long, List<ArticleCategoryVo>> categoryPidGroupMap = categoryVoList.stream().collect(
            Collectors.groupingBy(v -> v.getPid() == null ? Constants.TOP_ID : v.getPid())
        );

        List<ArticleCategoryVo> result = new ArrayList<>();
        Map<Long, Set<Long>> pidIdsMap = parseCategoryPidGroupMap(categoryPidGroupMap);
        Set<Long> firstPid = MathUtils.calculateFirstPid(pidIdsMap);
        for (Long pid : firstPid) {
            List<ArticleCategoryVo> categoryList = categoryPidGroupMap.get(pid);
            fetchCategoryVoChildren(categoryList, categoryPidGroupMap);
            result.addAll(categoryList);
        }

        return result;
    }

    /**
     * 查询一级类别列表
     *
     * @param queryDto 查询参数
     * @return CategoryVoList
     */
    @Override
    public List<ArticleCategoryVo> queryFirstCategoryList(ArticleCategoryQueryDto queryDto) {
        queryDto.setEnabled(true);
        queryDto.setFirstLevel(true);
        PageHelper.startPage(queryDto.getDefaultPage(), queryDto.getDefaultPageSize());
        return baseMapper.selectCategoryList(queryDto);
    }

    /**
     * 查询类别详情
     *
     * @param categoryId 类别ID
     * @return CategoryVo
     */
    @Override
    public ArticleCategoryVo queryCategoryDetail(Long categoryId) {
        Assert.notNull(categoryId, "参数【categoryId】不能为空");
        ArticleCategoryVo categoryVo = baseMapper.selectCategoryDetail(categoryId);
        Assert.notNull(categoryVo, "查询类别信息失败，类别【" + categoryId + "】不存在");
        return categoryVo;
    }

    /**
     * 新增类别
     *
     * @param categoryDto 类别信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(ArticleCategoryDto categoryDto) {
        categoryDto.setId(null);
        ArticleCategory articleCategory = articleCategoryConverter.dtoToEntity(categoryDto);
        checkArticleCategory(articleCategory);
        Assert.isTrue(baseMapper.insert(articleCategory) != 1, "新增失败");
    }

    /**
     * 编辑类别
     *
     * @param categoryDto 类别信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editCategory(ArticleCategoryDto categoryDto) {
        Assert.notNull(categoryDto.getId(), "参数【id】不能为空");
        ArticleCategory articleCategory = articleCategoryConverter.dtoToEntity(categoryDto);
        checkArticleCategory(articleCategory);
        Assert.isTrue(baseMapper.updateById(articleCategory) != 1, "修改失败");
    }

    /**
     * 删除类别
     *
     * @param categoryId 类别ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long categoryId) {
        Assert.notNull(categoryId, "参数【categoryId】不能为空");
        ArticleCategory category = baseMapper.selectById(categoryId);
        Assert.notNull(category, "待删除类别不存在，类别序列为：" + categoryId);

        ArticleCategory query = new ArticleCategory();
        query.setPid(categoryId);
        Long childCount = baseMapper.selectCount(queryBuild().eq(ArticleCategory::getPid, categoryId));
        DataValidated.isTrue(childCount > 0, "存在子节点，无法删除，请先删除子节点");

        Long articleCount = baseMapper.selectArticleCountByCategory(categoryId);
        DataValidated.isTrue(articleCount > 0, "该类别下存在文章，请先迁移文章到其他子类别");
        Assert.isTrue(baseMapper.deleteById(categoryId) != 1, "删除失败");
    }

    private void checkArticleCategory(ArticleCategory articleCategory) {
        Assert.isTrue(StringUtils.isBlank(articleCategory.getName()), "分类名称不能为空");
        Assert.notNull(articleCategory.getSort(), "分类排序不能为空");
        Assert.notNull(articleCategory.getEnabled(), "分类状态不能为空");

        ArticleCategory dbCategory = baseMapper.selectByCategoryName(articleCategory.getName());
        boolean isExist = dbCategory != null && !dbCategory.getId().equals(articleCategory.getId());
        DataValidated.isTrue(isExist, "分类名称[" + articleCategory.getName() + "]已存在");

        if (articleCategory.getId() != null) {
            if (articleCategory.getPid() == null) {
                // 如果是修改情况，且修改后为一级类别，需要校验类别下是否存在文章
                Long articleCount = baseMapper.selectArticleCountByCategory(articleCategory.getId());
                DataValidated.isTrue(articleCount > 0, "该类别下存在文章，不需要修改为一级类别，请先迁移文章到其他子类别");
            }
        }
    }

    private void fetchCategoryVoChildren(List<ArticleCategoryVo> categoryList, Map<Long, List<ArticleCategoryVo>> categoryPidGroupMap) {
        if (categoryList != null && !categoryList.isEmpty()) {
            for (ArticleCategoryVo category : categoryList) {
                List<ArticleCategoryVo> childrenList = categoryPidGroupMap.get(category.getId());
                fetchCategoryVoChildren(childrenList, categoryPidGroupMap);
                category.setChildren(childrenList);
            }
        }
    }

    private Map<Long, Set<Long>> parseCategoryPidGroupMap(Map<Long, List<ArticleCategoryVo>> categoryPidGroupMap) {
        Map<Long, Set<Long>> result = new HashMap<>(categoryPidGroupMap.size());
        for (Long pid : categoryPidGroupMap.keySet()) {
            Set<Long> ids = new HashSet<>();
            for (ArticleCategoryVo categoryVo : categoryPidGroupMap.get(pid)) {
                ids.add(categoryVo.getId());
            }
            result.put(pid, ids);
        }
        return result;
    }

    private LambdaQueryWrapper<ArticleCategory> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
