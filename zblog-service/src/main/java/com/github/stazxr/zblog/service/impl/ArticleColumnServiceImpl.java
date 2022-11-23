package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.domain.entity.ArticleColumn;
import com.github.stazxr.zblog.mapper.ArticleColumnMapper;
import com.github.stazxr.zblog.service.ArticleColumnService;
import org.springframework.stereotype.Service;

/**
 * 文章栏目业务实现层
 *
 * @author SunTao
 * @since 2022-11-22
 */
@Service
public class ArticleColumnServiceImpl extends ServiceImpl<ArticleColumnMapper, ArticleColumn> implements ArticleColumnService {

}
