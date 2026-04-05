package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.entity.FileRelation;
import com.github.stazxr.zblog.base.mapper.FileMapper;
import com.github.stazxr.zblog.base.mapper.FileRelationMapper;
import com.github.stazxr.zblog.converter.CategoryConverter;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.domain.dto.CategoryDto;
import com.github.stazxr.zblog.domain.dto.query.CategoryQueryDto;
import com.github.stazxr.zblog.domain.entity.Category;
import com.github.stazxr.zblog.domain.enums.ServiceUploadBusinessType;
import com.github.stazxr.zblog.domain.error.CategoryErrorCode;
import com.github.stazxr.zblog.domain.vo.CategoryVo;
import com.github.stazxr.zblog.mapper.CategoryMapper;
import com.github.stazxr.zblog.service.CategoryService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.math.MathUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 分类管理业务实现层
 *
 * @author SunTao
 * @since 2021-01-17
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    private final CategoryConverter categoryConverter;

    private final FileMapper fileMapper;

    private final FileRelationMapper fileRelationMapper;

    /**
     * 查询分类树列表
     *
     * @param queryDto 查询参数
     * @return List<CategoryVo>
     */
    @Override
    public List<CategoryVo> queryCategoryTree(CategoryQueryDto queryDto) {
        // 参数检查
        if (StringUtils.isNotBlank(queryDto.getName())) {
            queryDto.setName(queryDto.getName().trim());
        }

        // 查询分类列表
        List<CategoryVo> categoryVos = baseMapper.selectCategoryList(queryDto);

        // 按 PID 对分类进行分组
        Map<Long, List<CategoryVo>> categoryPidGroupMap = categoryVos.stream().collect(
            Collectors.groupingBy(CategoryVo::getPid)
        );

        // 构建分类树
        List<CategoryVo> result = new ArrayList<>();
        Map<Long, Set<Long>> pidIdsMap = parseCategoryPidGroupMap(categoryPidGroupMap);
        Set<Long> firstPid = MathUtils.calculateFirstPid(pidIdsMap);
        for (Long pid : firstPid) {
            List<CategoryVo> categoryList = categoryPidGroupMap.get(pid);
            fetchCategoryVoChildren(categoryList, categoryPidGroupMap);
            result.addAll(categoryList);
        }

        // 返回分类树
        return result;
    }

    /**
     * 查询一级分类列表
     *
     * @param queryDto 查询参数
     * @return List<CategoryVo>
     */
    @Override
    public List<CategoryVo> queryFirstCategoryList(CategoryQueryDto queryDto) {
        queryDto.setPid(0L);
        queryDto.setEnabled(true);
        return baseMapper.selectCategoryList(queryDto);
    }

    /**
     * 查询分类详情
     *
     * @param categoryId 分类id
     * @return CategoryVo
     */
    @Override
    public CategoryVo queryCategoryDetail(Long categoryId) {
        CategoryVo categoryVo = baseMapper.selectCategoryDetail(categoryId);
        return ThrowUtils.requireNonNull(categoryVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 新增分类
     *
     * @param categoryDto 分类
     */
    @Override
    public void addCategory(CategoryDto categoryDto) {
        // 获取分类信息
        Long imageId = categoryDto.getImageId();
        Category category = categoryConverter.dtoToEntity(categoryDto);
        // 新增时，不允许传入 CategoryId
        ThrowUtils.when(category.getId() != null).system(BaseErrorCode.SCOREB001);
        // 分类信息检查
        category.setId(SequenceUtils.getId());
        checkCategory(category, imageId);
        // 新增分类
        ThrowUtils.when(!save(category)).system(BaseErrorCode.SCOREA001);
    }

    /**
     * 编辑分类
     *
     * @param categoryDto 分类
     */
    @Override
    public void editCategory(CategoryDto categoryDto) {
        // 获取分类信息
        Long imageId = categoryDto.getImageId();
        Category category = categoryConverter.dtoToEntity(categoryDto);
        // 判断分类是否存在
        Category dbCategory = baseMapper.selectById(category.getId());
        ThrowUtils.throwIfNull(dbCategory, BaseErrorCode.ECOREA001);
        // 分类信息检查
        checkCategory(category, imageId);
        // 编辑角色
        ThrowUtils.when(!updateById(category)).system(BaseErrorCode.SCOREA002);
    }

    /**
     * 删除分类
     *
     * @param categoryId 分类id
     */
    @Override
    public void deleteCategory(Long categoryId) {
        // 判断分类是否存在
        CategoryVo dbCategory = queryCategoryDetail(categoryId);
        // 判断分类是否存在子分类
        ThrowUtils.when(dbCategory.getHasChildren()).service(CategoryErrorCode.ECATEA002);
        // 判断分类是否关联文章
        ThrowUtils.when(dbCategory.getArticleCount() > 0).service(CategoryErrorCode.ECATEA003);
        // 删除分类
        ThrowUtils.when(!removeById(categoryId)).system(BaseErrorCode.SCOREA003);
    }

    private Map<Long, Set<Long>> parseCategoryPidGroupMap(Map<Long, List<CategoryVo>> categoryPidGroupMap) {
        Map<Long, Set<Long>> result = new HashMap<>(categoryPidGroupMap.size());
        for (Long pid : categoryPidGroupMap.keySet()) {
            Set<Long> ids = new HashSet<>();
            for (CategoryVo categoryVo : categoryPidGroupMap.get(pid)) {
                ids.add(categoryVo.getId());
            }
            result.put(pid, ids);
        }
        return result;
    }

    private void fetchCategoryVoChildren(List<CategoryVo> categoryList, Map<Long, List<CategoryVo>> categoryPidGroupMap) {
        if (categoryList != null && !categoryList.isEmpty()) {
            for (CategoryVo category : categoryList) {
                List<CategoryVo> childrenList = categoryPidGroupMap.get(category.getId());
                fetchCategoryVoChildren(childrenList, categoryPidGroupMap);
                category.setChildren(childrenList);
            }
        }
    }

    private void checkCategory(Category category, Long imageId) {
        // 检查分类名称
        category.setName(category.getName().trim());
        ThrowUtils.when(checkCategoryNameExist(category)).service(CategoryErrorCode.ECATEA000);

        // 检查分类编码
        category.setSlug(category.getSlug().trim());
        ThrowUtils.throwIf(checkCategorySlugExist(category), CategoryErrorCode.ECATEA001);

        // 图片信息检查
        Long categoryId = category.getId();
        if (category.getImageUrl() == null && imageId == null) {
            fileMapper.deleteByBusinessInfo(categoryId, ServiceUploadBusinessType.CATEGORY_IMG);
            fileRelationMapper.deleteByBusinessInfo(categoryId, ServiceUploadBusinessType.CATEGORY_IMG);
        }
        if (imageId != null) {
            // 新增或修改了分类图
            fileMapper.deleteByBusinessInfo(categoryId, ServiceUploadBusinessType.CATEGORY_IMG);
            fileRelationMapper.deleteByBusinessInfo(categoryId, ServiceUploadBusinessType.CATEGORY_IMG);
            FileRelation fileRelation = new FileRelation();
            fileRelation.setFileId(imageId);
            fileRelation.setBusinessId(categoryId);
            fileRelation.setBusinessType(ServiceUploadBusinessType.CATEGORY_IMG);
            fileRelationMapper.insert(fileRelation);
        } else {
            // 没有设置或删除了分类图
            if (category.getImageUrl() == null) {
                fileMapper.deleteByBusinessInfo(categoryId, ServiceUploadBusinessType.CATEGORY_IMG);
                fileRelationMapper.deleteByBusinessInfo(categoryId, ServiceUploadBusinessType.CATEGORY_IMG);
            }
        }
    }

    private boolean checkCategoryNameExist(Category category) {
        if (category.getName() != null) {
            LambdaQueryWrapper<Category> queryWrapper = queryBuild().eq(Category::getName, category.getName());
            if (category.getId() != null) {
                queryWrapper.ne(Category::getId, category.getId());
            }
            return baseMapper.exists(queryWrapper);
        }
        return false;
    }

    private boolean checkCategorySlugExist(Category category) {
        if (category.getSlug() != null) {
            LambdaQueryWrapper<Category> queryWrapper = queryBuild().eq(Category::getSlug, category.getSlug());
            if (category.getId() != null) {
                queryWrapper.ne(Category::getId, category.getId());
            }
            return baseMapper.exists(queryWrapper);
        }
        return false;
    }

    private LambdaQueryWrapper<Category> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
