package com.momo.momopjt.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleSearch {
  //TODO 수정 필요
  Page<ArticleListRepluCountDTO> searchWithAll(String[] types,
                                               String keyword,
                                               Pageable pageable);
}
