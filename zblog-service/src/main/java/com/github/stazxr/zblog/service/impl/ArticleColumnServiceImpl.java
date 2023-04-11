package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.converter.ArticleColumnConverter;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.domain.dto.ArticleColumnArticleDto;
import com.github.stazxr.zblog.domain.dto.ArticleColumnDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleColumnQueryDto;
import com.github.stazxr.zblog.domain.entity.ArticleCategory;
import com.github.stazxr.zblog.domain.entity.ArticleColumn;
import com.github.stazxr.zblog.domain.entity.ArticleColumnRelation;
import com.github.stazxr.zblog.domain.vo.ArticleColumnArticleVo;
import com.github.stazxr.zblog.domain.vo.ArticleColumnVo;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import com.github.stazxr.zblog.mapper.ArticleColumnMapper;
import com.github.stazxr.zblog.mapper.ArticleColumnRelationMapper;
import com.github.stazxr.zblog.service.ArticleColumnService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
     * 分页查询专栏列表
     *
     * @param queryDto 查询参数
     * @return ColumnVoList
     */
    @Override
    public PageInfo<ArticleColumnVo> queryColumnListByPage(ArticleColumnQueryDto queryDto) {
        queryDto.checkPage();
        PageMethod.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(baseMapper.selectColumnList(queryDto));
    }

    /**
     * 查询栏目详情
     *
     * @param columnId 专栏id
     * @return ArticleColumnVo
     */
    @Override
    public ArticleColumnVo queryColumnDetail(Long columnId) {
        Assert.notNull(columnId, "参数【columnId】不能为空");
        ArticleColumnVo columnVo = baseMapper.selectColumnDetail(columnId);
        Assert.notNull(columnVo, "查询栏目信息失败，栏目【" + columnId + "】不存在");

        // 查询文章列表
        List<ArticleColumnArticleVo> articles = articleColumnRelationMapper.selectArticleList(columnId);
        columnVo.setArticles(articles);
        return columnVo;
    }

    /**
     * 新增专栏
     *
     * @param columnDto 专栏信息
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
     * 编辑专栏
     *
     * @param columnDto 专栏信息
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
     * 配置专栏
     *
     * @param columnDto 专栏信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void configColumn(ArticleColumnDto columnDto) {
        Assert.notNull(columnDto.getId(), "参数【id】不能为空");

        // 插入专栏文章信息
        articleColumnRelationMapper.deleteByColumnId(columnDto.getId());
        List<ArticleColumnArticleDto> articleList = columnDto.getArticleList();
        if (articleList != null && !articleList.isEmpty()) {
            String loginUsername = SecurityUtils.getLoginUsername();
            List<ArticleColumnRelation> relations = new ArrayList<>();
            articleList.forEach(article -> {
                Assert.notNull(article.getArticleId(), "参数【articleId】不能为空");
                Assert.notBlank(article.getArticleTitle(), "请检查文章标题，文章标题不能为空");

                ArticleColumnRelation relation = new ArticleColumnRelation();
                relation.setId(GenerateIdUtils.getId());
                relation.setColumnId(columnDto.getId());
                relation.setArticleId(article.getArticleId());
                relation.setArticleTitle(article.getArticleTitle());
                relation.setSort(article.getSort());
                relation.setCreateUser(loginUsername);
                relation.setCreateDate(DateUtils.formatDate());
                relation.setCreateTime(DateUtils.formatTime());
                relations.add(relation);
            });

            articleColumnRelationMapper.batchInsert(relations);
        }
    }

    /**
     * 删除专栏
     *
     * @param columnId 专栏id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteColumn(Long columnId) {
        Assert.notNull(columnId, "参数【columnId】不能为空");
        Assert.isTrue(baseMapper.deleteById(columnId) != 1, "删除失败");

        // 有关联的文章不允许删除
        Integer articleCount = articleColumnRelationMapper.selectArticleCount(columnId);
        DataValidated.isTrue(articleCount > 0, "该专栏下存在文章，无法删除");

        // 删除中间表的数据
        articleColumnRelationMapper.deleteByColumnId(columnId);
    }

    /**
     * 查询非专栏对应的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVo
     */
    @Override
    public List<ArticleVo> queryArticleListNotColumn(ArticleColumnQueryDto queryDto) {
        return baseMapper.selectArticleListNotColumn(queryDto);
    }

    private void checkArticleColumn(ArticleColumn articleColumn) {
        Assert.isTrue(StringUtils.isBlank(articleColumn.getName()), "请填写专栏名称");
        Assert.notNull(articleColumn.getSort(), "请填写专栏排序");
        Assert.notNull(articleColumn.getPageShow(), "请选择是否首页展示");
        Assert.notNull(articleColumn.getEnabled(), "请选择专栏状态");

        ArticleCategory dbColumn = baseMapper.selectByColumnName(articleColumn.getName());
        boolean isExist = dbColumn != null && !dbColumn.getId().equals(articleColumn.getId());
        DataValidated.isTrue(isExist, "专栏名称[" + articleColumn.getName() + "]已存在");
    }
}
