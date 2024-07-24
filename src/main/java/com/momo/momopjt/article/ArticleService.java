package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;

import java.util.List;

public interface ArticleService {

  Long createArticle(ArticleDTO articleDTO);
  ArticleDTO getArticleById(Long articleNo);
  //모든 게시글을 반환하는 메서드
  //시스템에 저장된 모든 게시글을 필요로 할 때 사용
  // 클럽 번호에 관계 없이 모든 게시글을 가져올 수 있음
  List<ArticleDTO> getAllArticles(Club clubNo);
  ArticleDTO updateArticle(Long articleNo, ArticleDTO articleDTO);
  void deleteArticle(Long articleNo);

}