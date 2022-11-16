package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 节点查询
 *
 * @author SunTao
 * @since 2022-11-09
 */
@Getter
@Setter
@ToString
public class NodeQueryDto extends PageParam {
    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点 IP
     */
    private String ip;
}
