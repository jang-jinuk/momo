package com.momo.momopjt.article;

import org.springframework.stereotype.Service;

import java.util.List;

public interface ArticleService {
  List<ArticleDTO> getAllArticles();
  //모든 게시글을 반환하는 메서드
  //시스템에 저장된 모든 게시글을 필요로 할 때 사용
  // 클럽 번호에 관계 없이 모든 게시글을 가져올 수 있음
  ArticleDTO getArticleById(Long articleNo);
  ArticleDTO createArticle(ArticleDTO articleDTO);
  ArticleDTO updateArticle(Long articleNo, ArticleDTO articleDTO);
  void deleteArticle(Long articleNo);

}
