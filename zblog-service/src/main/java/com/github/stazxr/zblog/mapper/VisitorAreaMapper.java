package com.github.stazxr.zblog.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 访客分布数据持久层
 *
 * @author SunTao
 * @since 2022-12-09
 */
public interface VisitorAreaMapper {
    /**
     * 判断地域信息是否已存在
     *
     * @param area 地域名称
     * @return boolean
     */
    boolean judgeAreaExist(@Param("area") String area);

    /**
     * 新增地域信息
     *
     * @param area 地域名称
     * @param createTime 创建时间
     */
    void insertAreaCount(@Param("area") String area, @Param("createTime") String createTime);

    /**
     * 修改地域信息
     *
     * @param area 地域名称
     * @param updateTime 修改时间
     */
    void updateAreaCount(@Param("area") String area, @Param("updateTime") String updateTime);
}
