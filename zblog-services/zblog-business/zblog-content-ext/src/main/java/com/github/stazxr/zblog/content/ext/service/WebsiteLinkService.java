package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.content.ext.domain.dto.WebsiteLinkConfigDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.WebsiteLinkQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.WebsiteLinkConfig;
import com.github.stazxr.zblog.content.ext.domain.vo.WebsiteLinkConfigVo;

/**
 * 网站链接管理业务层
 *
 * @author SunTao
 * @since 2026-08-23
 */
public interface WebsiteLinkService extends IService<WebsiteLinkConfig> {
    /**
     * 分页查询网站链接列表
     *
     * @param queryDto 查询参数
     * @return IPage<WebsiteLinkConfigVo>
     */
    IPage<WebsiteLinkConfigVo> queryWebsiteLinkListByPage(WebsiteLinkQueryDto queryDto);

    /**
     * 查询网站链接详情
     *
     * @param websiteLinkId 网站链接id
     * @return WebsiteLinkConfigVo
     */
    WebsiteLinkConfigVo queryWebsiteLinkDetail(Long websiteLinkId);

    /**
     * 新增网站链接
     *
     * @param websiteLinkConfigDto 网站链接信息
     */
    void addWebsiteLink(WebsiteLinkConfigDto websiteLinkConfigDto);

    /**
     * 编辑网站链接
     *
     * @param websiteLinkConfigDto 网站链接信息
     */
    void editWebsiteLink(WebsiteLinkConfigDto websiteLinkConfigDto);

    /**
     * 删除网站链接
     *
     * @param websiteLinkId 网站链接id
     */
    void deleteWebsiteLink(Long websiteLinkId);
}
