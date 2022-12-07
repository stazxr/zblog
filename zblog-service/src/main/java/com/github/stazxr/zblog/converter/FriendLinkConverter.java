package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.FriendLinkDto;
import com.github.stazxr.zblog.domain.entity.FriendLink;
import com.github.stazxr.zblog.domain.vo.FriendLinkVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * FriendLinkConverter
 *
 * @author SunTao
 * @since 2022-12-07
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FriendLinkConverter extends BaseConverter<FriendLink, FriendLinkDto, FriendLinkVo> {

}
