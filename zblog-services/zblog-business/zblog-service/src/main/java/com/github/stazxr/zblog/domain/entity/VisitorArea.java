package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 访客地域
 *
 * @author SunTao
 * @since 2022-12-09
 */
@Getter
@Setter
@Builder
@TableName("visitor_area")
public class VisitorArea {
    /**
     * 访问地域
     */
    private String area;

    /**
     * 访问量
     */
    private Integer areaCount;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;
}
