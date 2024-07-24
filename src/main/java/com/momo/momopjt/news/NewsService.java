package com.momo.momopjt.news;

import java.util.List;

public interface NewsService {

  //TODO 주석처리 메소드 create, update 확인 후 삭제 (Entity->DTO) 0724 YY 
//  void createNews(News news);
  void createNews(NewsDTO newsDTO);
  
  News readNews(Long newsNo);

  List<News> readAllNews();

//  void updateNews(News news);
  void updateNews(NewsDTO newsDTO);
  
  void deleteNews(Long newsNo);

}
