package com.github.stazxr.zblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.encryption.util.RsaUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.component.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.base.component.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.base.converter.UserConverter;
import com.github.stazxr.zblog.base.domain.bo.QqLoginParam;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.entity.UserTokenStorage;
import com.github.stazxr.zblog.base.domain.entity.Version;
import com.github.stazxr.zblog.base.domain.enums.Gender;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.base.mapper.UserMapper;
import com.github.stazxr.zblog.base.mapper.VersionMapper;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.converter.CommentConverter;
import com.github.stazxr.zblog.converter.MessageConverter;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.domain.bo.GitCalendarData;
import com.github.stazxr.zblog.domain.dto.*;
import com.github.stazxr.zblog.domain.bo.PageInfo;
import com.github.stazxr.zblog.domain.dto.query.AlbumPhotoQueryDto;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.dto.query.CommentQueryDto;
import com.github.stazxr.zblog.domain.dto.query.TalkQueryDto;
import com.github.stazxr.zblog.domain.dto.setting.OtherInfo;
import com.github.stazxr.zblog.domain.dto.setting.SocialInfo;
import com.github.stazxr.zblog.domain.dto.setting.WebInfo;
import com.github.stazxr.zblog.domain.entity.*;
import com.github.stazxr.zblog.domain.enums.WebsiteConfigType;
import com.github.stazxr.zblog.domain.vo.*;
import com.github.stazxr.zblog.mapper.*;
import com.github.stazxr.zblog.service.PortalService;
import com.github.stazxr.zblog.strategy.context.ArticleSearchStrategyContext;
import com.github.stazxr.zblog.util.*;
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import com.github.stazxr.zblog.util.git.GithubUtils;
import com.github.stazxr.zblog.util.http.HtmlContentUtils;
import com.github.stazxr.zblog.util.http.HttpUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 前台服务实现层
 *
 * @author SunTao
 * @since 2022-11-25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PortalServiceImpl implements PortalService {
    private static final String UNKNOWN_AREA = "未知";

    private final PortalMapper portalMapper;

    private final VisitorMapper visitorMapper;

    private final VisitorAreaMapper visitorAreaMapper;

    private final WebSettingMapper webSettingMapper;

    private final PageMapper pageMapper;

    private final TalkMapper talkMapper;

    private final ArticleMapper articleMapper;

    private final ArticleTagMapper articleTagMapper;

    private final ArticleCategoryMapper articleCategoryMapper;

    private final MessageConverter messageConverter;

    private final MessageMapper messageMapper;

    private final FriendLinkMapper friendLinkMapper;

    private final CommentMapper commentMapper;

    private final CommentLikeMapper commentLikeMapper;

    private final CommentConverter commentConverter;

    private final UserMapper userMapper;

    private final UserConverter userConverter;

    private final PasswordEncoder passwordEncoder;

    private final TalkLikeMapper talkLikeMapper;

    private final ArticleLikeMapper articleLikeMapper;

    private final ArticleViewMapper articleViewMapper;

    private final AlbumMapper albumMapper;

    private final UserService userService;

    private final AlbumPhotoMapper albumPhotoMapper;

    private final ArticleColumnMapper articleColumnMapper;

    private final VersionMapper versionMapper;

    private final ArticleColumnRelationMapper articleColumnRelationMapper;

    private final ArticleSearchStrategyContext articleSearchStrategyContext;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final JwtTokenStorage jwtTokenStorage;

    /**
     * 查询博客前台信息
     *
     * @return BlogWebVo
     */
    @Override
    public BlogWebVo queryBlogWebInfo() {
        BlogWebVo webVo = portalMapper.selectBlogWebInfo();

        // WebInfo
        String websiteConfig = getWebInfoFromCache();
        webVo.setWebInfo(StringUtils.isBlank(websiteConfig) ? new WebInfo() : JSON.parseObject(websiteConfig, WebInfo.class));

        // SocialInfo
        websiteConfig = getSocialInfoFromCache();
        webVo.setSocialInfo(websiteConfig == null ? new SocialInfo() : JSON.parseObject(websiteConfig, SocialInfo.class));

        // OtherInfo
        websiteConfig = getOtherInfoFromCache();
        OtherInfo otherInfo = websiteConfig == null ? new OtherInfo() : JSON.parseObject(websiteConfig, OtherInfo.class);
        webVo.setOtherInfo(otherInfo);
        webVo.setArticleDefaultImg(otherInfo.getArticleCover());

        // PageInfo
        List<PageInfo> pageList = pageMapper.selectWebPageList();
        webVo.setPageList(pageList);

        return webVo;
    }

    /**
     * 记录访客信息
     *
     * @param request 请求信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordVisitor(HttpServletRequest request) {
        // 获取访问信息
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem os = userAgent.getOperatingSystem();
        String ipAddress = IpUtils.getIp(request);

        // 生成访问唯一编码
        String uuid = ipAddress + os.getName();
        String md5Uuid = DigestUtils.md5DigestAsHex(uuid.getBytes());

        // 判断是否访问过
        String browserName = IpUtils.getBrowser(request);
        Visitor visitor = Visitor.builder().id(md5Uuid).addressIp(ipAddress).browserName(browserName).osName(os.getName()).build();
        boolean exists = visitorMapper.exists(new LambdaQueryWrapper<Visitor>().eq(Visitor::getId, md5Uuid));
        if (!exists) {
            // 第一次访问
            String province = IpUtils.getIpLocation(ipAddress, IpUtils.IP_LOCATION_PRO);
            if (StringUtils.isBlank(province)) {
                province = UNKNOWN_AREA;
            }

            // 保存访客信息
            visitor.setProvince(province);
            updateVisitorAreaCount(province);
            visitorMapper.insert(visitor);
        }

        // 网站访问量自增
        addVisitorCount();
    }

    private void addVisitorCount() {
        String today = DateUtils.formatDate();
        visitorMapper.addWebVisitorCount();
        if (visitorMapper.existsDate(today)) {
            visitorMapper.addWebVisitorTodayCount(today);
        } else {
            visitorMapper.insertWebVisitorTodayCount(today);
        }
    }

    /**
     * 查询首页轮播的说说列表
     *
     * @return TalkList
     */
    @Override
    public List<TalkVo> queryBoardTalkList() {
        return talkMapper.queryBoardTalkList();
    }

    /**
     * 分页查询前台文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleList
     */
    @Override
    public com.github.pagehelper.PageInfo<ArticleVo> queryArticleList(ArticleQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new com.github.pagehelper.PageInfo<>(articleMapper.selectWebArticleList(queryDto));
    }

    /**
     * 分页查询前台文章详情
     *
     * @param articleId 文章ID
     * @return ArticleVo
     */
    @Override
    public ArticleVo queryArticleDetail(Long articleId) {
        if (articleId == null) {
            // 返回空，前台处理
            return null;
        }

        // 查询文章信息
        ArticleVo articleVo = articleMapper.selectArticleDetail(articleId);
        if (articleVo != null) {
            // 查找上一篇文章
            articleVo.setLastArticle(articleMapper.selectLastArticle(articleId));

            // 查找下一篇文章
            articleVo.setNextArticle(articleMapper.selectNextArticle(articleId));

            // 查找推荐文章（按照标签、类别查询）
            articleVo.setRecommendList(articleMapper.selectRecommendList(articleId));

            // 查找最新文章
            articleVo.setNewestList(articleMapper.selectNewestList());
        }

        return articleVo;
    }

    /**
     * 留言板留言
     *
     * @param request    请求信息
     * @param messageDto 留言信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMessage(HttpServletRequest request, MessageDto messageDto) {
        // 留言审核开启状态
        WebsiteConfig websiteConfig = webSettingMapper.selectById(WebsiteConfigType.OTHER_INFO.value());
        OtherInfo otherInfo = websiteConfig == null ? new OtherInfo() : JSON.parseObject(websiteConfig.getConfig(), OtherInfo.class);
        Integer isReview = otherInfo.getIsMessageReview();

        // 设置请求信息
        Message message = messageConverter.dtoToEntity(messageDto);
        String ip = IpUtils.getIp(request);
        message.setIpAddress(ip);
        message.setMessageContent(HtmlContentUtils.filter(message.getMessageContent()));
        message.setIpSource(IpUtils.getIpLocation(ip));
        message.setId(SequenceUtils.getId());
        message.setIsReview(1 != isReview);
        messageMapper.insert(message);
    }

    /**
     * 查询前台弹幕列表
     *
     * @return MessageVo
     */
    @Override
    public List<MessageVo> queryMessageList() {
        return messageMapper.selectWebMessageList();
    }

    /**
     * 查询前台友链列表
     *
     * @return FriendLinkVo
     */
    @Override
    public List<FriendLinkVo> queryFriendLinkList() {
        return friendLinkMapper.selectWebFriendLinkList();
    }

    /**
     * 前台登录
     *
     * @param request  请求信息
     * @param loginDto 登录信息
     * @return User
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVo webLogin(HttpServletRequest request, UserLoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        Assert.isTrue(StringUtils.isBlank(username), "登录账号不能为空");
        Assert.isTrue(StringUtils.isBlank(password), "登录密码不能为空");

        // 判断用户名或邮箱是否存在
        boolean isEmail = username.contains("@");
        User user = userMapper.selectLoginUserByUsernameOrEmail(username, isEmail);
        DataValidated.notNull(user, "账户不存在");
        DataValidated.isTrue(!user.isEnabled(), "账户未启用");
        DataValidated.isTrue(user.getLocked(), "账户被锁定");

        // 对密码进行解密
        try {
            Resource resource = new ClassPathResource("pri.key");
            String priKeyBase64 = FileUtils.readFileFromStream(resource.getInputStream());
            password = RsaUtils.decryptByPrivateKey(priKeyBase64, password);
        } catch (Exception e) {
            throw new ServiceException("密码解析失败，请联系管理员", e);
        }

        DataValidated.isTrue(!passwordEncoder.matches(password, user.getPassword()), "密码错误");

        // 更新用户登录时间
        user.setLoginTime(DateUtils.formatNow());
        userMapper.updateById(user);
        return parseTokenUser(user, request);
    }

    /**
     * 查询前台评论列表
     *
     * @param queryDto 查询参数
     * @return CommentVo
     */
    @Override
    public com.github.pagehelper.PageInfo<CommentVo> queryCommentList(CommentQueryDto queryDto) {
        PageHelper.startPage(queryDto.getCurrent(), queryDto.getDefaultPageSize());
        List<CommentVo> commentVos = commentMapper.selectWebCommentList(queryDto);
        return new com.github.pagehelper.PageInfo<>(commentVos);
    }

    /**
     * 获取评论回复列表
     *
     * @param queryDto 查询参数
     * @return CommentReplyVo
     */
    @Override
    public com.github.pagehelper.PageInfo<CommentReplyVo> queryCommentReplyList(CommentQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<CommentReplyVo> commentVos = commentMapper.selectWebCommentReplyList(queryDto);
        return new com.github.pagehelper.PageInfo<>(commentVos);
    }

    /**
     * 新增评论
     *
     * @param request    请求信息
     * @param commentDto 评论信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveComment(HttpServletRequest request, CommentDto commentDto) {
        // 评论审核开启状态
        WebsiteConfig websiteConfig = webSettingMapper.selectById(WebsiteConfigType.OTHER_INFO.value());
        OtherInfo otherInfo = websiteConfig == null ? new OtherInfo() : JSON.parseObject(websiteConfig.getConfig(), OtherInfo.class);
        Integer isReview = otherInfo.getIsCommentReview();

        // 保存评论 如果需要过滤不文明字符，使用 HtmlContentUtils.filter(commentDto.getContent())
        Comment comment = commentConverter.dtoToEntity(commentDto);
        comment.setId(SequenceUtils.getId());
        String ip = IpUtils.getIp(request);
        comment.setIpAddress(ip);
        comment.setIpSource(IpUtils.getIpLocation(ip));
        comment.setIsReview(1 != isReview);
        commentMapper.insert(comment);

        // 判断是否开启邮箱通知,通知用户
        if (Constants.TRUE.equals(otherInfo.getIsEmailNotice())) {
            CompletableFuture.runAsync(() -> notice(comment));
        }
    }

    /**
     * 点赞评论
     *
     * @param request    请求信息
     * @param commentDto 评论信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeComment(HttpServletRequest request, CommentLikeDto commentDto) {
        if (commentLikeMapper.isLiked(commentDto.getUserId(), commentDto.getCommentId())) {
            // 取消点赞
            commentLikeMapper.deleteLike(commentDto.getUserId(), commentDto.getCommentId());
        } else {
            // 点赞
            CommentLike commentLike = new CommentLike();
            commentLike.setId(SequenceUtils.getId());
            commentLike.setUserId(commentDto.getUserId());
            commentLike.setCommentId(commentDto.getCommentId());
            String ip = IpUtils.getIp(request);
            commentLike.setIpAddress(ip);
            commentLike.setIpSource(IpUtils.getIpLocation(ip));
            commentLikeMapper.insert(commentLike);
        }
    }

    /**
     * 删除评论
     *
     * @param commentDto 评论信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(CommentDeleteDto commentDto) {
        Assert.notNull(commentDto.getUserId(), "请登录后在做删除操作");
        if (commentDto.getCommentId() != null) {
            Comment comment = commentMapper.selectById(commentDto.getCommentId());
            if (comment != null) {
                DataValidated.isTrue(!comment.getUserId().equals(commentDto.getUserId()), "无法删除其他用户的评论");
                commentMapper.deleteById(commentDto.getCommentId());
            }
        }
    }

    /**
     * 查询前台说说列表
     *
     * @param queryDto 查询参数
     * @return TalkList
     */
    @Override
    public com.github.pagehelper.PageInfo<TalkVo> queryTalkList(TalkQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<TalkVo> talkVos = talkMapper.selectWebTalkList(queryDto);
        return new com.github.pagehelper.PageInfo<>(talkVos);
    }

    /**
     * 点赞说说
     *
     * @param request 请求信息
     * @param talkDto 说说信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeTalk(HttpServletRequest request, TalkLikeDto talkDto) {
        if (talkLikeMapper.isLiked(talkDto.getUserId(), talkDto.getTalkId())) {
            // 取消点赞
            talkLikeMapper.deleteLike(talkDto.getUserId(), talkDto.getTalkId());
        } else {
            // 点赞
            TalkLike talkLike = new TalkLike();
            talkLike.setId(SequenceUtils.getId());
            talkLike.setUserId(talkDto.getUserId());
            talkLike.setTalkId(talkDto.getTalkId());
            String ip = IpUtils.getIp(request);
            talkLike.setIpAddress(ip);
            talkLike.setIpSource(IpUtils.getIpLocation(ip));
            talkLikeMapper.insert(talkLike);
        }
    }

    /**
     * 点赞文章
     *
     * @param request    请求信息
     * @param articleDto 文章信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeArticle(HttpServletRequest request, ArticleLikeDto articleDto) {
        if (articleLikeMapper.isLiked(articleDto.getUserId(), articleDto.getArticleId())) {
            // 取消点赞
            articleLikeMapper.deleteLike(articleDto.getUserId(), articleDto.getArticleId());
        } else {
            // 点赞
            ArticleLike articleLike = new ArticleLike();
            articleLike.setId(SequenceUtils.getId());
            articleLike.setUserId(articleDto.getUserId());
            articleLike.setArticleId(articleDto.getArticleId());
            String ip = IpUtils.getIp(request);
            articleLike.setIpAddress(ip);
            articleLike.setIpSource(IpUtils.getIpLocation(ip));
            articleLikeMapper.insert(articleLike);
        }
    }

    /**
     * 浏览文章
     *
     * @param request   请求信息
     * @param articleId 文章序列
     * @return 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean viewArticle(HttpServletRequest request, Long articleId) {
        if (articleId != null) {
            try {
                // 文章浏览配置
                WebsiteConfig websiteConfig = webSettingMapper.selectById(WebsiteConfigType.OTHER_INFO.value());
                OtherInfo otherInfo = websiteConfig == null ? new OtherInfo() : JSON.parseObject(websiteConfig.getConfig(), OtherInfo.class);
                int interval = otherInfo.getArticleViewInterval();
                String accessIp = IpUtils.getIp(request);
                if (interval != 0) {
                    // 浏览记录非每次都增加，需验证上一次浏览记录
                    ArticleView record = articleViewMapper.selectLastedRecord(articleId, accessIp);
                    if (record != null) {
                        if (interval == -1) {
                            return false;
                        }

                        // 浏览时间校验
                        Date beforeDate = DateUtils.addMinutes(new Date(), - interval);
                        if (DateUtils.parseDate(record.getAccessTime(), DateUtils.YMD_HMS_PATTERN).after(beforeDate)) {
                            return false;
                        }
                    }
                }

                // 新增
                ArticleView view = new ArticleView();
                view.setId(SequenceUtils.getId());
                view.setArticleId(articleId);
                view.setAccessIp(accessIp);
                view.setAccessAddress(IpUtils.getIpLocation(accessIp));
                view.setAccessTime(DateUtils.formatNow());
                return 1 == articleViewMapper.insert(view);
            } catch (Exception e) {
                log.error("set viewArticle failed", e);
            }
        }

        return false;
    }

    /**
     * 根据关键字搜索文章
     *
     * @param keywords 查询关键字
     * @return ArticleList
     */
    @Override
    public List<ArticleVo> searchArticleList(String keywords) {
        return articleSearchStrategyContext.executeSearchStrategy(keywords);
    }

    /**
     * 获取标签云数据
     *
     * @return CloudTagVos
     */
    @Override
    public List<CloudTagVo> queryBoardTagList() {
        return articleTagMapper.queryBoardTagList();
    }

    /**
     * 获取热门文章列表
     *
     * @return ArticleVos
     */
    @Override
    public List<ArticleVo> queryBoardHotArticleList() {
        return articleMapper.selectWebBoardHotArticleList();
    }

    /**
     * 获取分类专栏列表
     *
     * @return CategoryVos
     */
    @Override
    public List<ArticleCategoryVo> queryBoardCategoryList() {
        List<ArticleCategoryVo> categoryList = articleCategoryMapper.selectWebBoardCategoryList();
        Map<Long, List<ArticleCategoryVo>> categoryPidGroupMap = categoryList.stream().collect(
            Collectors.groupingBy(v -> v.getPid() == null ? Constants.TOP_ID : v.getPid())
        );

        List<ArticleCategoryVo> result = new ArrayList<>();
        for (ArticleCategoryVo categoryVo : categoryList) {
            if (categoryVo.getPid() == null) {
                result.add(categoryVo);
                if (categoryPidGroupMap.containsKey(categoryVo.getId())) {
                    result.addAll(categoryPidGroupMap.get(categoryVo.getId()));
                }
            }
        }

        return result;
    }

    /**
     * 获取最新评论列表
     *
     * @return CommentVos
     */
    @Override
    public List<CommentVo> queryBoardLastedCommentList() {
        return commentMapper.selectBoardLastedCommentList();
    }

    /**
     * 查询 Github 贡献日历数据
     *
     * @param username Github 用户名
     * @return 贡献日历数据
     */
    @Override
    public GitCalendarData queryGithubCalendarData(String username) {
        try {
            // 从 Redis 拿去数据
            YwConstants.CacheKey cacheKey = YwConstants.CacheKey.githubCalendarData;
            String key = cacheKey.cacheKey().concat(username);
            String jsonData = GlobalCache.get(key);
            if (jsonData != null) {
                return JSON.parseObject(jsonData, GitCalendarData.class);
            }

            // 查询新数据
            List<Map<String, Object>> data = GithubUtils.getGithubCalendarData(username);
            GitCalendarData calendarData = new GitCalendarData();
            calendarData.setGithubUser(username);
            calendarData.setGithubUrl(GithubUtils.BASE_URL.concat("/").concat(username));
            calendarData.initData(data);

            // 缓存新数据
            GlobalCache.put(key, JSON.toJSONString(calendarData), cacheKey.duration());
            return calendarData;
        } catch (Exception e) {
            throw new ServiceException("贡献日历数据获取超时", e);
        }
    }

    /**
     * 查询前台相册列表
     *
     * @return AlbumVo
     */
    @Override
    public List<AlbumVo> queryAlbumList() {
        return albumMapper.selectWebAlbumList();
    }

    /**
     * 查询前台相册照片列表
     *
     * @param queryDto 查询参数
     * @return PortalAlbumPhotoVo
     */
    @Override
    public PortalAlbumPhotoVo queryAlbumPhotoList(AlbumPhotoQueryDto queryDto) {
        Assert.notNull(queryDto.getAlbumId(), "参数【albumId】不能为空");
        AlbumVo albumVo = albumMapper.selectAlbumDetail(queryDto.getAlbumId());
        Assert.notNull(albumVo, "相册信息不存在");

        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<AlbumPhotoVo> photoVos = albumPhotoMapper.selectAlbumPhotoList(queryDto);

        PortalAlbumPhotoVo albumPhotoVo = new PortalAlbumPhotoVo();
        albumPhotoVo.setPhotoAlbumName(albumVo.getAlbumName());
        albumPhotoVo.setPhotoAlbumCover(albumVo.getAlbumCover());
        albumPhotoVo.setPhotoAlbumAuthor(albumVo.getUserNickname());
        albumPhotoVo.setList(photoVos);

        return albumPhotoVo;
    }

    /**
     * 查询前台专栏列表
     *
     * @param current 页码
     * @return ColumnList
     */
    @Override
    public com.github.pagehelper.PageInfo<ArticleColumnVo> queryColumnList(Integer current) {
        int page = current == null ? 1 : current;
        PageHelper.startPage(page, 8);
        List<ArticleColumnVo> articleVos = articleColumnMapper.selectWebColumnList();
        return new com.github.pagehelper.PageInfo<>(articleVos);
    }

    /**
     * 查询前台专栏详情
     *
     * @param columnId 专栏id
     * @return ColumnVo
     */
    @Override
    public ArticleColumnVo queryColumnById(Long columnId) {
        return articleColumnMapper.selectColumnDetail(columnId);
    }

    /**
     * 分页查询前台专栏文章列表
     *
     * @param queryDto 查询参数
     * @return ArticleList
     */
    @Override
    public com.github.pagehelper.PageInfo<ArticleVo> queryColumnArticleList(ArticleQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new com.github.pagehelper.PageInfo<>(articleColumnRelationMapper.selectWebArticleList(queryDto.getColumnId()));
    }

    /**
     * 查询前台版本列表
     *
     * @return VersionList
     */
    @Override
    public List<Version> queryVersionList() {
        return versionMapper.selectVersionList(null);
    }

    /**
     * 查询前台说说详情
     *
     * @param talkId 查询详情
     * @return TalkVo
     */
    @Override
    public TalkVo queryTalkById(Long talkId) {
        Assert.notNull(talkId, "参数错误");
        TalkVo talkVo = talkMapper.selectWebTalkDetail(talkId);
        Assert.notNull(talkVo, "说说信息不存在");
        return talkVo;
    }

    /**
     * 查询前台标签列表
     *
     * @return TagList
     */
    @Override
    public List<ArticleTagVo> queryTagList() {
        return articleTagMapper.selectWebTagList();
    }

    /**
     * 查询前台分类列表
     *
     * @return CategoryList
     */
    @Override
    public List<ArticleCategoryVo> queryCategoryList() {
        return articleCategoryMapper.selectWebCategoryList();
    }

    /**
     * 查询前台归档列表
     *
     * @param current 页码
     * @return ArticleList
     */
    @Override
    public com.github.pagehelper.PageInfo<ArticleVo> queryArchiveList(Integer current) {
        int page = current == null ? 1 : current;
        PageHelper.startPage(page, 10);
        List<ArticleVo> articleVos = articleMapper.selectArchiveList();
        return new com.github.pagehelper.PageInfo<>(articleVos);
    }

    /**
     * 查询前台分类详情
     *
     * @param categoryId 查询详情
     * @return CategoryVo
     */
    @Override
    public ArticleCategoryVo queryCategoryById(Long categoryId) {
        Assert.notNull(categoryId, "参数错误");
        ArticleCategoryVo categoryVo = articleCategoryMapper.selectCategoryDetail(categoryId);
        Assert.notNull(categoryVo, "类别信息不存在");
        return categoryVo;
    }

    /**
     * 查询前台标签详情
     *
     * @param tagId 查询详情
     * @return TagVo
     */
    @Override
    public ArticleTagVo queryTagById(Long tagId) {
        Assert.notNull(tagId, "参数错误");
        ArticleTagVo tagVo = articleTagMapper.selectTagDetail(tagId);
        Assert.notNull(tagVo, "标签信息不存在");
        return tagVo;
    }

    /**
     * QQ 登录
     *
     * @param qqLoginParam qq 登录信息
     * @param request      请求信息
     * @return UserVo 用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVo qqLogin(QqLoginParam qqLoginParam, HttpServletRequest request) {
        Assert.notBlank(qqLoginParam.getAccessToken(), "登录失败，access_token 不能为空");

        // 获取 openId
        String openId = getQqOpenId(qqLoginParam.getAccessToken());
        Assert.notBlank(openId, "登录失败，获取用户信息异常");

        // 判断用户是否存在
        Long userId = portalMapper.selectUserIdByQqOpenId(openId);
        if (userId == null) {
            // 第一次登录，获取用户基本信息
            JSONObject userInfo = getQqUserInfo(qqLoginParam.getAccessToken(), openId);
            User newUser = new User();
            newUser.setId(SequenceUtils.getId());
            newUser.setNickname(userInfo.getString("nickname"));
            newUser.setUsername(String.valueOf(newUser.getId()));
            newUser.setPassword(passwordEncoder.encode(UuidUtils.gen8BitUuidStr()));
            newUser.setGender(Gender.HIDE.getType());
            newUser.setHeadImgUrl(userInfo.getString("figureurl_qq_2"));
            newUser.setLoginTime(DateUtils.formatNow());
            newUser.setChangePwd(false);
            newUser.setLocked(false);
            newUser.setTemp(false);
            newUser.setAdmin(false);
            newUser.setEnabled(true);
            userMapper.insert(newUser);

            // 插入用户与 QQ 的关联关系
            portalMapper.insertUserOauthQqRelation(newUser.getId(), openId);
            return parseTokenUser(newUser, request);
        } else {
            // 用户已存在，查询用户信息
            User user = userMapper.selectById(userId);
            user.setLoginTime(DateUtils.formatNow());
            userMapper.updateById(user);
            return parseTokenUser(user, request);
        }
    }

    /**
     * 查询用户各种明细
     *
     * @param userId 用户id
     * @return UserVo 用户信息
     */
    @Override
    public UserVo queryUserDetail(Long userId) {
        if (userId != null) {
            User user = userMapper.selectById(userId);
            UserVo userVo = userConverter.entityToVo(user);
            userVo.setCommentLikeSet(portalMapper.selectCommentLikeSet(userVo.getId()));
            userVo.setTalkLikeSet(portalMapper.selectTalkLikeSet(userVo.getId()));
            userVo.setArticleLikeSet(portalMapper.selectArticleLikeSet(userVo.getId()));
            userVo.setUserToken(jwtTokenStorage.getAccessToken(userId));
            return userVo;
        }

        return null;
    }

    private JSONObject getQqUserInfo(String accessToken, String openId) {
        String websiteConfig = getWebInfoFromCache();
        WebInfo webInfo = StringUtils.isBlank(websiteConfig) ? new WebInfo() : JSON.parseObject(websiteConfig, WebInfo.class);

        Map<String, Object> param = new HashMap<>(CollectionUtils.mapSize(3));
        param.put("access_token", accessToken);
        param.put("oauth_consumer_key", webInfo.getQqAppId());
        param.put("openid", openId);
        String result = HttpUtils.get("https://graph.qq.com/user/get_user_info", param, null);
        return JSON.parseObject(result);
    }

    private String getQqOpenId(String accessToken) {
        Map<String, Object> param = new HashMap<>(CollectionUtils.mapSize(2));
        param.put("access_token", accessToken);
        param.put("fmt", "json");
        String result = HttpUtils.get("https://graph.qq.com/oauth2.0/me", param, null);
        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject.getString("openid");
    }

    private UserVo parseTokenUser(User user, HttpServletRequest request) {
        UserVo userVo = userConverter.entityToVo(user);

        // 查询用户点赞列表
        userVo.setCommentLikeSet(portalMapper.selectCommentLikeSet(userVo.getId()));
        userVo.setTalkLikeSet(portalMapper.selectTalkLikeSet(userVo.getId()));
        userVo.setArticleLikeSet(portalMapper.selectArticleLikeSet(userVo.getId()));

        // 封装 Token
        int tokenVersion = 1;
        String token = jwtTokenGenerator.generateToken(request, user, tokenVersion, null);
        UserTokenStorage tokenStorage = UserTokenStorage.builder().userId(userVo.getId()).lastedToken(token).version(tokenVersion).build();
        userService.storageUserToken(tokenStorage, 1);
        Constants.SysCacheKey ssoTknKey = Constants.SysCacheKey.ssoTkn;
        String ssoTknCacheKey = String.format(ssoTknKey.cacheKey(), IpUtils.getIp(request), Locale.ROOT);
        GlobalCache.put(ssoTknCacheKey, token, ssoTknKey.duration());

        userVo.setUserToken(Constants.AUTHENTICATION_PREFIX.concat(token));
        return userVo;
    }

    private String getWebInfoFromCache() {
        YwConstants.CacheKey cacheKey = YwConstants.CacheKey.webInfo;
        String cacheConfig = GlobalCache.get(cacheKey.cacheKey());
        if (cacheConfig == null) {
            // 查询数据库
            WebsiteConfig websiteConfig = webSettingMapper.selectById(WebsiteConfigType.WEB_INFO.value());
            if (websiteConfig == null) {
                return null;
            }

            // 返回数据
            GlobalCache.put(cacheKey.cacheKey(), websiteConfig.getConfig(), cacheKey.duration());
            return websiteConfig.getConfig();
        }

        return cacheConfig;
    }

    private String getSocialInfoFromCache() {
        YwConstants.CacheKey cacheKey = YwConstants.CacheKey.socialInfo;
        String cacheConfig = GlobalCache.get(cacheKey.cacheKey());
        if (cacheConfig == null) {
            // 查询数据库
            WebsiteConfig websiteConfig = webSettingMapper.selectById(WebsiteConfigType.SOCIAL_INFO.value());
            if (websiteConfig == null) {
                return null;
            }

            // 返回数据
            GlobalCache.put(cacheKey.cacheKey(), websiteConfig.getConfig(), cacheKey.duration());
            return websiteConfig.getConfig();
        }

        return cacheConfig;
    }

    private String getOtherInfoFromCache() {
        YwConstants.CacheKey cacheKey = YwConstants.CacheKey.otherInfo;
        String cacheConfig = GlobalCache.get(cacheKey.cacheKey());
        if (cacheConfig == null) {
            // 查询数据库
            WebsiteConfig websiteConfig = webSettingMapper.selectById(WebsiteConfigType.OTHER_INFO.value());
            if (websiteConfig == null) {
                return null;
            }

            // 返回数据
            GlobalCache.put(cacheKey.cacheKey(), websiteConfig.getConfig(), cacheKey.duration());
            return websiteConfig.getConfig();
        }

        return cacheConfig;
    }

    private void notice(Comment comment) {
        throw new ServiceException("暂不支持");
    }

    private synchronized void updateVisitorAreaCount(String area) {
        String currentTime = DateUtils.formatNow();
        if (visitorAreaMapper.judgeAreaExist(area)) {
            visitorAreaMapper.updateAreaCount(area, currentTime);
        } else {
            visitorAreaMapper.insertAreaCount(area, currentTime);
        }
    }
}
