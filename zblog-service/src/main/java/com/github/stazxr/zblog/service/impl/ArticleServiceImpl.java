package com.github.stazxr.zblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.component.file.FileHandler;
import com.github.stazxr.zblog.base.component.file.FileTypeHandler;
import com.github.stazxr.zblog.base.component.file.model.FileInfo;
import com.github.stazxr.zblog.base.component.file.model.FileUploadType;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.service.FileService;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.converter.ArticleConverter;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.domain.bo.ArticleCountData;
import com.github.stazxr.zblog.domain.bo.ArticlePageData;
import com.github.stazxr.zblog.domain.dto.ArticleAuditDto;
import com.github.stazxr.zblog.domain.dto.ArticleDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.dto.setting.OtherInfo;
import com.github.stazxr.zblog.domain.entity.*;
import com.github.stazxr.zblog.domain.enums.*;
import com.github.stazxr.zblog.domain.vo.ArticleTmpContentVo;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import com.github.stazxr.zblog.mapper.*;
import com.github.stazxr.zblog.rabbitmq.producer.DelayProducer;
import com.github.stazxr.zblog.service.ArticleService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.graphics.ImageBuilderUtils;
import com.github.stazxr.zblog.util.http.HtmlContentUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.math.MathUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文章业务实现层
 *
 * @author SunTao
 * @since 2021-02-23
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    /**
     * 多封面最多支持上传四个封面
     */
    private static final int MAX_ARTICLE_IMG_SIZE = 4;

    private final FileService fileService;

    private final ArticleConverter articleConverter;

    private final ArticleTagMapper articleTagMapper;

    private final ArticleImgRelationMapper articleImgRelationMapper;

    private final ArticleTagRelationMapper articleTagRelationMapper;

    private final WebSettingMapper webSettingMapper;

    private final DelayProducer delayProducer;

    /**
     * 分页查询用户的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVoList
     */
    @Override
    public ArticlePageData queryArticleListByPage(ArticleQueryDto queryDto) {
        // 查询当前用户信息
        Long loginId = SecurityUtils.getLoginId();
        queryDto.setLoginUser(loginId);

        queryDto.checkPage();
        Assert.notNull(queryDto.getTagStatus(), "参数【tagStatus】不能为空");
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        PageInfo<ArticleVo> dataList = new PageInfo<>(baseMapper.selectUserArticleList(queryDto));
        ArticleCountData countData = baseMapper.selectUserArticleCountData(loginId);
        return ArticlePageData.builder().dataList(dataList).countInfo(countData).build();
    }

    /**
     * 分页查询可见的文章列表
     *
     * @param queryDto 查询参数
     * @return ArticlePageData
     */
    @Override
    public ArticlePageData queryPublicArticleListByPage(ArticleQueryDto queryDto) {
        queryDto.checkPage();
        Assert.notNull(queryDto.getTagStatus(), "参数【tagStatus】不能为空");
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        PageInfo<ArticleVo> dataList = new PageInfo<>(baseMapper.selectPublicArticleList(queryDto));
        ArticleCountData countData = baseMapper.selectPublicArticleCountData();
        return ArticlePageData.builder().dataList(dataList).countInfo(countData).build();
    }

    /**
     * 查询文章详情
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    @Override
    public ArticleVo queryArticleDetail(Long articleId) {
        Assert.notNull(articleId, "参数【articleId】不能为空");
        ArticleVo articleVo = baseMapper.selectArticleDetail(articleId);
        Assert.notNull(articleVo, "文章不存在");

        if (ArticlePerm.SELF_SHOW.getType().equals(articleVo.getArticlePerm())) {
            // 私密文章，非作者不能查看
            Long loginId = SecurityUtils.getLoginId();
            DataValidated.isTrue(!loginId.equals(articleVo.getAuthorId()), "文章是私密文章，禁止查看");
        }
        return articleVo;
    }

    /**
     * 新增文章
     *
     * @param articleDto 文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArticle(ArticleDto articleDto) {
        // 操作类型检查
        boolean isAction = StringUtils.isBlank(articleDto.getAction()) || !BaseConst.Action.ADD.equals(articleDto.getAction());
        Assert.isTrue(isAction, "参数【action】不正确，理论为：" + BaseConst.Action.ADD);

        // 新增文章
        checkArticle(articleDto);
        Article article = articleConverter.dtoToEntity(articleDto);
        Assert.isTrue(baseMapper.insert(article) != 1, "新增失败");
        baseMapper.deleteArticleTmpContent(articleDto.getId());
    }

    /**
     * 编辑文章
     *
     * @param articleDto 文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editArticle(ArticleDto articleDto) {
        // 操作类型检查
        boolean isAction = StringUtils.isBlank(articleDto.getAction()) || !BaseConst.Action.EDIT.equals(articleDto.getAction());
        Assert.isTrue(isAction, "参数【action】不正确，理论为：" + BaseConst.Action.EDIT);

        // 编辑文章
        checkArticle(articleDto);
        Article article = articleConverter.dtoToEntity(articleDto);
        Assert.isTrue(baseMapper.updateById(article) != 1, "修改失败");
        baseMapper.deleteArticleTmpContent(articleDto.getId());
        baseMapper.invalidateArticlePublishTiming(articleDto.getId(), "重新编辑文章【" + DateUtils.formatNow() + "】");
    }

    /**
     * 保存文章草稿
     *
     * @param articleDto 文章信息
     * @return 保存时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveArticleDraft(ArticleDto articleDto) {
        String time = checkDraftInfo(articleDto);
        Article article = articleConverter.dtoToEntity(articleDto);
        boolean isAdd = BaseConst.Action.ADD.equals(articleDto.getAction());
        Assert.isTrue(isAdd ? baseMapper.insert(article) != 1 : baseMapper.updateById(article) != 1, "保存失败");
        baseMapper.deleteArticleTmpContent(articleDto.getId());
        baseMapper.invalidateArticlePublishTiming(articleDto.getId(), "保存文章草稿【" + DateUtils.formatNow() + "】");
        return time;
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

    /**
     * 文章内容自动保存
     *
     * @param articleDto 文章内容信息
     * @return saveTime
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String autoSaveArticleContent(ArticleDto articleDto) {
        try {
            String saveTime = DateUtils.formatNow("yyyy-MM-dd HH:mm:ss");
            if (articleDto.getId() != null && StringUtils.isNotBlank(articleDto.getContent())) {
                ArticleTmpContentVo contentVo = new ArticleTmpContentVo();
                contentVo.setId(GenerateIdUtils.getId());
                contentVo.setArticleId(articleDto.getId());
                contentVo.setContent(articleDto.getContent());
                contentVo.setRemark(articleDto.getRemark());
                contentVo.setCount(articleDto.getExtend2());
                contentVo.setSaveTime(saveTime);
                baseMapper.insertArticleTmpContent(contentVo);
                return saveTime;
            }
        } catch (Exception e) {
            // 自动保存失败，回滚事务
            log.error("文章内容自动保存功能异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return null;
    }

    /**
     * 分页查询文章内容自动保存列表
     *
     * @param queryDto 查询参数
     * @return ArticleTmpContentVoList
     */
    @Override
    public PageInfo<ArticleTmpContentVo> queryAutoSaveArticleContentByPage(ArticleQueryDto queryDto) {
        queryDto.checkPage();
        Assert.notNull(queryDto.getArticleId(), "参数【articleId】不能为空");
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(baseMapper.selectAutoSaveArticleContentList(queryDto.getArticleId()));
    }

    /**
     * 查询文章内容自动保存信息
     *
     * @param recordId 记录序列
     * @return ArticleTmpContentVo
     */
    @Override
    public ArticleTmpContentVo queryAutoSaveArticleContentById(Long recordId) {
        if (recordId != null) {
            return baseMapper.selectAutoSaveArticleContentById(recordId);
        }
        return null;
    }

    /**
     * 文章定时发布
     *
     * @param articleDto 文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishArticleByTiming(ArticleDto articleDto) {
        String nowTime = DateUtils.formatNow();
        int delay = checkAutoPublishInfo(articleDto);
        Article article = articleConverter.dtoToEntity(articleDto);
        article.setDesc("文章将于 ".concat(articleDto.getAutoPublishTime()).concat(" 自动发布"));
        boolean isAdd = BaseConst.Action.ADD.equals(articleDto.getAction());
        Assert.isTrue(isAdd ? baseMapper.insert(article) != 1 : baseMapper.updateById(article) != 1, "保存失败");
        baseMapper.deleteArticleTmpContent(articleDto.getId());
        baseMapper.invalidateArticlePublishTiming(articleDto.getId(), "重新定时发布【" + nowTime + "】");
        Long publishId = GenerateIdUtils.getId();
        baseMapper.insertArticlePublishTiming(publishId, articleDto.getId(), articleDto.getAutoPublishTime(), nowTime);
        delayProducer.producerArticlePublishMessage(publishId, delay);
    }

    /**
     * 删除文章
     *
     * @param articleId 文章ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveArticleToRecycleBin(Long articleId) {
        Assert.notNull(articleId, "参数【articleId】不能为空");
        baseMapper.updateArticleStatus(articleId, ArticleStatus.RECYCLE.getType());
        baseMapper.invalidateArticlePublishTiming(articleId, "文章移至回收站【" + DateUtils.formatNow() + "】");
    }

    /**
     * 彻底删除文章
     *
     * @param articleId 文章ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long articleId) {
        Assert.notNull(articleId, "参数【articleId】不能为空");
        Assert.isTrue(baseMapper.deleteById(articleId) != 1, "删除失败");
    }

    /**
     * 批量删除文章
     *
     * @param articleIds 文章编号列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteArticle(List<Long> articleIds) {
        if (articleIds != null && articleIds.size() > 0) {
            baseMapper.batchInvalidateArticlePublishTiming(articleIds, "文章移至回收站【" + DateUtils.formatNow() + "】");
            baseMapper.batchUpdateArticleStatus(articleIds, ArticleStatus.RECYCLE.getType(), "文章被批量移至回收站");
        }
    }

    /**
     * 清空回收站
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearArticleRecycleBin() {
        Long authorId = SecurityUtils.getLoginId();
        baseMapper.clearArticleRecycleBin(authorId);
        baseMapper.deleteArticleByAuthor(authorId);
    }

    /**
     * 将文章回收至草稿箱
     *
     * @param articleId 文章ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recycleToDraftBox(Long articleId) {
        Assert.notNull(articleId, "参数【articleId】不能为空");

        // 是否允许操作
        ArticleVo dbInfo = baseMapper.selectArticleDetail(articleId);
        Long authorId = SecurityUtils.getLoginId();
        DataValidated.isTrue(!dbInfo.getAuthorId().equals(authorId), "没有权限操作其他作者的文章");

        // 状态检查
        DataValidated.isTrue(!ArticleStatus.RECYCLE.getType().equals(dbInfo.getArticleStatus()), "文章状态无效，刷新页面重试");

        // 修改状态
        baseMapper.updateArticleStatus(articleId, ArticleStatus.DRAFT.getType());
    }

    /**
     * 查询文章默认封面
     *
     * @return ArticleImg
     */
    @Override
    public String queryArticleDefaultImg() {
        Integer dbKey = WebsiteConfigType.OTHER_INFO.value();
        WebsiteConfig websiteConfig = webSettingMapper.selectById(dbKey);
        if (websiteConfig != null && StringUtils.isNotBlank(websiteConfig.getConfig())) {
            OtherInfo otherInfo = JSON.parseObject(websiteConfig.getConfig(), OtherInfo.class);
            return otherInfo.getArticleCover();
        }

        return null;
    }

    /**
     * 审核文章
     *
     * @param auditDto 审核信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditArticle(ArticleAuditDto auditDto) {
        Assert.notNull(auditDto.getStatus(), "审核状态不能为空");
        Assert.isTrue(StringUtils.isBlank(auditDto.getOpinion()), "审核意见不能为空");
        Assert.isTrue(auditDto.getArticleIds() == null || auditDto.getArticleIds().size() == 0, "文章列表不能为空");

        int noAuditCount = baseMapper.checkNoAuditCount(auditDto.getArticleIds());
        Assert.isTrue(noAuditCount > 0, "只能选择待审核状态的文章");

        // 待审核 => 已发布
        List<Long> auditIds1 = baseMapper.splitAuditArticleIds(auditDto.getArticleIds(), ArticleStatus.REVIEW.getType());
        if (auditIds1.size() > 0) {
            Integer status = auditDto.getStatus() ? ArticleStatus.PUBLISHED.getType() : ArticleStatus.FAILED_REVIEW.getType();
            baseMapper.batchUpdateArticleStatus(auditIds1, status, auditDto.getOpinion());
        }

        // 待审核 => 待发布
        List<Long> auditIds2 = baseMapper.splitAuditArticleIds(auditDto.getArticleIds(), ArticleStatus.PUBLISH_REVIEW.getType());
        if (auditIds2.size() > 0) {
            Integer status = auditDto.getStatus() ? ArticleStatus.TIME_PUBLISH.getType() : ArticleStatus.FAILED_REVIEW.getType();
            baseMapper.batchUpdateArticleStatus(auditIds2, status, auditDto.getOpinion());
        }
    }

    private void emptyCheck(ArticleDto articleDto) {
        Assert.notNull(articleDto.getId(), "文章序列不能为空");
        Assert.isTrue(StringUtils.isBlank(articleDto.getAction()), "操作类型不能为空");
        Assert.isTrue(StringUtils.isBlank(articleDto.getTitle()), "文章标题不能为空");
        Assert.isTrue(StringUtils.isBlank(articleDto.getContent()), "文章内容不能为空");
        Assert.isTrue(StringUtils.isBlank(articleDto.getRemark()), "文章摘要不能为空");
        Assert.isTrue(StringUtils.isBlank(articleDto.getExtend1()), "编辑器类型不能为空");
        Assert.notNull(articleDto.getCategoryId(), "文章分类不能为空");
        Assert.notNull(articleDto.getArticlePerm(), "发布范围不能为空");
        Assert.notNull(articleDto.getCommentFlag(), "评论设置不能为空");
        Assert.notNull(articleDto.getCoverImageType(), "封面类型不能为空");
        articleDto.setTitle(articleDto.getTitle().trim());
    }

    private void actionCheck(ArticleDto articleDto) {
        Long loginId = SecurityUtils.getLoginId();
        if (BaseConst.Action.ADD.equals(articleDto.getAction())) {
            // 新增
            articleDto.setAuthorId(loginId);
        } else if (BaseConst.Action.EDIT.equals(articleDto.getAction())) {
            // 编辑
            Article dbInfo = baseMapper.selectOneWithDeleted(articleDto.getId());
            checkArticleIsAllowEdit(dbInfo);
        } else {
            throw new ServiceException("无法处理的操作类型：" + articleDto.getAction());
        }
    }

    private void checkArticle(ArticleDto articleDto) {
        // 非空校验
        emptyCheck(articleDto);

        // 文章类型校验
        Integer articleType = articleDto.getArticleType();
        Assert.notNull(articleType, "文章类型不能为空");
        if (!ArticleType.ORIGINAL.getType().equals(articleType)) {
            Assert.isTrue(StringUtils.isBlank(articleDto.getReprintLink()), "原文链接不能为空");
        }

        // 操作状态校验
        actionCheck(articleDto);

        // 设置文章内容（去除标签）
        articleDto.setContentMd(HtmlContentUtils.getTextFromHtml(articleDto.getContent()).replaceAll("&nbsp;", ""));

        // 设置文章状态（对外不可见的文章无须审批）
        Integer articlePerm = articleDto.getArticlePerm();
        boolean isSelfShow = articlePerm.equals(ArticlePerm.SELF_SHOW.getType());
        Integer status = isSelfShow ? ArticleStatus.PUBLISHED.getType() : ArticleStatus.REVIEW.getType();
        articleDto.setArticleStatus(status);

        // 封面校验
        articleImgConfig(articleDto);

        // 标签校验，自定义标签入库，文章关键字生成
        List<String> tags = articleDto.getArticleTag();
        Assert.isTrue(tags == null || tags.size() == 0, "文章标签不能为空");
        articleTagConfig(articleDto);
    }

    private String checkDraftInfo(ArticleDto articleDto) {
        String saveTime = DateUtils.formatNow("HH:mm:ss");
        Assert.notNull(articleDto.getId(), "文章序列不能为空");
        articleDto.setTitle(StringUtils.isBlank(articleDto.getTitle()) ? "【无标题】" : articleDto.getTitle().trim());

        Long loginId = SecurityUtils.getLoginId();
        if (BaseConst.Action.ADD.equals(articleDto.getAction())) {
            // 新增
            articleDto.setAuthorId(loginId);
        } else if (BaseConst.Action.EDIT.equals(articleDto.getAction())) {
            // 编辑
            Article dbInfo = baseMapper.selectOneWithDeleted(articleDto.getId());
            if (dbInfo == null) {
                // 文章不存在，新增
                articleDto.setAction(BaseConst.Action.ADD);
                articleDto.setAuthorId(loginId);
            } else {
                checkArticleIsAllowEdit(dbInfo);
            }
        } else {
            throw new ServiceException("无法处理的操作类型：" + articleDto.getAction());
        }

        // 设置文章状态（对外不可见的文章无须审批）
        articleDto.setArticleStatus(ArticleStatus.DRAFT.getType());

        // 封面校验
        articleImgConfig(articleDto);

        // 标签校验，自定义标签入库，文章关键字生成
        List<String> tags = articleDto.getArticleTag();
        if (tags != null && tags.size() != 0) {
            articleTagConfig(articleDto);
        }

        return saveTime;
    }

    private int checkAutoPublishInfo(ArticleDto articleDto) {
        // 非空校验
        emptyCheck(articleDto);
        Assert.notNull(articleDto.getAutoPublishTime(), "发布时间不能为空");

        // 文章类型校验
        Integer articleType = articleDto.getArticleType();
        Assert.notNull(articleType, "文章类型不能为空");
        if (!ArticleType.ORIGINAL.getType().equals(articleType)) {
            Assert.isTrue(StringUtils.isBlank(articleDto.getReprintLink()), "原文链接不能为空");
        }

        // 发布时间校验
        Date nowDate = new Date();
        Date publishDate;
        try {
            publishDate = DateUtils.parseDate(articleDto.getAutoPublishTime(), "yyyy-MM-dd HH:mm");
        } catch (Exception e) {
            throw new ServiceException("发布时间格式不正确", e);
        }
        long delay = publishDate.getTime() - nowDate.getTime();
        Assert.isTrue(delay <= 0 || delay > Integer.MAX_VALUE, "发布时间无效");

        // 新增编辑校验
        actionCheck(articleDto);

        // 发布类型校验
        Integer articlePerm = articleDto.getArticlePerm();
        Assert.isTrue(ArticlePerm.SELF_SHOW.getType().equals(articlePerm), "私密文章暂不支持定时发布");

        // 设置文章状态
        articleDto.setArticleStatus(ArticleStatus.PUBLISH_REVIEW.getType());

        // 封面校验
        articleImgConfig(articleDto);

        // 标签校验，自定义标签入库，文章关键字生成
        List<String> tags = articleDto.getArticleTag();
        Assert.isTrue(tags == null || tags.size() == 0, "文章标签不能为空");
        articleTagConfig(articleDto);

        // 返回发布毫秒数
        return (int) delay;
    }

    private void checkArticleIsAllowEdit(Article dbInfo) {
        // 检查文章是否存在
        Assert.notNull(dbInfo, "操作失败，文章不存在");

        // 检查文章是否被删除
        Assert.isTrue(dbInfo.getDeleted(), "操作失败，文章已被删除");

        // 检查作者
        Long loginId = SecurityUtils.getLoginId();
        DataValidated.isTrue(!dbInfo.getAuthorId().equals(loginId), "没有权限修改其他作者的文章");

        // 检查文章是否已经被删除
        DataValidated.isTrue(ArticleStatus.RECYCLE.getType().equals(dbInfo.getArticleStatus()), "文章处于回收站中，不允许编辑，请先把文章移动到草稿箱在进行编辑");
    }

    private void articleImgConfig(ArticleDto articleDto) {
        Set<Long> fileIds = new HashSet<>();
        List<File> files = articleDto.getArticleImg();
        Integer imageType = articleDto.getCoverImageType();
        if (ArticleImgType.SINGLE.getType().equals(imageType)) {
            // 单封面
            Assert.isTrue(files == null || files.size() == 0, "文章封面不能为空");
            fileIds.add(files.get(0).getId());
        } else if (ArticleImgType.MULTI.getType().equals(imageType)) {
            // 多封面
            Assert.isTrue(files == null || files.size() == 0, "文章封面不能为空");
            if (files.size() == 1) {
                // 只上传了一个封面则视为单封面
                articleDto.setCoverImageType(ArticleImgType.SINGLE.getType());
                fileIds.add(files.get(0).getId());
            } else {
                for (int i = 0; i < files.size() && i < MAX_ARTICLE_IMG_SIZE; i++) {
                    fileIds.add(files.get(i).getId());
                }
            }
        } else if (ArticleImgType.AUTO_GENERATE.getType().equals(imageType)) {
            // 自动生成封面
            File newFile = autoGenerateArticleImg(articleDto.getTitle());
            fileIds.add(newFile.getId());
        } else {
            // 默认封面、无封面、未传参（草稿）
            if (files != null && files.size() != 0) {
                // 删除图片
                files.forEach(file -> fileService.deleteFile(file.getId(), null));
            }
        }

        // 物理删除中间数据
        articleImgRelationMapper.deleteByArticleId(articleDto.getId());

        if (fileIds.size() != 0) {
            for (Long fileId : fileIds) {
                // 插入新的中间数据
                ArticleImgRelation relation = new ArticleImgRelation();
                relation.setArticleId(articleDto.getId());
                relation.setFileId(fileId);
                articleImgRelationMapper.insert(relation);
            }
        }
    }

    private File autoGenerateArticleImg(String title) {
        try {
            // 本地生成封面
            String filePath = ImageBuilderUtils.buildFontImage(title, FileUtils.SYS_TEM_DIR);

            // 上传至服务器
            java.io.File file = new java.io.File(filePath);
            FileInputStream fileInputStream = new FileInputStream(filePath);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), null, fileInputStream);
            MultipartFile[] files = new MultipartFile[] {multipartFile};

            int fileUploadType = fileService.getFileUploadType();
            FileHandler fileHandler = FileTypeHandler.instance(fileUploadType);
            List<FileInfo> fileList = fileHandler.uploadFile(files);
            List<File> insertFile = fileService.insertFile(FileUploadType.NORMAL.getType(), fileList);
            return insertFile.get(0);
        } catch (Exception e) {
            throw new ServiceException("封面自动生成失败，请选择其他封面类型", e);
        }
    }

    private void articleTagConfig(ArticleDto articleDto) {
        Set<Long> tagIds = new HashSet<>();
        StringBuilder keywords = new StringBuilder();
        for (String tag : articleDto.getArticleTag()) {
            if (MathUtils.isInteger(tag)) {
                Long tagId = Long.parseLong(tag);
                ArticleTag articleTag = articleTagMapper.selectById(tagId);
                if (articleTag != null) {
                    // 已有标签
                    tagIds.add(tagId);
                    keywords.append(articleTag.getName()).append(",");
                    continue;
                }
            }

            // 自定义标签，或根据ID查询标签不存在
            ArticleTag articleTag = articleTagMapper.selectByTagName(tag);
            if (articleTag != null) {
                // 标签名称已存在，不需要再创建
                tagIds.add(articleTag.getId());
                keywords.append(articleTag.getName()).append(",");
            } else {
                // 创建自定义标签
                ArticleTag customTag = new ArticleTag();
                Long customTagId = GenerateIdUtils.getId();
                customTag.setId(customTagId);
                customTag.setName(tag);
                customTag.setType(ArticleTagType.CUSTOM.getType());
                customTag.setEnabled(true);
                articleTagMapper.insert(customTag);

                tagIds.add(customTagId);
                keywords.append(tag).append(",");
            }
        }

        // 设置关键字
        articleDto.setKeywords(keywords.length() > 0 ? keywords.substring(0, keywords.length() - 1) : "");

        // 物理删除中间数据
        articleTagRelationMapper.deleteByArticleId(articleDto.getId());

        for (Long tagId : tagIds) {
            // 插入新的中间数据
            ArticleTagRelation relation = new ArticleTagRelation();
            relation.setArticleId(articleDto.getId());
            relation.setTagId(tagId);
            articleTagRelationMapper.insert(relation);
        }
    }
}
