package com.github.stazxr.zblog.content.converter;

import com.github.stazxr.zblog.content.domain.dto.CategoryDto;
import com.github.stazxr.zblog.content.domain.entity.Category;
import com.github.stazxr.zblog.content.domain.vo.CategoryVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.springframework.stereotype.Component;

/**
 * CategoryConverter
 *
 * @author SunTao
 * @since 2022-09-21
 */
@Component
public class CategoryConverter implements BaseConverter<Category, CategoryDto, CategoryVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Category> getEntityClass() {
        return Category.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<CategoryVo> getVoClass() {
        return CategoryVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<CategoryDto> getDtoClass() {
        return CategoryDto.class;
    }
}
