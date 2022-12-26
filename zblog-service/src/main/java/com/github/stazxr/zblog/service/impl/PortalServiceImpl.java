package com.github.stazxr.zblog.service.impl;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.stazxr.zblog.core.util.IpImplUtils;
import com.github.stazxr.zblog.domain.bo.PageInfo;
import com.github.stazxr.zblog.domain.dto.query.ArticleQueryDto;
import com.github.stazxr.zblog.domain.dto.setting.OtherInfo;
import com.github.stazxr.zblog.domain.dto.setting.SocialInfo;
import com.github.stazxr.zblog.domain.dto.setting.WebInfo;
import com.github.stazxr.zblog.domain.entity.Visitor;
import com.github.stazxr.zblog.domain.entity.WebsiteConfig;
import com.github.stazxr.zblog.domain.enums.WebsiteConfigType;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import com.github.stazxr.zblog.domain.vo.BlogWebVo;
import com.github.stazxr.zblog.domain.vo.TalkVo;
import com.github.stazxr.zblog.mapper.*;
import com.github.stazxr.zblog.service.PortalService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 前台服务实现层
 *
 * @author SunTao
 * @since 2022-11-25
 */
@Service
@RequiredArgsConstructor
public class PortalServiceImpl implements PortalService {
    private static final String UNKNOWN_AREA = "外星人";

    private static final String LOCAL_AREA_NET = "局域网";

    private final PortalMapper portalMapper;

    private final VisitorMapper visitorMapper;

    private final VisitorAreaMapper visitorAreaMapper;

    private final WebSettingMapper webSettingMapper;

    private final PageMapper pageMapper;

    private final TalkMapper talkMapper;

    private final ArticleMapper articleMapper;

    /**
     * 查询博客前台信息
     *
     * @return BlogWebVo
     */
    @Override
    public BlogWebVo queryBlogWebInfo() {
        BlogWebVo webVo = portalMapper.selectBlogWebInfo();

        // WebInfo
        WebsiteConfig websiteConfig = webSettingMapper.selectById(WebsiteConfigType.WEB_INFO.value());
        webVo.setWebInfo(websiteConfig == null ? new WebInfo() : JSON.parseObject(websiteConfig.getConfig(), WebInfo.class));

        // SocialInfo
        websiteConfig = webSettingMapper.selectById(WebsiteConfigType.SOCIAL_INFO.value());
        webVo.setSocialInfo(websiteConfig == null ? new SocialInfo() : JSON.parseObject(websiteConfig.getConfig(), SocialInfo.class));

        // OtherInfo
        websiteConfig = webSettingMapper.selectById(WebsiteConfigType.OTHER_INFO.value());
        webVo.setOtherInfo(websiteConfig == null ? new OtherInfo() : JSON.parseObject(websiteConfig.getConfig(), OtherInfo.class));

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
            JSONObject cityInfo = IpImplUtils.getHttpCityDetailInfo(ipAddress);
            String province = cityInfo.get("addr", String.class);
            if (StringUtils.isBlank(province) || LOCAL_AREA_NET.equals(province.trim())) {
                updateVisitorAreaCount(UNKNOWN_AREA);
            } else {
                province = province.trim().substring(0, 2);
                visitor.setProvince(province);
                visitor.setAreaCode(cityInfo.get("cityCode", String.class));
                updateVisitorAreaCount(province);
            }

            // 保存访客信息
            visitorMapper.insert(visitor);
        }

        // 网站访问量自增
        visitorMapper.addWebVisitorCount();
    }

    /**
     * 查询首页轮播的说说列表
     *
     * @return TalkList
     */
    @Override
    public List<TalkVo> queryTalkList() {
        return talkMapper.selectWebTalkList();
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

    private synchronized void updateVisitorAreaCount(String area) {
        String currentTime = DateUtils.formatNow();
        if (visitorAreaMapper.judgeAreaExist(area)) {
            visitorAreaMapper.updateAreaCount(area, currentTime);
        } else {
            visitorAreaMapper.insertAreaCount(area, currentTime);
        }
    }
}
