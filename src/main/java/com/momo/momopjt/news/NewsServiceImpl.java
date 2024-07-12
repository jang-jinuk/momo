package com.momo.momopjt.news;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Log4j2
public class NewsServiceImpl implements NewsService {

  @Autowired
  private NewsRepository newsRepository;

  //공지 생성
  @Override
  public void createNews(News news) {
    log.info("----------------- [createNews]-----------------");

    newsRepository.save(news);

  }

  //공지 조회 1개
  @Override
  public News readNews(Long newsNo) {
    log.info("----------------- [ReadNews]-----------------");
    return newsRepository.findById(newsNo).orElseThrow();
  }

  //공지 전체 조회
  @Override
  public List<News> readAllNews(){
    log.info("----------------- [ReadAllNews]-----------------");

    return newsRepository.findAll();

  }

  //공지 업데이트
  @Override
  public void updateNews(News news) {
    log.info("----------------- [UpdateNews]-----------------");

    newsRepository.save(news);

  }

  //공지 삭제
  @Override
  public void deleteNews(Long newsNo) {
    log.info("----------------- [DeleteNews]-----------------");

    newsRepository.deleteById(newsNo);


  }
}
