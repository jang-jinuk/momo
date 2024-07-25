package com.momo.momopjt.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleSearch {
  Page<ArticleListRepluCountDTO> searchWithAll(String[] types,
                                               String keyword,
                                               Pageable pageable);
}
