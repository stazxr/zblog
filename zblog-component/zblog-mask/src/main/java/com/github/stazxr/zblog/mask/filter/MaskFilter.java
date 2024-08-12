package com.github.stazxr.muses.utils.mask.filter;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.github.stazxr.muses.utils.mask.MaskUtil;
import com.github.stazxr.muses.utils.mask.core.FieldMask;
import com.github.stazxr.zblog.util.collection.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * JSON desensitization filter implementation.
 * <p>
 * This class implements the {@link com.alibaba.fastjson.serializer.ValueFilter} interface
 * to desensitize sensitive data in JSON.
 * </p>
 *
 * @author SunTao
 * @since 2024-05-16
 */
public class MaskFilter implements ValueFilter {
    /**
     * Processes field values in JSON.
     *
     * @param obj   Object
     * @param name  Field name
     * @param value Field value
     * @return Processed field value
     */
    @Override
    public Object process(Object obj, String name, Object value) {
        Field field = null;
        Class<?> objClazz = obj.getClass();
        while (!Object.class.getName().equalsIgnoreCase(objClazz.getName())) {
            try {
                field = objClazz.getDeclaredField(name);
                break;
            } catch (NoSuchFieldException e) {
                objClazz = objClazz.getSuperclass();
            }
        }

        if (field != null && field.getName().equals(name)) {
            try {
                return maskValue(field, value);
            } catch (Exception e) {
                return value;
            }
        }

        return value;
    }

    /**
     * Masks the field value.
     *
     * @param field Field
     * @param value Field value
     * @return Masked field value
     */
    private Object maskValue(Field field, Object value) {
        if (field.isAnnotationPresent(FieldMask.class)) {
            FieldMask fieldMask = field.getAnnotation(FieldMask.class);
            if (value != null && fieldMask != null) {
                return doMaskValue(fieldMask, value);
            }
        }

        return value;
    }

    /**
     * Performs desensitization operation on field value.
     *
     * @param fieldMask Field desensitization annotation
     * @param value     Field value
     * @return Desensitized field value
     */
    protected Object doMaskValue(FieldMask fieldMask, Object value) {
        if (value instanceof String) {
            return MaskUtil.desensitized(String.valueOf(value), fieldMask.type());
        }
        if (value instanceof Collection) {
            List<Object> maskList = new ArrayList<>();
            ((Collection<?>) value).forEach(k -> {
                if (k instanceof String) {
                    maskList.add(MaskUtil.desensitized(String.valueOf(k), fieldMask.type()));
                } else {
                    maskList.add(doMaskValue(fieldMask, k));
                }
            });
            return maskList;
        }
        if (value instanceof Map) {
            Map<?, ?> newMap = (Map<?, ?>) value;
            Map<Object, Object> maskMap = new HashMap<>(CollectionUtils.mapSize(newMap.size()));
            newMap.forEach((k, v) -> {
                if (v instanceof String) {
                    maskMap.put(k, MaskUtil.desensitized((String) v, fieldMask.type()));
                } else {
                    maskMap.put(k, doMaskValue(fieldMask, v));
                }
            });
            return maskMap;
        }

        return value;
    }
}
