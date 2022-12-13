package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 说说查询
 *
 * @author SunTao
 * @since 2022-12-12
 */
@Getter
@Setter
@ToString
public class TalkQueryDto extends PageParam {
    /**
     * 当前登录用户
     */
    private String loginUser;

    /**
     * 说说状态
     */
    private Integer status;
}
