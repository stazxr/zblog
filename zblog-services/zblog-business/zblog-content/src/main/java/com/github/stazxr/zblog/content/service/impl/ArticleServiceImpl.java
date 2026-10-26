package com.github.stazxr.zblog.content.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ServiceException;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.exception.code.CommonErrorCode;
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
import com.github.stazxr.zblog.content.domain.vo.*;
import com.github.stazxr.zblog.content.mapper.*;
import com.github.stazxr.zblog.content.service.ArticleService;
import com.github.stazxr.zblog.content.util.Constants;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import com.github.stazxr.zblog.util.graphics.ImageBuilderUtils;
import com.github.stazxr.zblog.util.html.MarkdownHtmlUtils;
import com.github.stazxr.zblog.util.html.SlugUtils;
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
import java.util.*;
import java.util.stream.Collectors;

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

    private final ArticleVersionMapper articleVersionMapper;

    private final ArticleVersionTagMapper articleVersionTagMapper;

    private final ArticleVersionImageMapper articleVersionImageMapper;

    private final ArticleOperationLogMapper articleOperationLogMapper;

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
        ThrowUtils.throwIfBlank(queryDto.getTagStatus(), ArticleErrorCode.EARTIA001);
        if (StringUtils.isNotBlank(queryDto.getTitle())) {
            queryDto.setTitle(queryDto.getTitle().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getSlug())) {
            queryDto.setSlug(queryDto.getSlug().trim());
        }

        // 分页查询
        Page<ArticleVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        IPage<ArticleVo> articlePageList = baseMapper.selectMyArticleList(page, queryDto);

        // 当前页文章 ID
        List<ArticleVo> records = articlePageList.getRecords();
        List<Long> articleIds = records.stream().map(ArticleVo::getId).collect(Collectors.toList());
        if (!articleIds.isEmpty()) {
            // 批量查询标签
            List<ArticleTagNameVo> tagRows = baseMapper.selectArticleTagNamesByArticleIds(articleIds);
            Map<Long, List<String>> tagMap = tagRows.stream().collect(Collectors.groupingBy(
                    ArticleTagNameVo::getArticleId, Collectors.mapping(ArticleTagNameVo::getTagName, Collectors.toList())
            ));

            // 批量查询封面
            List<ArticleCoverImageVo> imageRows = baseMapper.selectArticleCoverImagesByArticleIds(articleIds);
            Map<Long, List<String>> imageMap = imageRows.stream().collect(Collectors.groupingBy(
                ArticleCoverImageVo::getArticleId, Collectors.mapping(ArticleCoverImageVo::getCoverImage, Collectors.toList())
            ));

            // 回填文章标签和封面
            for (ArticleVo article : records) {
                article.setTagNames(tagMap.getOrDefault(article.getId(), Collections.emptyList()));
                article.setCoverImageList(imageMap.getOrDefault(article.getId(), Collections.emptyList()));
            }
        }

        return articlePageList;
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
            ThrowUtils.throwIf(!loginId.equals(articleVo.getAuthorId()), BaseErrorCode.ECOREA002);

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
     * 初始化文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleVo initArticle() {
        Long loginId = SecurityUtils.getLoginId();
        ArticleVo articleVo = baseMapper.selectLastArticleEmptyDraft(loginId);
        if (articleVo == null) {
            // 初始化文章信息
            LocalDateTime now = LocalDateTime.now();
            Article article = new Article();
            article.setId(SequenceUtils.getId());
            article.setTitle("");
            article.setContentMd("");
            article.setContentHtml("");
            article.setAuthorId(loginId);
            article.setArticleType(ArticleType.ORIGINAL.getType());
            article.setArticleStatus(ArticleStatus.DRAFT.getValue());
            article.setDeleteFlag(ArticleDeleteFlag.DEFAULT.getValue());
            article.setCommentFlag(true);
            article.setArticlePerm(ArticlePerm.PUBLIC.getValue());
            article.setCoverImageType(ArticleCoverImageType.DEFAULT.getValue());
            article.setWordsCount(0);
            article.setCreatorId(loginId);
            article.setCreateTime(now);
            article.setUpdaterId(loginId);
            article.setUpdateTime(now);
            baseMapper.insert(article);
            articleVo = baseMapper.selectLastArticleEmptyDraft(loginId);

            // 记录文章操作日志
            recordArticleLog(article, null, ArticleOperationType.INIT, null);
        }

        ThrowUtils.throwSystemIf(articleVo == null, ArticleErrorCode.SARTIA001);
        return articleVo;
    }

    /**
     * 保存文章
     *
     * @param articleDto 文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveArticle(ArticleDraftDto articleDto) {
        // 获取文章信息
        Long articleId = articleDto.getId(); // 文章ID
        Article article = baseMapper.selectById(articleId);

        // 检查文章是否允许编辑
        checkArticleEditable(article);

        // 查询历史文章内容（用于记录日志）
        Article dbArticle = baseMapper.selectById(articleId);

        // 刷新文章信息并保存
        if (articleDto.getTitle() == null) {
            article.setTitle("");
        } else {
            article.setTitle(articleDto.getTitle().trim());
        }
        if (article.getSlug() == null || article.getVersion() == 0) {
            // 文章已发布过，则 SLUG 不允许修改
            if (StringUtils.isBlank(articleDto.getSlug())) {
                article.setSlug(null);
            } else {
                article.setSlug(articleDto.getSlug());
            }
        }
        if (articleDto.getContentMd() == null) {
            article.setContentMd("");
            article.setContentHtml("");
            article.setWordsCount(0);
        } else {
            article.setContentMd(articleDto.getContentMd());
            article.setContentHtml(MarkdownHtmlUtils.toHtml(articleDto.getContentMd()));
            article.setWordsCount(MarkdownWordCountUtils.countMarkdownWords(article.getContentMd()));
        }
        article.setSummary(articleDto.getSummary());
        article.setCategoryId(articleDto.getCategoryId());
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
        if (articleDto.getArticlePerm() == null) {
            article.setArticlePerm(ArticlePerm.PUBLIC.getValue());
        } else {
            article.setArticlePerm(articleDto.getArticlePerm());
        }
        if (articleDto.getCommentFlag() == null) {
            article.setCommentFlag(true);
        } else {
            article.setCommentFlag(articleDto.getCommentFlag());
        }
        // 保存文章标签
        if (articleDto.getTags() != null && articleDto.getTags().size() > 0) {
            checkArticleTags(articleId, articleDto.getTags());
        } else {
            articleTagMapper.deleteByArticleIdHard(articleId);
        }
        // 保存文章封面
        if (articleDto.getCoverImageType() == null) {
            article.setCoverImageType(ArticleCoverImageType.DEFAULT.getValue());
        } else {
            article.setCoverImageType(articleDto.getCoverImageType());
        }
        checkArticleCoverImagesWeak(articleDto.getId(), article.getCoverImageType(), articleDto.getCoverImages());
        // 只要触发保存，文章状态就变为草稿态
        if (ArticleStatus.SCHEDULED.getValue().equals(article.getArticleStatus())) {
            // TODO 如果原文章状态为待发布，则删除自动发布记录
        }
        article.setArticleStatus(ArticleStatus.DRAFT.getValue());
        article.setUpdaterId(SecurityUtils.getLoginId());
        article.setUpdateTime(LocalDateTime.now());
        baseMapper.updateArticleById(article);

        // 记录文章操作日志
        recordArticleLog(article, dbArticle, ArticleOperationType.UPDATE, null);

        // 删除临时保存记录
        articleContentDraftRecordMapper.deleteByArticleId(articleDto.getId());
    }

    /**
     * 发布文章
     *
     * @param articleDto 文章信息
     * @return ArticleVo 发布文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleVo publishArticle(ArticleDto articleDto) {
        // 获取文章信息
        Long articleId = articleDto.getId(); // 文章ID
        Article dbArticle = baseMapper.selectById(articleId);
        Article article = baseMapper.selectById(articleId);
        // 检查文章是否允许编辑
        checkArticleEditable(article);
        // 标签校验
        checkArticleTags(articleId, articleDto.getTags());
        // 封面校验
        checkArticleCoverImages(articleId, articleDto.getTitle(), articleDto.getCoverImageType(), articleDto.getCoverImages());
        // 文章类型校验
        checkArticleType(articleDto);
        // SLUG 校验
        checkArticleSlug(article.getVersion(), article.getSlug(), articleDto.getSlug());
        // 填充文章信息
        fillArticle(article, articleDto);
        if (ArticlePerm.SELF.getValue().equals(article.getArticlePerm())) {
            // 私密文章直接发布
            article.setArticleStatus(ArticleStatus.PUBLISHED.getValue());
            // 记录文章发布版本信息
            article.setVersion(article.getVersion() + 1); // 每次发布文章版本号 + 1
            publishArticleVersion(article);

            // 记录文章操作日志
            recordArticleLog(article, dbArticle, ArticleOperationType.PUBLISH, "私密文章自动发布");
        } else {
            // 非私密文章审核通过后发布
            article.setArticleStatus(ArticleStatus.PENDING_REVIEW.getValue());

            // 记录文章操作日志
            recordArticleLog(article, dbArticle, ArticleOperationType.PUBLISH, "非私密文章需要审核");
        }
        // 保存文章内容
        baseMapper.updateArticleById(article);
        // 删除临时保存记录
        articleContentDraftRecordMapper.deleteByArticleId(articleId);
        // 返回文章信息
        return queryMyArticleDetail(articleId);
    }

    /**
     * 定时发布文章
     *
     * @param articleDto 文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoPublishArticle(ArticleDto articleDto) {

    }

    /**
     * 删除文章（移动到回收站）
     *
     * @param articleId 文章id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long articleId) {
        // 查询文章信息
        Article dbArticle = baseMapper.selectById(articleId);
        ThrowUtils.requireNonNull(dbArticle, BaseErrorCode.ECOREA001);
        // 文章作者判断
        Long loginId = SecurityUtils.getLoginId();
        ThrowUtils.throwIf(!loginId.equals(dbArticle.getAuthorId()), BaseErrorCode.ECOREA004);
        // 文章状态校验
        ThrowUtils.throwIf(!ArticleDeleteFlag.DEFAULT.getValue().equals(dbArticle.getDeleteFlag()), ArticleErrorCode.EARTIA009);
        // 第一次删除文章，将文章移动到回收站
        baseMapper.deleteArticle(articleId, ArticleDeleteFlag.RECYCLE.getValue());
        // TODO
        // baseMapper.invalidateArticlePublishTiming(articleId, "文章移至回收站【" + DateUtils.formatNow() + "】");
        // 删除临时保存记录
        articleContentDraftRecordMapper.deleteByArticleId(articleId);
        // 记录文章操作日志
        Article newArticle = baseMapper.selectById(articleId);
        recordArticleLog(newArticle, dbArticle, ArticleOperationType.DELETE1, null);
    }

    /**
     * 删除文章（彻底删除）
     *
     * @param articleId 文章id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle2(Long articleId) {
        // 查询文章信息
        Article dbArticle = baseMapper.selectById(articleId);
        ThrowUtils.requireNonNull(dbArticle, BaseErrorCode.ECOREA001);
        // 文章作者判断
        Long loginId = SecurityUtils.getLoginId();
        ThrowUtils.throwIf(!loginId.equals(dbArticle.getAuthorId()), BaseErrorCode.ECOREA004);
        // 文章状态校验
        ThrowUtils.throwIf(!ArticleDeleteFlag.RECYCLE.getValue().equals(dbArticle.getDeleteFlag()), ArticleErrorCode.EARTIA009);
        // 彻底删除文章
        baseMapper.deleteArticle(articleId, ArticleDeleteFlag.DELETED.getValue());
        // 删除临时保存记录
        articleContentDraftRecordMapper.deleteByArticleId(articleId);
        // 记录文章操作日志
        Article newArticle = baseMapper.selectById(articleId);
        recordArticleLog(newArticle, dbArticle, ArticleOperationType.DELETE2, null);
    }

    /**
     * 删除文章（清空回收站）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle3() {
        Long loginId = SecurityUtils.getLoginId();
        baseMapper.deleteArticle3(loginId);
        // TODO 记录操作日志
        // 删除临时保存记录
    }

    /**
     * 还原文章（移至草稿箱）
     *
     * @param articleId 文章id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetArticle(Long articleId) {
        // 查询文章信息
        Article dbArticle = baseMapper.selectById(articleId);
        ThrowUtils.requireNonNull(dbArticle, BaseErrorCode.ECOREA001);
        // 文章作者判断
        Long loginId = SecurityUtils.getLoginId();
        ThrowUtils.throwIf(!loginId.equals(dbArticle.getAuthorId()), CommonErrorCode.EBASEA000);
        // 文章状态校验
        ThrowUtils.throwIf(!ArticleDeleteFlag.RECYCLE.getValue().equals(dbArticle.getDeleteFlag()), ArticleErrorCode.EARTIA009);
        baseMapper.updateArticleStatus(articleId, ArticleStatus.DRAFT.getValue());
        // 记录文章操作日志
        Article newArticle = baseMapper.selectById(articleId);
        recordArticleLog(newArticle, dbArticle, ArticleOperationType.RESET, null);
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
    // 检查文件是否允许编辑
    private void checkArticleEditable(Article article) {
        // 检查文章是否存在
        ThrowUtils.throwIfNull(article, BaseErrorCode.ECOREA001);

        // 检查文章状态是否正常
        ThrowUtils.throwIf(!ArticleDeleteFlag.DEFAULT.getValue().equals(article.getDeleteFlag()), BaseErrorCode.ECOREA001);

        // 检查是否作者编辑
        Long loginId = SecurityUtils.getLoginId(); // 当前登录用户
        ThrowUtils.throwIf(!loginId.equals(article.getAuthorId()), BaseErrorCode.ECOREA003);
    }

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
            ThrowUtils.throwIf(CollectionUtils.isEmpty(coverImages), ArticleErrorCode.EARTIA003);
            ThrowUtils.throwIf(coverImages.size() > Constants.MAX_ARTICLE_COVER_IMAGE_SIZE, ArticleErrorCode.EARTIA008);
            insertArticleCoverImages(articleId, coverImages);
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
                List<UploadFileDto> _coverImages = new ArrayList<>();
                for (UploadFileDto uploadFileDto : coverImages) {
                    if (_coverImages.size() < Constants.MAX_ARTICLE_COVER_IMAGE_SIZE) {
                        _coverImages.add(uploadFileDto);
                    } else {
                        fileMapper.deleteById(uploadFileDto.getFileId());
                    }
                }

                insertArticleCoverImages(articleId, coverImages);
                return;
            }
        }
        if (ArticleCoverImageType.TITLE.getValue().equals(coverImageType)) {
            removeArticleCoverImages(articleId, coverImages);
            return;
        }
        if (ArticleCoverImageType.NONE.getValue().equals(coverImageType)) {
            removeArticleCoverImages(articleId, coverImages);
        }
    }

    // 检查文章类型
    private void checkArticleType(ArticleDto articleDto) {
        if (ArticleType.ORIGINAL.getType().equals(articleDto.getArticleType())) {
            articleDto.setSourceName(null);
            articleDto.setSourceAuthor(null);
            articleDto.setSourceUrl(null);
        } else {
            ThrowUtils.throwIfBlank(articleDto.getSourceUrl(), ArticleErrorCode.EARTIA004);
        }
    }

    // 检查文章 SLUG
    private void checkArticleSlug(Integer version, String dbSlug, String newSlug) {
        if (version > 0) {
            // 文章发布后，SLUG 不允许变更
            ThrowUtils.throwIf(!newSlug.equals(dbSlug), ArticleErrorCode.EARTIA010);
        }
    }

    // 填充文章信息
    private void fillArticle(Article article, ArticleDto articleDto) {
        article.setTitle(articleDto.getTitle().trim());
        if (article.getVersion() == 0) {
            article.setSlug(articleDto.getSlug());
        }
        article.setContentMd(articleDto.getContentMd());
        article.setContentHtml(MarkdownHtmlUtils.toHtml(articleDto.getContentMd()));
        article.setWordsCount(MarkdownWordCountUtils.countMarkdownWords(article.getContentMd()));
        article.setSummary(articleDto.getSummary());
        article.setCategoryId(articleDto.getCategoryId());
        article.setSeoTitle(articleDto.getSeoTitle());
        article.setSeoKeywords(articleDto.getSeoKeywords());
        article.setSeoDescription(articleDto.getSeoDescription());
        article.setArticleType(articleDto.getArticleType());
        article.setSourceName(articleDto.getSourceName());
        article.setSourceAuthor(articleDto.getSourceAuthor());
        article.setSourceUrl(articleDto.getSourceUrl());
        article.setArticlePerm(articleDto.getArticlePerm());
        article.setCommentFlag(articleDto.getCommentFlag());
        article.setCoverImageType(articleDto.getCoverImageType());
        article.setUpdaterId(SecurityUtils.getLoginId());
        article.setUpdateTime(LocalDateTime.now());
    }

    private ArticleVersion parseArticleVersion(Article article) {
        ArticleVersion articleVersion = new ArticleVersion();
        articleVersion.setArticleId(article.getId());
        articleVersion.setVersion(article.getVersion());
        articleVersion.setTitle(article.getTitle());
        articleVersion.setSlug(article.getSlug());
        articleVersion.setSummary(article.getSummary());
        articleVersion.setContentMd(article.getContentMd());
        articleVersion.setContentHtml(MarkdownHtmlUtils.toHtml(article.getContentMd()));
        articleVersion.setWordsCount(MarkdownWordCountUtils.countMarkdownWords(article.getContentMd()));
        articleVersion.setCategoryId(article.getCategoryId());
        articleVersion.setSeoTitle(article.getSeoTitle());
        articleVersion.setSeoKeywords(article.getSeoKeywords());
        articleVersion.setSeoDescription(article.getSeoDescription());
        articleVersion.setArticleType(article.getArticleType());
        articleVersion.setSourceName(article.getSourceName());
        articleVersion.setSourceAuthor(article.getSourceAuthor());
        articleVersion.setSourceUrl(article.getSourceUrl());
        articleVersion.setArticlePerm(article.getArticlePerm());
        articleVersion.setCommentFlag(article.getCommentFlag());
        articleVersion.setCoverImageType(article.getCoverImageType());
        articleVersion.setExtraInfo(article.getExtraInfo());
        articleVersion.setPublishUserId(SequenceUtils.getId());
        articleVersion.setPublishTime(LocalDateTime.now());
        return articleVersion;
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

    // 发布文章版本
    private void publishArticleVersion(Article article) {
        // 获取发布版本ID
        Long versionId = SequenceUtils.getId();

        // 新增文章发布版本
        ArticleVersion articleVersion = parseArticleVersion(article);
        articleVersion.setId(versionId);
        articleVersionMapper.insert(articleVersion);

        // 记录正式发布的文章标签
        articleVersionTagMapper.insertVersionTag(versionId, article.getId());

        // 记录正式发布的文章封面
        articleVersionImageMapper.insertVersionImage(versionId, article.getId());
    }

    // 记录文章操作日志
    private void recordArticleLog(Article newArticle, Article oldArticle, ArticleOperationType type, String remark) {
        if (newArticle == null) {
            return;
        }

        ArticleOperationLog operationLog = new ArticleOperationLog();
        operationLog.setId(SequenceUtils.getId());
        operationLog.setArticleId(newArticle.getId());
        operationLog.setOperatorId(SecurityUtils.getLoginId());
        operationLog.setOperationType(type.getValue());
        operationLog.setAfterType(newArticle.getArticleType());
        operationLog.setAfterPerm(newArticle.getArticlePerm());
        operationLog.setAfterStatus(newArticle.getArticleStatus());
        if (oldArticle != null) {
            operationLog.setBeforeType(oldArticle.getArticleType());
            operationLog.setBeforePerm(oldArticle.getArticlePerm());
            operationLog.setBeforeStatus(oldArticle.getArticleStatus());
        }
        operationLog.setVersion(newArticle.getVersion());
        operationLog.setRemark(remark);
        operationLog.setCreateTime(LocalDateTime.now());
        articleOperationLogMapper.insert(operationLog);
    }
}
