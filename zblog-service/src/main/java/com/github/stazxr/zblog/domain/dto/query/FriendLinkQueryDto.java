package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 友链查询
 *
 * @author SunTao
 * @since 2022-12-07
 */
@Getter
@Setter
@ToString
public class FriendLinkQueryDto extends PageParam {
    /**
     * 链接名称
     */
    private String name;

    /**
     * 链接状态
     */
    private Boolean valid;
}
