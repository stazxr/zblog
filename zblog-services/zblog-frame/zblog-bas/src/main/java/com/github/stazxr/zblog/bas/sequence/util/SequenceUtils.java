package com.github.stazxr.zblog.bas.sequence.util;

import com.github.stazxr.zblog.bas.context.util.SpringContextUtil;
import com.github.stazxr.zblog.bas.sequence.core.id.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * ID生成工具类
 *
 * @author SunTao
 * @since 2021-12-17
 */
public class SequenceUtils {
    private static IdGenerator idGenerator;

    private static IdGenerator instance() {
        if (SequenceUtils.idGenerator == null) {
            SequenceUtils.idGenerator = SpringContextUtil.getBean("idGeneratorService", IdGenerator.class);
        }
        return SequenceUtils.idGenerator;
    }

    /**
     * 生成唯一ID
     *
     * @return Long id
     */
    public static Long getId() {
        return Long.valueOf(instance().generateId());
    }

    /**
     * 生成唯一ID列表
     *
     * @param count 生成ID的个数
     * @return List<Long> id列表
     */
    public static List<Long> getIdList(Integer count) {
        List<Long> ids = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ids.add(getId());
        }
        return ids;
    }
}
