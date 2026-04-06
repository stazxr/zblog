package com.github.stazxr.zblog.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.content.converter.TagConverter;
import com.github.stazxr.zblog.content.domain.dto.TagDto;
import com.github.stazxr.zblog.content.domain.dto.query.TagQueryDto;
import com.github.stazxr.zblog.content.domain.entity.Tag;
import com.github.stazxr.zblog.content.domain.error.TagErrorCode;
import com.github.stazxr.zblog.content.domain.vo.TagVo;
import com.github.stazxr.zblog.content.mapper.TagMapper;
import com.github.stazxr.zblog.content.service.TagService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章标签业务实现层
 *
 * @author SunTao
 * @since 2021-01-17
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    private final TagConverter tagConverter;

    /**
     * 分页查询标签列表
     *
     * @param queryDto 查询参数
     * @return IPage<TagVo>
     */
    @Override
    public IPage<TagVo> queryTagListByPage(TagQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getName())) {
            queryDto.setName(queryDto.getName().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getSlug())) {
            queryDto.setSlug(queryDto.getSlug().trim());
        }
        // 分页查询
        Page<TagVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return baseMapper.selectTagList(page, queryDto);
    }

    /**
     * 查询标签详情
     *
     * @param tagId 标签ID
     * @return TagVo
     */
    @Override
    public TagVo queryTagDetail(Long tagId) {
        TagVo tagVo = baseMapper.selectTagDetail(tagId);
        return ThrowUtils.requireNonNull(tagVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 新增标签
     *
     * @param tagDto 标签信息
     */
    @Override
    public void addTag(TagDto tagDto) {
        // 获取标签信息
        Tag tag = tagConverter.dtoToEntity(tagDto);
        // 新增时，不允许传入 TagId
        ThrowUtils.when(tag.getId() != null).system(BaseErrorCode.SCOREB001);
        // 标签信息检查
        checkTag(tag);
        // 新增标签
        ThrowUtils.when(!save(tag)).system(BaseErrorCode.SCOREA001);
    }

    /**
     * 编辑标签
     *
     * @param tagDto 标签信息
     */
    @Override
    public void editTag(TagDto tagDto) {
        // 获取标签信息
        Tag tag = tagConverter.dtoToEntity(tagDto);
        // 判断标签是否存在
        Tag dbTag = baseMapper.selectById(tag.getId());
        ThrowUtils.throwIfNull(dbTag, BaseErrorCode.ECOREA001);
        // 标签信息检查
        checkTag(tag);
        // 编辑标签
        ThrowUtils.when(!updateById(tag)).system(BaseErrorCode.SCOREA002);
    }

    /**
     * 删除标签
     *
     * @param tagId 标签ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long tagId) {
        // 判断标签是否存在
        TagVo dbTag = queryTagDetail(tagId);
        // 判断标签是否关联文章
        ThrowUtils.when(dbTag.getArticleCount() > 0).service(TagErrorCode.ETAGSA002);
        // 删除标签
        ThrowUtils.when(!removeById(tagId)).system(BaseErrorCode.SCOREA003);
    }

    private void checkTag(Tag tag) {
        // 检查标签名称
        tag.setName(tag.getName().trim());
        ThrowUtils.when(checkTagNameExist(tag)).service(TagErrorCode.ETAGSA000);

        // 检查标签编码
        tag.setSlug(tag.getSlug().trim());
        ThrowUtils.throwIf(checkTagSlugExist(tag), TagErrorCode.ETAGSA001);
    }

    private boolean checkTagNameExist(Tag tag) {
        if (tag.getName() != null) {
            LambdaQueryWrapper<Tag> queryWrapper = queryBuild().eq(Tag::getName, tag.getName());
            if (tag.getId() != null) {
                queryWrapper.ne(Tag::getId, tag.getId());
            }
            return baseMapper.exists(queryWrapper);
        }
        return false;
    }

    private boolean checkTagSlugExist(Tag tag) {
        if (tag.getSlug() != null) {
            LambdaQueryWrapper<Tag> queryWrapper = queryBuild().eq(Tag::getSlug, tag.getSlug());
            if (tag.getId() != null) {
                queryWrapper.ne(Tag::getId, tag.getId());
            }
            return baseMapper.exists(queryWrapper);
        }
        return false;
    }

    private LambdaQueryWrapper<Tag> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
