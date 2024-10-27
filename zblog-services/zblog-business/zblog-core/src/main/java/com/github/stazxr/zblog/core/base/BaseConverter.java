package com.github.stazxr.zblog.core.base;

import java.util.List;

/**
 * BaseConverter，继承该类实现默认的 VO, DTO, PO之间的转换
 *
 *  eg：
 * // @Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
 * // public interface PermissionConverter extends BaseConverter<Permission, PermissionDto, MenuVo> {
 * // }
 *
 * 备注：如果新增的Converter在某些属性在转换时失败，会导致编译错误，项目启动失败，需排查处理
 *
 * @param <P> po PO
 * @param <D> dto DTO
 * @param <V> vo VO
 * @author SunTao
 * @since 2022-06-29
 */
public interface BaseConverter<P, D, V> {
    /**
     * dto to po
     *
     * @param dto dto
     * @return po
     */
    P dtoToEntity(D dto);

    /**
     * dto list to po list
     *
     * @param dtoList dto list
     * @return po list
     */
    List<P> dtoToEntity(List<D> dtoList);

    /**
     * po to dto
     *
     * @param po po
     * @return dto
     */
    D entityToDto(P po);

    /**
     * po list to dto list
     *
     * @param poList po list
     * @return dto list
     */
    List<D> entityToDto(List<P> poList);

    /**
     * po to vo
     *
     * @param entity po
     * @return vo
     */
    V entityToVo(P entity);

    /**
     * po list to vo list
     *
     * @param entity po list
     * @return vo list
     */
    List<V> entityToVo(List<P> entity);
}
