package com.momo.momopjt.news;

import java.util.List;

public interface NewsService {

  void createNews(NewsDTO newsDTO);
  News readNews(Long newsNo);
  List<News> readAllNews();
  void updateNews(NewsDTO newsDTO);
  void deleteNews(Long newsNo);

}
