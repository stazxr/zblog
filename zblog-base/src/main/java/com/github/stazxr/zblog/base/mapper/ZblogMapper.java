package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.bo.DateCount;
import com.github.stazxr.zblog.base.domain.bo.NameValue;
import com.github.stazxr.zblog.base.domain.vo.HomePanelDataCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部分公共查询
 *
 * @author SunTao
 * @since 2022-07-24
 */
public interface ZblogMapper {
    /**
     * 清除记住我信息
     *
     * @param username 用户名
     */
    void removeRememberMe(@Param("username") String username);

    /**
     * 查询首页统计信息
     *
     * @return HomePanelDataCountVo
     */
    HomePanelDataCountVo queryHomePanelDataCount();

    /**
     * 根据日期范围查询浏览量数据
     *
     * @param dates 日期范围
     * @return 浏览量数据信息
     */
    List<DateCount> queryPvRangeData(String[] dates);

    /**
     * 根据日期范围查询访客数数据
     *
     * @param dates 日期范围
     * @return 访客数数据信息
     */
    List<DateCount> queryUvRangeData(String[] dates);

    /**
     * 根据日期范围查询用户数数据
     *
     * @param dates 日期范围
     * @return 用户数数据信息
     */
    List<DateCount> queryUuRangeData(String[] dates);

    /**
     * 根据日期范围查询阅读量数据
     *
     * @param dates 日期范围
     * @return 阅读量数据信息
     */
    List<DateCount> queryAvRangeData(String[] dates);

    /**
     * 获取首页面板的访客地域数据
     *
     * @return NameValue
     */
    List<NameValue> queryVisitorAreaCount();
}
