package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 照片查询
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Getter
@Setter
@ToString
public class AlbumPhotoQueryDto extends PageParam {
    /**
     * 相册ID
     */
    private Long albumId;
}
