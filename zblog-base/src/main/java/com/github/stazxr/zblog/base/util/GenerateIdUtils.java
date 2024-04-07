package com.github.stazxr.zblog.base.util;

import com.github.stazxr.easycloud.id.core.service.IdGeneratorService;
import com.github.stazxr.zblog.core.util.SpringContextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ID生成工具类
 *
 * @author SunTao
 * @since 2021-12-17
 */
public class GenerateIdUtils {
    private static IdGeneratorService idGeneratorService;

    private static IdGeneratorService instance() {
        if (GenerateIdUtils.idGeneratorService == null) {
            GenerateIdUtils.idGeneratorService = SpringContextUtils.getBean("EasyIdGeneratorService", IdGeneratorService.class);
        }
        return GenerateIdUtils.idGeneratorService;
    }

    /**
     * 生成唯一ID
     *
     * @return Long id
     */
    public static Long getId() {
        return instance().generateId();
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
            ids.add(instance().generateId());
        }
        return ids;
    }
}
