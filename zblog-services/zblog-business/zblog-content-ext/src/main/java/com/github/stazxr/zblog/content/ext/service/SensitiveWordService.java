package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.content.ext.domain.dto.SensitiveWordDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.SensitiveWordQueryDto;
import com.github.stazxr.zblog.content.ext.domain.vo.SensitiveWordVo;

/**
 * 敏感词管理业务层
 *
 * @author SunTao
 * @since 2026-07-23
 */
public interface SensitiveWordService {
    /**
     * 分页查询敏感词列表
     *
     * @param queryDto 查询参数
     * @return IPage<SensitiveWordVo>
     */
    IPage<SensitiveWordVo> querySensitiveWordListByPage(SensitiveWordQueryDto queryDto);

    /**
     * 新增敏感词
     *
     * @param sensitiveWordDto 敏感词信息
     */
    void addSensitiveWord(SensitiveWordDto sensitiveWordDto);

    /**
     * 编辑敏感词
     *
     * @param sensitiveWordDto 敏感词信息
     */
    void editSensitiveWord(SensitiveWordDto sensitiveWordDto);

    /**
     * 删除敏感词
     *
     * @param sensitiveWordId 敏感词id
     */
    void deleteSensitiveWord(Long sensitiveWordId);
}
