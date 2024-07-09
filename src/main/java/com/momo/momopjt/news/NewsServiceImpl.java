package com.momo.momopjt.news;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
@Log4j2
public class NewsServiceImpl implements NewsService {

  @Autowired
  private NewsRepository newsRepository;

  @Autowired
  private ModelMapper modelMapper;

  //공지 생성
  @Override
  public void createNews(NewsDTO newsDTO) {
    log.info("----------------- [createNews]-----------------");
    News news = modelMapper.map(newsDTO, News.class);
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
  public void updateNews(NewsDTO newsDTO) {
    log.info("----------------- [UpdateNews]-----------------");
    News news = newsRepository.findById(newsDTO.getNewsNo()).orElseThrow();

    // mapper 사용으로 주석처리
    /*
    news.setNewsNo(news.getNewsNo());
    news.setNewsContent(newsDTO.getNewsContent());
    news.setNewsCreateDate(newsDTO.getNewsCreateDate());
    news.setNewsModifyDate(Instant.now());
    news.setNewsTag(newsDTO.getNewsTag());
    news.setNewsTitle(newsDTO.getNewsTitle());
    */

    News updateNews = modelMapper.map(newsDTO, News.class);

    updateNews.setNewsModifyDate(Instant.now());

    log.info("----------------- [updateNews]-----------------");
    log.info(updateNews.toString());

    newsRepository.save(updateNews);

  }

  //공지 삭제
  @Override
  public void deleteNews(Long newsNo) {
    log.info("----------------- [DeleteNews]-----------------");

    newsRepository.deleteById(newsNo);


  }
}
