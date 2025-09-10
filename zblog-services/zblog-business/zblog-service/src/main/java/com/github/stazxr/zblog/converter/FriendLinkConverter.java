package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.FriendLinkDto;
import com.github.stazxr.zblog.domain.entity.FriendLink;
import com.github.stazxr.zblog.domain.vo.FriendLinkVo;
import org.springframework.stereotype.Component;

/**
 * FriendLinkConverter
 *
 * @author SunTao
 * @since 2022-12-07
 */
@Component
public class FriendLinkConverter implements BaseConverter<FriendLink, FriendLinkDto, FriendLinkVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<FriendLink> getEntityClass() {
        return FriendLink.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<FriendLinkVo> getVoClass() {
        return FriendLinkVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<FriendLinkDto> getDtoClass() {
        return FriendLinkDto.class;
    }
}
