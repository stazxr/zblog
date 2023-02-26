package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.entity.Visitor;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 日期数据是否存在
     *
     * @param date 日期
     * @return boolean
     */
    boolean existsDate(@Param("date") String date);

    /**
     * 新增日期访问量
     *
     * @param id   ID
     * @param date 日期
     */
    void insertWebVisitorTodayCount(@Param("date") String date);

    /**
     * 更新日期访问量
     *
     * @param date 日期
     */
    void addWebVisitorTodayCount(@Param("date") String date);
}
