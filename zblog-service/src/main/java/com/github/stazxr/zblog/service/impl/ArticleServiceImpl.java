package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.domain.entity.Article;
import com.github.stazxr.zblog.mapper.ArticleMapper;
import com.github.stazxr.zblog.service.ArticleService;
import org.springframework.stereotype.Service;

/**
 * 文章业务实现层
 *
 * @author SunTao
 * @since 2021-02-23
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
