package com.github.stazxr.zblog.content.ext.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.content.ext.converter.WebsiteLinkConverter;
import com.github.stazxr.zblog.content.ext.domain.dto.WebsiteLinkConfigDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.WebsiteLinkQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.WebsiteLinkConfig;
import com.github.stazxr.zblog.content.ext.domain.error.WebsiteLinkErrorCode;
import com.github.stazxr.zblog.content.ext.domain.vo.WebsiteLinkConfigVo;
import com.github.stazxr.zblog.content.ext.mapper.WebsiteLinkConfigMapper;
import com.github.stazxr.zblog.content.ext.service.WebsiteLinkService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 网站链接管理业务实现层
 *
 * @author SunTao
 * @since 2026-08-23
 */
@Service
@RequiredArgsConstructor
public class WebsiteLinkServiceImpl extends ServiceImpl<WebsiteLinkConfigMapper, WebsiteLinkConfig> implements WebsiteLinkService {
    private final WebsiteLinkConverter websiteLinkConverter;

    /**
     * 分页查询网站链接列表
     *
     * @param queryDto 查询参数
     * @return IPage<WebsiteLinkConfigVo>
     */
    @Override
    public IPage<WebsiteLinkConfigVo> queryWebsiteLinkListByPage(WebsiteLinkQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getLinkName())) {
            queryDto.setLinkName(queryDto.getLinkName().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getLinkType())) {
            queryDto.setLinkType(queryDto.getLinkType().trim());
        }

        // 分页查询
        Page<WebsiteLinkConfigVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return baseMapper.selectWebsiteLinkList(page, queryDto);
    }

    /**
     * 查询网站链接详情
     *
     * @param websiteLinkId 网站链接id
     * @return WebsiteLinkConfigVo
     */
    @Override
    public WebsiteLinkConfigVo queryWebsiteLinkDetail(Long websiteLinkId) {
        WebsiteLinkConfigVo websiteLinkConfigVo = baseMapper.selectWebsiteLinkDetail(websiteLinkId);
        return ThrowUtils.requireNonNull(websiteLinkConfigVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 新增网站链接
     *
     * @param websiteLinkConfigDto 网站链接信息
     */
    @Override
    public void addWebsiteLink(WebsiteLinkConfigDto websiteLinkConfigDto) {
        // 获取网站链接信息
        WebsiteLinkConfig websiteLinkConfig = websiteLinkConverter.dtoToEntity(websiteLinkConfigDto);
        websiteLinkConfig.setCreateTime(LocalDateTime.now());
        // 新增时，不允许传入 websiteLinkId
        ThrowUtils.when(websiteLinkConfig.getId() != null).system(BaseErrorCode.SCOREB001);
        // 网站链接信息检查
        checkWebsiteLink(websiteLinkConfig);
        // 新增网站链接
        ThrowUtils.when(!save(websiteLinkConfig)).system(BaseErrorCode.SCOREA001);
    }

    /**
     * 编辑网站链接
     *
     * @param websiteLinkConfigDto 网站链接信息
     */
    @Override
    public void editWebsiteLink(WebsiteLinkConfigDto websiteLinkConfigDto) {
        // 获取网站链接信息
        WebsiteLinkConfig websiteLinkConfig = websiteLinkConverter.dtoToEntity(websiteLinkConfigDto);
        websiteLinkConfig.setUpdateTime(LocalDateTime.now());
        // 判断网站链接是否存在
        WebsiteLinkConfig dbWebsiteLinkConfig = baseMapper.selectById(websiteLinkConfig.getId());
        ThrowUtils.throwIfNull(dbWebsiteLinkConfig, BaseErrorCode.ECOREA001);
        // 网站链接信息检查
        checkWebsiteLink(websiteLinkConfig);
        // 编辑网站链接
        ThrowUtils.when(!updateById(websiteLinkConfig)).system(BaseErrorCode.SCOREA002);
    }

    /**
     * 删除网站链接
     *
     * @param websiteLinkId 网站链接id
     */
    @Override
    public void deleteWebsiteLink(Long websiteLinkId) {
        // 判断网站链接是否存在
        WebsiteLinkConfig dbWebsiteLinkConfig = baseMapper.selectById(websiteLinkId);
        ThrowUtils.throwIfNull(dbWebsiteLinkConfig, BaseErrorCode.ECOREA001);
        // 删除网站链接
        ThrowUtils.when(!removeById(websiteLinkId)).system(BaseErrorCode.SCOREA003);
    }

    private void checkWebsiteLink(WebsiteLinkConfig websiteLink) {
        // 检查链接类型的唯一性
        websiteLink.setLinkType(websiteLink.getLinkType().trim());
        ThrowUtils.throwIf(checkLinkTypeExist(websiteLink), WebsiteLinkErrorCode.EWEBLA001);
    }

    private boolean checkLinkTypeExist(WebsiteLinkConfig websiteLink) {
        if (websiteLink.getLinkType() != null) {
            LambdaQueryWrapper<WebsiteLinkConfig> queryWrapper = queryBuild().eq(WebsiteLinkConfig::getLinkType, websiteLink.getLinkType());
            if (websiteLink.getId() != null) {
                queryWrapper.ne(WebsiteLinkConfig::getId, websiteLink.getId());
            }
            return baseMapper.exists(queryWrapper);
        }
        return false;
    }

    private LambdaQueryWrapper<WebsiteLinkConfig> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
