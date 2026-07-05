package com.github.stazxr.zblog.audit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 敏感词Mapper
 *
 * @author Sun Tao
 * @since 2026-07-05
 */
@Mapper
public interface SensitiveWordMapper {
    /**
     * 查询敏感词列表
     *
     * @return List<String>
     */
    @Select("SELECT WORD FROM sensitive_word WHERE STATUS = 1")
    List<String> selectAllWords();
}
