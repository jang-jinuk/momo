package com.momo.momopjt.news;

import java.util.List;

public interface NewsService {

  void createNews(News news);
  News readNews(Long newsNo);
  List<News> readAllNews();
  void updateNews(News news);
  void deleteNews(Long newsNo);

}
