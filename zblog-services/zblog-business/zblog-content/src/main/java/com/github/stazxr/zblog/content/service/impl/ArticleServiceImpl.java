package com.github.stazxr.zblog.content.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ServiceException;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.file.util.MockMultipartFile;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.dto.UploadFileDto;
import com.github.stazxr.zblog.base.domain.entity.FileRelation;
import com.github.stazxr.zblog.base.domain.vo.UploadFileVo;
import com.github.stazxr.zblog.base.mapper.FileMapper;
import com.github.stazxr.zblog.base.mapper.FileRelationMapper;
import com.github.stazxr.zblog.base.service.FileService;
import com.github.stazxr.zblog.content.domain.dto.ArticleAutoSaveDto;
import com.github.stazxr.zblog.content.domain.dto.ArticleDraftDto;
import com.github.stazxr.zblog.content.domain.dto.ArticleDto;
import com.github.stazxr.zblog.content.domain.dto.query.ArticleAutoSaveQueryDto;
import com.github.stazxr.zblog.content.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.content.domain.entity.*;
import com.github.stazxr.zblog.content.domain.enums.*;
import com.github.stazxr.zblog.content.domain.error.ArticleErrorCode;
import com.github.stazxr.zblog.content.domain.vo.ArticleAutoSaveVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleCountVo;
import com.github.stazxr.zblog.content.domain.vo.ArticleVo;
import com.github.stazxr.zblog.content.mapper.*;
import com.github.stazxr.zblog.content.service.ArticleService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.SlugUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import com.github.stazxr.zblog.util.graphics.ImageBuilderUtils;
import com.github.stazxr.zblog.util.http.MarkdownWordCountUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.math.MathUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文章管理业务实现层
 *
 * @author SunTao
 * @since 2021-02-23
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final TagMapper tagMapper;

    private final ArticleTagMapper articleTagMapper;

    private final ArticleImageMapper articleImageMapper;

    private final ArticleContentDraftRecordMapper articleContentDraftRecordMapper;

    private final FileMapper fileMapper;

    private final FileRelationMapper fileRelationMapper;

    private final FileService fileService;

    /**
     * 分页查询我的文章列表
     *
     * @param queryDto 查询参数
     * @return IPage<ArticleVo>
     */
    @Override
    public IPage<ArticleVo> queryMyArticleListByPage(ArticleQueryDto queryDto) {
        // 设置用户ID
        Long loginId = SecurityUtils.getLoginId();
        queryDto.setAuthorId(loginId);

        // 参数检查
        queryDto.checkPage();
        ThrowUtils.throwIfNull(queryDto.getTagStatus(), ArticleErrorCode.EARTIA001);
        if (StringUtils.isNotBlank(queryDto.getTitle())) {
            queryDto.setTitle(queryDto.getTitle().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getSlug())) {
            queryDto.setSlug(queryDto.getSlug().trim());
        }

        // 分页查询
        Page<ArticleVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return baseMapper.selectMyArticleList(page, queryDto);
    }

    /**
     * 查询我的文章数量统计信息
     *
     * @return ArticleCountVo
     */
    @Override
    public ArticleCountVo queryMyArticleCountInfo() {
        Long loginId = SecurityUtils.getLoginId();
        ArticleCountVo articleCountVo = baseMapper.selectMyArticleCountInfo(loginId);
        if (articleCountVo == null) {
            articleCountVo = new ArticleCountVo();
        }
        return articleCountVo;
    }

    /**
     * 查询我的文章详情
     *
     * @param articleId 文章id
     * @return ArticleVo
     */
    @Override
    public ArticleVo queryMyArticleDetail(Long articleId) {
        Long loginId = SecurityUtils.getLoginId();
        ArticleVo articleVo = baseMapper.selectArticleDetail(articleId);
        if (articleVo != null) {
            // 只能查询自己的文章
            ThrowUtils.throwIf(!loginId.equals(articleVo.getAuthorId()), ArticleErrorCode.EARTIA005);

            // 查询标签列表
            List<Long> tagIds = articleTagMapper.selectTagIdsByArticleId(articleVo.getId());
            articleVo.setTags(tagIds);

            // 查询封面列表
            if (ArticleCoverImageType.SINGLE.getValue().equals(articleVo.getCoverImageType()) ||
                    ArticleCoverImageType.MULTIPLE.getValue().equals(articleVo.getCoverImageType()) ||
                    ArticleCoverImageType.TITLE.getValue().equals(articleVo.getCoverImageType())) {
                List<UploadFileVo> coverImages = fileRelationMapper.selectByBusinessInfo(articleId, ServiceUploadBusinessType.ARTICLE_COVER_IMG);
                articleVo.setCoverImages(coverImages);
            }
        }
        return ThrowUtils.requireNonNull(articleVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 新增文章
     *
     * @param articleDto 文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArticle(ArticleDto articleDto) {
        // 标签校验
        checkArticleTags(articleDto.getId(), articleDto.getTags());
        // 封面校验
        checkArticleCoverImages(articleDto.getId(), articleDto.getTitle(), articleDto.getCoverImageType(), articleDto.getCoverImages());
        // 文章类型校验
        checkArticleType(articleDto);
    }

    /**
     * 编辑文章
     *
     * @param articleDto 文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editArticle(ArticleDto articleDto) {

    }

    /**
     * 文章内容自动保存
     *
     * @param autoSaveDto 文章自动保存信息
     * @return 自动保存时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String autoSaveArticleContent(ArticleAutoSaveDto autoSaveDto) {
        try {
            String saveTime = DateUtils.formatNow("yyyy-MM-dd HH:mm:ss");
            if (autoSaveDto.getArticleId() != null && StringUtils.isNotBlank(autoSaveDto.getContent())) {
                ArticleContentDraftRecord record = new ArticleContentDraftRecord();
                record.setId(SequenceUtils.getId());
                record.setArticleId(autoSaveDto.getArticleId());
                record.setTitle(autoSaveDto.getTitle());
                record.setSlug(autoSaveDto.getSlug());
                record.setSummary(autoSaveDto.getSummary());
                record.setContent(autoSaveDto.getContent());
                record.setWordCount(autoSaveDto.getWordCount());
                record.setSaveTime(saveTime);
                articleContentDraftRecordMapper.insert(record);
                return saveTime;
            }
        } catch (Exception e) {
            log.error("文章内容自动保存功能异常", e);
        }

        return null;
    }

    /**
     * 分页查询文章内容自动保存列表
     *
     * @param queryDto 查询参数
     * @return IPage<ArticleAutoSaveVo>
     */
    @Override
    public IPage<ArticleAutoSaveVo> queryAutoSaveArticleContentByPage(ArticleAutoSaveQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        ThrowUtils.throwIfNull(queryDto.getArticleId(), BaseErrorCode.SCOREB001);

        // 分页查询
        Page<ArticleAutoSaveVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return articleContentDraftRecordMapper.selectAutoSaveArticleContentList(page, queryDto);
    }

    /**
     * 查询文章内容自动保存详情
     *
     * @param recordId 保存记录id
     * @return ArticleAutoSaveVo
     */
    @Override
    public ArticleAutoSaveVo queryAutoSaveArticleContentById(Long recordId) {
        ThrowUtils.throwIfNull(recordId, BaseErrorCode.SCOREB001);
        return articleContentDraftRecordMapper.selectAutoSaveArticleContentById(recordId);
    }

    /**
     * 保存文章草稿
     *
     * @param articleDto 文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveArticleDraft(ArticleDraftDto articleDto) {
        Long articleId = articleDto.getId(); // 文章ID
        Long loginId = SecurityUtils.getLoginId(); // 当前登录用户
        LocalDateTime now = LocalDateTime.now(); // 当前时间

        // 判断是新增场景还是保存场景
        Article article = baseMapper.selectById(articleId);
        if (article == null) {
            // 新增场景
            article = new Article();
            article.setId(articleId);
            if (StringUtils.isBlank(articleDto.getTitle())) {
                article.setTitle("未命名文章");
            } else {
                article.setTitle(articleDto.getTitle());
            }
            article.setSlug(articleDto.getSlug());
            article.setSummary(articleDto.getSummary());
            article.setContentMd(articleDto.getContentMd());
            article.setCategoryId(articleDto.getCategoryId());
            article.setAuthorId(loginId);
            article.setSeoTitle(articleDto.getSeoTitle());
            article.setSeoKeywords(articleDto.getSeoKeywords());
            article.setSeoDescription(articleDto.getSeoDescription());
            if (articleDto.getArticleType() == null) {
                article.setArticleType(ArticleType.ORIGINAL.getType());
            } else {
                article.setArticleType(articleDto.getArticleType());
            }
            article.setSourceName(articleDto.getSourceName());
            article.setSourceAuthor(articleDto.getSourceAuthor());
            article.setSourceUrl(articleDto.getSourceUrl());
            article.setArticleStatus(ArticleStatus.DRAFT.getType());
            article.setDeleteFlag(ArticleDeleteFlag.DEFAULT.getValue());
            if (articleDto.getCommentFlag() == null) {
                article.setCommentFlag(true);
            } else {
                article.setCommentFlag(articleDto.getCommentFlag());
            }
            if (articleDto.getArticlePerm() == null) {
                article.setArticlePerm(ArticlePerm.PUBLIC.getValue());
            } else {
                article.setArticlePerm(articleDto.getArticlePerm());
            }
            if (articleDto.getTags() != null && articleDto.getTags().size() > 0) {
                checkArticleTags(articleId, articleDto.getTags());
            }
            if (articleDto.getCoverImageType() == null) {
                article.setCoverImageType(ArticleCoverImageType.DEFAULT.getValue());
            } else {
                article.setCoverImageType(articleDto.getCoverImageType());
            }
            checkArticleCoverImagesWeak(articleDto.getId(), article.getCoverImageType(), articleDto.getCoverImages());
            article.setWordsCount(MarkdownWordCountUtils.countMarkdownWords(article.getContentMd()));
            article.setCreatorId(loginId);
            article.setCreateTime(now);
            article.setUpdaterId(loginId);
            article.setUpdateTime(now);
            baseMapper.insert(article);
        } else {
            // 修改场景 TODO 后续完善修改逻辑
//            if (article != null && !loginId.equals(article.getAuthorId())) {
//                throw new BusinessException("无权操作该文章");
//            }
        }

        // 删除临时保存记录
        articleContentDraftRecordMapper.deleteByArticleId(articleDto.getId());
    }

    /**
     * 查询最新的文章草稿
     *
     * @return ArticleVo
     */
    @Override
    public ArticleVo queryLastArticleDraft() {
        Long authorId = SecurityUtils.getLoginId();
        return baseMapper.selectLastArticleDraft(authorId);
    }

//    /**
//     * 分页查询用户的文章列表
//     *
//     * @param queryDto 查询参数
//     * @return ArticleVoList
//     */
//    @Override
//    public ArticlePageData queryArticleListByPage(ArticleQueryDto queryDto) {
//        // 查询当前用户信息
//        Long loginId = SecurityUtils.getLoginId();
//        queryDto.setLoginUser(loginId);
//
//        queryDto.checkPage();
//        Assert.notNull(queryDto.getTagStatus(), "参数【tagStatus】不能为空");
//        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
//        PageInfo<ArticleVo> dataList = new PageInfo<>(baseMapper.selectUserArticleList(queryDto));
//        ArticleCountData countData = baseMapper.selectUserArticleCountData(loginId);
//        return ArticlePageData.builder().dataList(dataList).countInfo(countData).build();
//    }
//
//    /**
//     * 分页查询可见的文章列表
//     *
//     * @param queryDto 查询参数
//     * @return ArticlePageData
//     */
//    @Override
//    public ArticlePageData queryPublicArticleListByPage(ArticleQueryDto queryDto) {
//        queryDto.checkPage();
//        Assert.notNull(queryDto.getTagStatus(), "参数【tagStatus】不能为空");
//        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
//        PageInfo<ArticleVo> dataList = new PageInfo<>(baseMapper.selectPublicArticleList(queryDto));
//        ArticleCountData countData = baseMapper.selectPublicArticleCountData();
//        return ArticlePageData.builder().dataList(dataList).countInfo(countData).build();
//    }
//
//    /**
//     * 查询文章详情
//     *
//     * @param articleId 文章ID
//     * @return ArticleVo
//     */
//    @Override
//    public ArticleVo queryArticleDetail(Long articleId) {
//        Assert.notNull(articleId, "参数【articleId】不能为空");
//        ArticleVo articleVo = baseMapper.selectArticleDetail(articleId);
//        Assert.notNull(articleVo, "文章不存在");
//
//        if (ArticlePerm.SELF_SHOW.getType().equals(articleVo.getArticlePerm())) {
//            // 私密文章，非作者不能查看
//            Long loginId = SecurityUtils.getLoginId();
//            DataValidated.isTrue(!loginId.equals(articleVo.getAuthorId()), "文章是私密文章，禁止查看");
//        }
//        return articleVo;
//    }
//
//    /**
//     * 新增文章
//     *
//     * @param articleDto 文章信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void addArticle(ArticleDto articleDto) {
//        // 操作类型检查
//        boolean isAction = StringUtils.isBlank(articleDto.getAction()) || !BaseConst.Action.ADD.equals(articleDto.getAction());
//        Assert.isTrue(isAction, "参数【action】不正确，理论为：" + BaseConst.Action.ADD);
//
//        // 新增文章
//        checkArticle(articleDto);
//        Article article = articleConverter.dtoToEntity(articleDto);
//        Assert.isTrue(baseMapper.insert(article) != 1, "新增失败");
//        baseMapper.deleteArticleTmpContent(articleDto.getId());
//    }
//
//    /**
//     * 编辑文章
//     *
//     * @param articleDto 文章信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void editArticle(ArticleDto articleDto) {
//        // 编辑文章
//        checkArticle(articleDto);
//        Article article = articleConverter.dtoToEntity(articleDto);
//        Assert.isTrue(baseMapper.updateById(article) != 1, "修改失败");
//        baseMapper.deleteArticleTmpContent(articleDto.getId());
//        baseMapper.invalidateArticlePublishTiming(articleDto.getId(), "重新编辑文章【" + DateUtils.formatNow() + "】");
//    }
//
//    /**
//     * 文章定时发布
//     *
//     * @param articleDto 文章信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void publishArticleByTiming(ArticleDto articleDto) {
//        String nowTime = DateUtils.formatNow();
//        int delay = checkAutoPublishInfo(articleDto);
//        Article article = articleConverter.dtoToEntity(articleDto);
//        article.setDesc("文章将于 ".concat(articleDto.getAutoPublishTime()).concat(" 自动发布"));
//        boolean isAdd = BaseConst.Action.ADD.equals(articleDto.getAction());
//        Assert.isTrue(isAdd ? baseMapper.insert(article) != 1 : baseMapper.updateById(article) != 1, "保存失败");
//        baseMapper.deleteArticleTmpContent(articleDto.getId());
//        baseMapper.invalidateArticlePublishTiming(articleDto.getId(), "重新定时发布【" + nowTime + "】");
//        Long publishId = SequenceUtils.getId();
//        baseMapper.insertArticlePublishTiming(publishId, articleDto.getId(), articleDto.getAutoPublishTime(), nowTime);
//        // throw new ServiceException("功能已关闭，优化中");
//    }
//
//    /**
//     * 删除文章
//     *
//     * @param articleId 文章ID
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void moveArticleToRecycleBin(Long articleId) {
//        Assert.notNull(articleId, "参数【articleId】不能为空");
//        baseMapper.updateArticleStatus(articleId, ArticleStatus.RECYCLE.getType());
//        baseMapper.invalidateArticlePublishTiming(articleId, "文章移至回收站【" + DateUtils.formatNow() + "】");
//    }
//
//    /**
//     * 彻底删除文章
//     *
//     * @param articleId 文章ID
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteArticle(Long articleId) {
//        Assert.notNull(articleId, "参数【articleId】不能为空");
//        Assert.isTrue(baseMapper.deleteById(articleId) != 1, "删除失败");
//    }
//
//    /**
//     * 批量删除文章
//     *
//     * @param articleIds 文章编号列表
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void batchDeleteArticle(List<Long> articleIds) {
//        if (articleIds != null && articleIds.size() > 0) {
//            baseMapper.batchInvalidateArticlePublishTiming(articleIds, "文章移至回收站【" + DateUtils.formatNow() + "】");
//            baseMapper.batchUpdateArticleStatus(articleIds, ArticleStatus.RECYCLE.getType(), "文章被批量移至回收站");
//        }
//    }
//
//    /**
//     * 清空回收站
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void clearArticleRecycleBin() {
//        Long authorId = SecurityUtils.getLoginId();
//        baseMapper.clearArticleRecycleBin(authorId);
//        baseMapper.deleteArticleByAuthor(authorId);
//    }
//
//    /**
//     * 将文章回收至草稿箱
//     *
//     * @param articleId 文章ID
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void recycleToDraftBox(Long articleId) {
//        Assert.notNull(articleId, "参数【articleId】不能为空");
//
//        // 是否允许操作
//        ArticleVo dbInfo = baseMapper.selectArticleDetail(articleId);
//        Long authorId = SecurityUtils.getLoginId();
//        DataValidated.isTrue(!dbInfo.getAuthorId().equals(authorId), "没有权限操作其他作者的文章");
//
//        // 状态检查
//        DataValidated.isTrue(!ArticleStatus.RECYCLE.getType().equals(dbInfo.getArticleStatus()), "文章状态无效，刷新页面重试");
//
//        // 修改状态
//        baseMapper.updateArticleStatus(articleId, ArticleStatus.DRAFT.getType());
//    }
//
//    /**
//     * 审核文章
//     *
//     * @param auditDto 审核信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void auditArticle(ArticleAuditDto auditDto) {
//        Assert.notNull(auditDto.getStatus(), "审核状态不能为空");
//        Assert.isTrue(StringUtils.isBlank(auditDto.getOpinion()), "审核意见不能为空");
//        Assert.isTrue(auditDto.getArticleIds() == null || auditDto.getArticleIds().size() == 0, "文章列表不能为空");
//
//        int noAuditCount = baseMapper.checkNoAuditCount(auditDto.getArticleIds());
//        Assert.isTrue(noAuditCount > 0, "只能选择待审核状态的文章");
//
//        // 待审核 => 已发布
//        List<Long> auditIds1 = baseMapper.splitAuditArticleIds(auditDto.getArticleIds(), ArticleStatus.REVIEW.getType());
//        if (auditIds1.size() > 0) {
//            Integer status = auditDto.getStatus() ? ArticleStatus.PUBLISHED.getType() : ArticleStatus.FAILED_REVIEW.getType();
//            baseMapper.batchUpdateArticleStatus(auditIds1, status, auditDto.getOpinion());
//        }
//
//        // 待审核 => 待发布
//        List<Long> auditIds2 = baseMapper.splitAuditArticleIds(auditDto.getArticleIds(), ArticleStatus.PUBLISH_REVIEW.getType());
//        if (auditIds2.size() > 0) {
//            Integer status = auditDto.getStatus() ? ArticleStatus.TIME_PUBLISH.getType() : ArticleStatus.FAILED_REVIEW.getType();
//            baseMapper.batchUpdateArticleStatus(auditIds2, status, auditDto.getOpinion());
//        }
//    }
//
//    private void emptyCheck(ArticleDto articleDto) {
//        Assert.notNull(articleDto.getId(), "文章序列不能为空");
//        Assert.isTrue(StringUtils.isBlank(articleDto.getAction()), "操作类型不能为空");
//        Assert.isTrue(StringUtils.isBlank(articleDto.getTitle()), "文章标题不能为空");
//        Assert.isTrue(StringUtils.isBlank(articleDto.getContent()), "文章内容不能为空");
//        Assert.isTrue(StringUtils.isBlank(articleDto.getRemark()), "文章摘要不能为空");
//        Assert.isTrue(StringUtils.isBlank(articleDto.getExtend1()), "编辑器类型不能为空");
//        Assert.notNull(articleDto.getCategoryId(), "文章分类不能为空");
//        Assert.notNull(articleDto.getArticlePerm(), "发布范围不能为空");
//        Assert.notNull(articleDto.getCommentFlag(), "评论设置不能为空");
//        Assert.notNull(articleDto.getCoverImageType(), "封面类型不能为空");
//        articleDto.setTitle(articleDto.getTitle().trim());
//    }
//
//    private void actionCheck(ArticleDto articleDto) {
//        Long loginId = SecurityUtils.getLoginId();
//        if (BaseConst.Action.ADD.equals(articleDto.getAction())) {
//            // 新增
//            articleDto.setAuthorId(loginId);
//        } else if (BaseConst.Action.EDIT.equals(articleDto.getAction())) {
//            // 编辑
//            Article dbInfo = baseMapper.selectOneWithDeleted(articleDto.getId());
//            checkArticleIsAllowEdit(dbInfo);
//        } else {
//            // throw new ServiceException("无法处理的操作类型：" + articleDto.getAction());
//        }
//    }
//
//    private void checkArticle(ArticleDto articleDto) {
//        // 非空校验
//        emptyCheck(articleDto);
//
//        // 文章类型校验
//        Integer articleType = articleDto.getArticleType();
//        Assert.notNull(articleType, "文章类型不能为空");
//        if (!ArticleType.ORIGINAL.getType().equals(articleType)) {
//            Assert.isTrue(StringUtils.isBlank(articleDto.getReprintLink()), "原文链接不能为空");
//        }
//
//        // 操作状态校验
//        actionCheck(articleDto);
//
//        // 设置文章内容（去除标签）
//        articleDto.setContentMd(HtmlContentUtils.getTextFromHtml(articleDto.getContent()).replaceAll("&nbsp;", ""));
//
//        // 设置文章状态（对外不可见的文章无须审批）
//        Integer articlePerm = articleDto.getArticlePerm();
//        boolean isSelfShow = articlePerm.equals(ArticlePerm.SELF_SHOW.getType());
//        Integer status = isSelfShow ? ArticleStatus.PUBLISHED.getType() : ArticleStatus.REVIEW.getType();
//        articleDto.setArticleStatus(status);
//
//        // 封面校验
//        articleImgConfig(articleDto);
//
//    }
//
//    private int checkAutoPublishInfo(ArticleDto articleDto) {
//        // 非空校验
//        emptyCheck(articleDto);
//        Assert.notNull(articleDto.getAutoPublishTime(), "发布时间不能为空");
//
//        // 文章类型校验
//        Integer articleType = articleDto.getArticleType();
//        Assert.notNull(articleType, "文章类型不能为空");
//        if (!ArticleType.ORIGINAL.getType().equals(articleType)) {
//            Assert.isTrue(StringUtils.isBlank(articleDto.getReprintLink()), "原文链接不能为空");
//        }
//
//        // 发布时间校验
//        Date nowDate = new Date();
//        Date publishDate = new Date();
//        try {
//            publishDate = DateUtils.parseDate(articleDto.getAutoPublishTime(), "yyyy-MM-dd HH:mm");
//        } catch (Exception e) {
//            // throw new ServiceException("发布时间格式不正确", e);
//        }
//        long delay = publishDate.getTime() - nowDate.getTime();
//        Assert.isTrue(delay <= 0 || delay > Integer.MAX_VALUE, "发布时间无效");
//
//        // 新增编辑校验
//        actionCheck(articleDto);
//
//        // 发布类型校验
//        Integer articlePerm = articleDto.getArticlePerm();
//        Assert.isTrue(ArticlePerm.SELF_SHOW.getType().equals(articlePerm), "私密文章暂不支持定时发布");
//
//        // 设置文章状态
//        articleDto.setArticleStatus(ArticleStatus.PUBLISH_REVIEW.getType());
//
//        // 封面校验
//        articleImgConfig(articleDto);
//
//        // 标签校验，自定义标签入库，文章关键字生成
//        List<String> tags = articleDto.getArticleTag();
//        Assert.isTrue(tags == null || tags.size() == 0, "文章标签不能为空");
//        checkArticleTagConfig(articleDto);
//
//        // 返回发布毫秒数
//        return (int) delay;
//    }
//
//    private void checkArticleIsAllowEdit(Article dbInfo) {
//        // 检查文章是否存在
//        Assert.notNull(dbInfo, "操作失败，文章不存在");
//
//        // 检查文章是否被删除
//        Assert.isTrue(dbInfo.getDeleted(), "操作失败，文章已被删除");
//
//        // 检查作者
//        Long loginId = SecurityUtils.getLoginId();
//        DataValidated.isTrue(!dbInfo.getAuthorId().equals(loginId), "没有权限修改其他作者的文章");
//
//        // 检查文章是否已经被删除
//        DataValidated.isTrue(ArticleStatus.RECYCLE.getType().equals(dbInfo.getArticleStatus()), "文章处于回收站中，不允许编辑，请先把文章移动到草稿箱在进行编辑");
//    }

    private void checkArticleTags(Long articleId, List<String> tags) {
        ThrowUtils.throwIf(tags == null || tags.size() == 0, ArticleErrorCode.EARTIA001);

        Set<Long> tagIds = new HashSet<>();
        for (String tagIdOrName : tags) {
            if (MathUtils.isPositiveInteger(tagIdOrName)) {
                Long tagId = Long.parseLong(tagIdOrName);
                Tag tag = tagMapper.selectById(tagId);
                if (tag != null) {
                    // 已有标签
                    tagIds.add(tagId);
                    continue;
                }
            }

            // 自定义标签，根据标签名称查询标签不存在
            Tag tag = tagMapper.selectByTagName(tagIdOrName);
            if (tag != null) {
                // 标签名称已存在，不需要再创建
                tagIds.add(tag.getId());
            } else {
                // 创建自定义标签
                String slug = SlugUtils.toSlug(tagIdOrName);
                String _slug = slug;
                Tag tagBySlug = tagMapper.selectByTagSlug(_slug);
                int count = 1;
                while (tagBySlug != null) {
                    _slug = slug + "-" + count++;
                    tagBySlug = tagMapper.selectByTagSlug(_slug);
                }

                Tag customTag = new Tag();
                Long customTagId = SequenceUtils.getId();
                customTag.setId(customTagId);
                customTag.setName(tagIdOrName);
                customTag.setSlug(_slug);
                customTag.setSeoSearch(false);
                customTag.setEnabled(true);
                tagMapper.insert(customTag);
                tagIds.add(customTagId);
            }
        }

        // 物理删除中间数据
        articleTagMapper.deleteByArticleIdHard(articleId);

        // 插入新的中间数据
        for (Long tagId : tagIds) {
            ArticleTagRelation relation = new ArticleTagRelation();
            relation.setArticleId(articleId);
            relation.setTagId(tagId);
            relation.setDeleted(false);
            articleTagMapper.insert(relation);
        }
    }

    private void checkArticleCoverImages(Long articleId, String title, Integer coverImageType, List<UploadFileDto> coverImages) {
        if (ArticleCoverImageType.DEFAULT.getValue().equals(coverImageType)) {
            removeArticleCoverImages(articleId, coverImages);
            return;
        }
        if (ArticleCoverImageType.SINGLE.getValue().equals(coverImageType)) {
            ThrowUtils.throwIf(CollectionUtils.isEmpty(coverImages), ArticleErrorCode.EARTIA003);
            UploadFileDto uploadFile = coverImages.get(0);
            insertArticleCoverImage(articleId, uploadFile);

            // 删除多余上传的封面
            if (coverImages.size() > 1) {
                for (int i = 1; i < coverImages.size(); i++) {
                    UploadFileDto uploadFileDto = coverImages.get(i);
                    fileMapper.deleteById(uploadFileDto.getFileId());
                }
            }
            return;
        }
        if (ArticleCoverImageType.MULTIPLE.getValue().equals(coverImageType)) {
            // TODO 最大上传数量待客户化
            ThrowUtils.throwIf(CollectionUtils.isEmpty(coverImages), ArticleErrorCode.EARTIA003);
            insertArticleCoverImages(articleId, coverImages);
            return;
        }
        if (ArticleCoverImageType.RANDOM.getValue().equals(coverImageType)) {
            removeArticleCoverImages(articleId, coverImages);
            return;
        }
        if (ArticleCoverImageType.TITLE.getValue().equals(coverImageType)) {
            removeArticleCoverImages(articleId, coverImages);
            UploadFileVo uploadFileVo = generateArticleCoverImageByTitle(title);
            UploadFileDto uploadFile = new UploadFileDto();
            uploadFile.setFileId(uploadFileVo.getFileId());
            uploadFile.setFileAccessUrl(uploadFileVo.getFileAccessUrl());
            insertArticleCoverImage(articleId, uploadFile);
            return;
        }
        if (ArticleCoverImageType.NONE.getValue().equals(coverImageType)) {
            removeArticleCoverImages(articleId, coverImages);
        }
    }

    private void checkArticleCoverImagesWeak(Long articleId, Integer coverImageType, List<UploadFileDto> coverImages) {
        if (ArticleCoverImageType.DEFAULT.getValue().equals(coverImageType)) {
            removeArticleCoverImages(articleId, coverImages);
            return;
        }
        if (coverImages.size() > 0) {
            if (ArticleCoverImageType.SINGLE.getValue().equals(coverImageType)) {
                UploadFileDto uploadFile = coverImages.get(0);
                insertArticleCoverImage(articleId, uploadFile);

                // 删除多余上传的封面
                if (coverImages.size() > 1) {
                    for (int i = 1; i < coverImages.size(); i++) {
                        UploadFileDto uploadFileDto = coverImages.get(i);
                        fileMapper.deleteById(uploadFileDto.getFileId());
                    }
                }
                return;
            }
            if (ArticleCoverImageType.MULTIPLE.getValue().equals(coverImageType)) {
                // TODO 最大上传数量待客户化
                insertArticleCoverImages(articleId, coverImages);
                return;
            }
        }
        if (ArticleCoverImageType.RANDOM.getValue().equals(coverImageType)) {
            removeArticleCoverImages(articleId, coverImages);
            return;
        }
        if (ArticleCoverImageType.TITLE.getValue().equals(coverImageType)) {
            removeArticleCoverImages(articleId, coverImages);
            return;
        }
        if (ArticleCoverImageType.NONE.getValue().equals(coverImageType)) {
            removeArticleCoverImages(articleId, coverImages);
        }
    }

    private void checkArticleType(ArticleDto articleDto) {
        if (ArticleType.ORIGINAL.getType().equals(articleDto.getArticleType())) {
            articleDto.setSourceName(null);
            articleDto.setSourceAuthor(null);
            articleDto.setSourceUrl(null);
        } else {
            ThrowUtils.throwIfBlank(articleDto.getSourceUrl(), ArticleErrorCode.EARTIA004);
        }
    }

    private void insertArticleCoverImage(Long articleId, UploadFileDto uploadFile) {
        articleImageMapper.deleteByArticleId(articleId);
        ArticleImageRelation articleImageRelation = new ArticleImageRelation();
        articleImageRelation.setArticleId(articleId);
        articleImageRelation.setArticleImage(uploadFile.getFileAccessUrl());
        articleImageMapper.insert(articleImageRelation);

        fileRelationMapper.deleteByBusinessInfo(articleId, ServiceUploadBusinessType.ARTICLE_COVER_IMG);
        FileRelation fileRelation = new FileRelation();
        fileRelation.setFileId(uploadFile.getFileId());
        fileRelation.setBusinessId(articleId);
        fileRelation.setBusinessType(ServiceUploadBusinessType.ARTICLE_COVER_IMG);
        fileRelationMapper.insert(fileRelation);
    }

    private void insertArticleCoverImages(Long articleId, List<UploadFileDto> coverImages) {
        articleImageMapper.deleteByArticleId(articleId);
        fileRelationMapper.deleteByBusinessInfo(articleId, ServiceUploadBusinessType.ARTICLE_COVER_IMG);

        for (UploadFileDto coverImage : coverImages) {
            ArticleImageRelation articleImageRelation = new ArticleImageRelation();
            articleImageRelation.setArticleId(articleId);
            articleImageRelation.setArticleImage(coverImage.getFileAccessUrl());
            articleImageMapper.insert(articleImageRelation);

            FileRelation fileRelation = new FileRelation();
            fileRelation.setFileId(coverImage.getFileId());
            fileRelation.setBusinessId(articleId);
            fileRelation.setBusinessType(ServiceUploadBusinessType.ARTICLE_COVER_IMG);
            fileRelationMapper.insert(fileRelation);
        }
    }

    private void removeArticleCoverImages(Long articleId, List<UploadFileDto> coverImages) {
        articleImageMapper.deleteByArticleId(articleId);
        fileRelationMapper.deleteByBusinessInfo(articleId, ServiceUploadBusinessType.ARTICLE_COVER_IMG);
        if (coverImages != null && coverImages.size() > 0) {
            for (UploadFileDto coverImage : coverImages) {
                fileMapper.deleteById(coverImage.getFileId());
            }
        }
    }

    private UploadFileVo generateArticleCoverImageByTitle(String title) {
        try {
            // 本地生成封面
            String filePath = ImageBuilderUtils.buildFontImage(title, FileUtils.SYS_TEM_DIR);

            // 上传至服务器
            java.io.File file = new java.io.File(filePath);
            FileInputStream fileInputStream = new FileInputStream(filePath);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "image/webp", fileInputStream);

            List<UploadFileVo> uploadFileVoList = fileService.uploadFile(multipartFile, null);
            return uploadFileVoList.get(0);
        } catch (Exception e) {
            log.error("generateArticleCoverImageByTitle error: {}", title, e);
            throw new ServiceException(ArticleErrorCode.EARTIA002);
        }
    }
}
