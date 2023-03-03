package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 相册查询
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Getter
@Setter
@ToString
public class AlbumQueryDto extends PageParam {
    /**
     * 当前登录用户
     */
    private String loginUser;

    /**
     * 当前登录用户Id
     */
    private String loginUserId;

    /**
     * 相册名称
     */
    private String albumName;

    /**
     * 相册状态
     */
    private Integer status;
}
