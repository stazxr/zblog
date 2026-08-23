package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.ext.domain.dto.query.WebsiteLinkQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.WebsiteLinkConfig;
import com.github.stazxr.zblog.content.ext.domain.vo.WebsiteLinkConfigVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 网站链接配置数据层
 *
 * @author SunTao
 * @since 2026-08-20
 */
public interface WebsiteLinkConfigMapper extends BaseMapper<WebsiteLinkConfig> {
    /**
     * 分页查询网站链接列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<WebsiteLinkConfigVo>
     */
    IPage<WebsiteLinkConfigVo> selectWebsiteLinkList(@Param("page") Page<WebsiteLinkConfigVo> page, @Param("query") WebsiteLinkQueryDto queryDto);

    /**
     * 查询网站链接详情
     *
     * @param websiteLinkId 网站链接id
     * @return WebsiteLinkConfigVo
     */
    WebsiteLinkConfigVo selectWebsiteLinkDetail(@Param("websiteLinkId") Long websiteLinkId);

    /**
     * 查询前端网站链接列表（筛选启用且配置了链接地址的）
     *
     * @return List<WebsiteLinkConfigVo>
     */
    List<WebsiteLinkConfigVo> selectPortalWebsiteLinks();
}
