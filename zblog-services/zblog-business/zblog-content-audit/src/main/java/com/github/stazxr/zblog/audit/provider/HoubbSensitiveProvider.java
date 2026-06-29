//package com.github.stazxr.zblog.audit.provider;
//
//import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * houbb敏感词实现
// *
// * @author SunTao
// * @since 2026-06-29
// */
//@Component
//public class HoubbSensitiveProvider implements SensitiveProvider {
//    /**
//     * 是否包含敏感词
//     *
//     * @param content 内容
//     * @return boolean
//     */
//    @Override
//    public boolean contains(String content) {
//        return SensitiveWordHelper.contains(content);
//    }
//
//    /**
//     * 替换敏感词
//     *
//     * @param content 内容
//     * @return 替换结果
//     */
//    @Override
//    public String replace(String content) {
//        return SensitiveWordHelper.replace(content);
//    }
//
//    /**
//     * 查询命中的敏感词
//     *
//     * @param content 内容
//     * @return 敏感词列表
//     */
//    @Override
//    public List<String> findAll(String content) {
//        return SensitiveWordHelper.findAll(content);
//    }
//}