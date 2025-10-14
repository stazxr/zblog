package com.github.stazxr.zblog.core.base;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据转换器，实现数据对象转实体对象，实体对象转视图对象的方法。
 *
 * <p>基础用法：<pre>{@code
 * @Mapper(componentModel = "spring")
 * public interface XxxConverter extends BaseConverter<Xxx, XxxDto, XxxVo> {
 *     // 需要实现父接口方法
 *     // 因为 Java 的泛型类型在编译期被“擦除”，MapStruct 无法推断泛型类型实际指代哪个类型
 * }
 * }</pre></p>
 *
 * <p>2025年08月01日，由于 MapStruct 的泛型支持不友好，修改为使用 BeanUtils </p>
 *
 * @param <P> po   BaseEntity 实体对象
 * @param <D> dto  BaseDto    数据对象
 * @param <V> vo   BaseVo     视图对象
 * @author SunTao
 * @since 2022-06-29
 */
public interface BaseConverter<P extends BaseEntity, D extends BaseDto, V extends BaseVo> {
    /**
     * 数据对象转实体对象
     *
     * @param dto 数据对象
     * @return po 实体对象
     */
    default P dtoToEntity(D dto) {
        if (dto == null) {
            return null;
        }

        try {
            // 创建目标实例
            P entity = getEntityClass().getDeclaredConstructor().newInstance();
            // 属性复制
            BeanUtils.copyProperties(dto, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("对象复制失败", e);
        }
    }

    /**
     * 数据对象列表转实体对象列表
     *
     * @param dtoList 数据对象列表
     * @return poList 实体对象列表
     */
    default List<P> dtoToEntity(List<D> dtoList) {
        return dtoList == null ? null : dtoList.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }

    /**
     * 实体对象转视图对象
     *
     * @param po  实体对象
     * @return vo 视图对象
     */
    default V entityToVo(P po) {
        if (po == null) {
            return null;
        }

        try {
            // 创建目标实例
            V vo = getVoClass().getDeclaredConstructor().newInstance();
            // 属性复制
            BeanUtils.copyProperties(po, vo);
            return vo;
        } catch (Exception e) {
            throw new RuntimeException("对象复制失败", e);
        }
    }


    /**
     * 实体对象列表转视图对象列表
     *
     * @param poList  实体对象列表
     * @return voList 视图对象列表
     */
    default List<V> entityToVo(List<P> poList) {
        return poList == null ? null : poList.stream().map(this::entityToVo).collect(Collectors.toList());
    }

    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    Class<P> getEntityClass();

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    Class<V> getVoClass();

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    Class<D> getDtoClass();
}
