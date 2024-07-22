package com.momo.momopjt.article;

import java.util.List;

public interface ArticleService {

  Article createArticle(ArticleDTO articleDTO);
  ArticleDTO getArticleById(Long articleNo);
  List<ArticleDTO> getAllArticles();
  ArticleDTO updateArticle(Long articleNo, ArticleDTO articleDTO);
  void deleteArticle(Long articleNo);

}