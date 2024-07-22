package com.momo.momopjt.article;

import com.momo.momopjt.club.ClubDTO;
import com.momo.momopjt.userandclub.UserAndClubDTO;

import java.util.List;

public interface ArticleService {

  Article createArticle(ArticleDTO articleDTO);
  ArticleDTO getArticleById(Long articleNo);
  List<ArticleDTO> getAllArticles();
  ArticleDTO updateArticle(Long articleNo, ArticleDTO articleDTO);
  void deleteArticle(Long articleNo);

}