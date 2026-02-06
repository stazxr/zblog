package com.github.stazxr.zblog.core.util;

import com.github.stazxr.zblog.bas.mask.MaskUtil;
import com.github.stazxr.zblog.bas.mask.core.FieldMask;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 工具类，用于构建对象的字符串表示。
 *
 * <p>一个用于将对象转换为 JSON 或字符串表示形式的工具类，可对敏感字段进行掩码处理。</p>
 *
 * @author SunTao
 * @since 2024-10-31
 */
public class ToStringUtils {
    private static final Logger log = LoggerFactory.getLogger(ToStringUtils.class);

    /**
     * 将给定对象转换为字符串
     *
     * @param obj 要转换为字符串的对象
     * @return 对象的的字符串表示
     */
    public static String buildString(Object obj) {
        return buildJsonString(obj);
    }

    /**
     * 将对象转换为 JSON 格式的字符串表示，对敏感字段进行掩码处理。
     * 该方法内部使用 `MaskUtil.toMaskString` 方法来处理掩码逻辑。
     *
     * @param obj 要转换的对象
     * @return JSON 格式的字符串，包含掩码处理的敏感字段
     */
    public static String buildJsonString(Object obj) {
        return MaskUtil.toMaskString(obj);
    }

    /**
     * 构建对象的默认字符串表示形式，使用反射获取对象的所有字段并生成ToString表示。
     * 对于带有 @FieldMask 注解的字符串字段，应用字段掩码。
     *
     * @param obj 要生成字符串表示的对象
     * @return 生成的字符串表示
     */
    public static String buildDefaultString(Object obj) {
        ToStringBuilder builder = new ToStringBuilder(obj);
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object var = field.get(obj);
                FieldMask fieldMask = field.getAnnotation(FieldMask.class);
                if (var instanceof String && fieldMask != null) {
                    builder.append(field.getName(), MaskUtil.desensitized((String) var, fieldMask.type()));
                } else {
                    builder.append(field.getName(), var);
                }
            } catch (Exception e) {
                log.error("Error accessing field: {}", field.getName(), e);
                builder.append(field.getName(), "access error");
            }
        }
        return builder.toString();
    }
}
