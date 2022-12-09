package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.entity.Visitor;

/**
 * 访客数据持久层
 *
 * @author SunTao
 * @since 2022-12-09
 */
public interface VisitorMapper extends BaseMapper<Visitor> {
    /**
     * 增加网站访问量
     */
    void addWebVisitorCount();
}
