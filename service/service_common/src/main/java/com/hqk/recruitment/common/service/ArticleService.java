package com.hqk.recruitment.common.service;

import com.hqk.recruitment.model.common.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.base.PageVo;
import com.hqk.recruitment.vo.common.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-03-03
 */
public interface ArticleService extends IService<Article> {
    boolean addArticle(ArticleVo articleVo);

    boolean removeArticle(Long articleId);


    R getArticleListByPage(ArticlePageVo articlePageVo);

    R searchArticleByParams(ArticleSearchVo  articleSearchVo);

    R getArticleDetailById(Long id, String authorization);

    R getArticleRelationList(Long id);


    R addWatchCount(Long id);

    List<Long> getMajorIdsInArticle();

    Map uploadArticle(MultipartFile file);

    R getArticleById(PageVo pageVo, String authorization);
}
