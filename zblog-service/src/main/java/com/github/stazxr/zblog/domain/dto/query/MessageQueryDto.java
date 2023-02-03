package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 留言查询
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Getter
@Setter
@ToString
public class MessageQueryDto extends PageParam {
    /**
     * 留言内容
     */
    private String content;

    /**
     * 留言状态
     */
    private Integer status;
}
