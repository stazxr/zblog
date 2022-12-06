package com.github.stazxr.zblog.service.impl;

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
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.domain.dto.ArticleDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.entity.Article;
import com.github.stazxr.zblog.domain.entity.ArticleImgRelation;
import com.github.stazxr.zblog.domain.entity.ArticleTag;
import com.github.stazxr.zblog.domain.entity.ArticleTagRelation;
import com.github.stazxr.zblog.domain.enums.*;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import com.github.stazxr.zblog.mapper.ArticleImgRelationMapper;
import com.github.stazxr.zblog.mapper.ArticleMapper;
import com.github.stazxr.zblog.mapper.ArticleTagMapper;
import com.github.stazxr.zblog.mapper.ArticleTagRelationMapper;
import com.github.stazxr.zblog.service.ArticleService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.graphics.ImageBuilderUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.math.MathUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
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

    /**
     * 分页查询文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleVoList
     */
    @Override
    public PageInfo<ArticleVo> queryArticleListByPage(ArticleQueryDto queryDto) {
        // 查询当前用户信息
        String loginUsername = SecurityUtils.getLoginUsername();
        queryDto.setLoginUser(loginUsername);

        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(baseMapper.selectArticleList(queryDto));
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

        Long loginId = SecurityUtils.getLoginId();
        boolean allowSee = ArticlePerm.SELF_SHOW.getType().equals(articleVo.getArticlePerm()) && !loginId.equals(articleVo.getAuthorId());
        DataValidated.isTrue(!allowSee, "文章是私密文章，禁止查看");
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
        checkArticle(articleDto);
        Article article = articleConverter.dtoToEntity(articleDto);
        Assert.isTrue(baseMapper.insert(article) != 1, "新增失败");
    }

    /**
     * 编辑文章
     *
     * @param articleDto 文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editArticle(ArticleDto articleDto) {
        checkArticle(articleDto);
        Article article = articleConverter.dtoToEntity(articleDto);
        Assert.isTrue(baseMapper.updateById(article) != 1, "修改失败");
    }

    /**
     * 删除文章，逻辑删除，移至回收站
     *
     * @param articleId 文章ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long articleId) {
        Assert.notNull(articleId, "参数【articleId】不能为空");
        baseMapper.updateArticleStatus(articleId, ArticleStatus.RECYCLE);
    }

    private void checkArticle(ArticleDto articleDto) {
        // 非空校验
        Assert.notNull(articleDto.getTitle(), "文章标题不能为空");
        Assert.notNull(articleDto.getContent(), "文章内容不能为空");
        Assert.notNull(articleDto.getRemark(), "文章摘要不能为空");
        Assert.notNull(articleDto.getCategoryId(), "文章分类不能为空");
        Assert.notNull(articleDto.getArticlePerm(), "发布范围不能为空");
        Assert.notNull(articleDto.getCommentFlag(), "评论设置不能为空");

        // 文章类型校验
        Integer articleType = articleDto.getArticleType();
        Assert.notNull(articleType, "文章类型不能为空");
        if (!ArticleType.ORIGINAL.getType().equals(articleType)) {
            Assert.notNull(articleDto.getReprintLink(), "原文链接不能为空");
        }

        Long loginId = SecurityUtils.getLoginId();
        Integer articlePerm = articleDto.getArticlePerm();
        if (articleDto.getId() == null) {
            // 新增
            articleDto.setId(GenerateIdUtils.getId());
            articleDto.setAuthorId(loginId);
        } else {
            // 编辑
            Article dbInfo = baseMapper.selectById(articleDto.getId());
            Assert.notNull(dbInfo, "编辑失败，文章不存在");
            DataValidated.isTrue(!dbInfo.getAuthorId().equals(loginId), "没有权限修改其他作者的文章");
        }

        // 设置文章状态
        Integer status = articlePerm.equals(ArticlePerm.SELF_SHOW.getType()) ? ArticleStatus.PUBLISHED.getType() : ArticleStatus.REVIEW.getType();
        articleDto.setArticleStatus(status);

        // 封面校验
        articleImgConfig(articleDto);

        // 标签校验，自定义标签入库，文章关键字生成
        List<String> tags = articleDto.getArticleTag();
        Assert.isTrue(tags == null || tags.size() == 0, "文章标签不能为空");
        articleTagConfig(articleDto);
    }

    private void articleImgConfig(ArticleDto articleDto) {
        Set<Long> fileIds = new HashSet<>();
        List<File> files = articleDto.getArticleImg();
        Integer imageType = articleDto.getCoverImageType();
        Assert.notNull(imageType, "封面类型不能为空");
        if (imageType.equals(ArticleImgType.SINGLE.getType())) {
            // 单封面
            Assert.isTrue(files == null || files.size() == 0, "文章封面不能为空");
            fileIds.add(files.get(0).getId());
        } else if (imageType.equals(ArticleImgType.MULTI.getType())) {
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
        } else if (imageType.equals(ArticleImgType.AUTO_GENERATE.getType())) {
            // 自动生成封面
            File newFile = autoGenerateArticleImg(articleDto.getTitle());
            fileIds.add(newFile.getId());
        } else {
            if (files.size() != 0) {
                // 删除图片
                files.forEach(file -> fileService.deleteFile(file.getId(), null));
            }
        }

        // 物理删除中间数据
        articleImgRelationMapper.deleteByArticleId(articleDto.getId());

        for (Long fileId : fileIds) {
            // 插入新的中间数据
            ArticleImgRelation relation = new ArticleImgRelation();
            relation.setArticleId(articleDto.getId());
            relation.setFileId(fileId);
            articleImgRelationMapper.insert(relation);
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
