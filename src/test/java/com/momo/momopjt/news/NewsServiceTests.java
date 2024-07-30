package com.momo.momopjt.news;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

@SpringBootTest
@Log4j2
class NewsServiceTests {

  @Autowired
  private NewsService newsService;
  @Autowired
  private ModelMapper modelMapper;

  //Test 순서대로 Create, Read, ReadAll, Update, Delete
  @Test
  void newsCreateTest(){
    log.info("----------------- [news create test]-----------------");

    for (int i=1; i<100; i++) {

      NewsDTO tempNewsDTO = new NewsDTO();
      tempNewsDTO.setNewsNo(-1L); // 없는거 지정하면 알아서 Auto-increment 들어감
      tempNewsDTO.setNewsContent("TestContent"+i);
      tempNewsDTO.setNewsCreateDate(Instant.now()); //
      tempNewsDTO.setNewsModifyDate(null);
      tempNewsDTO.setNewsTag("tag"+i%10);
    tempNewsDTO.setNewsTag("testTag"); // char(10) 넘지않게
      tempNewsDTO.setNewsTitle("testTitle"+i);
      newsService.createNews(tempNewsDTO);

    }

  }

  @Test
  void newsReadTest(){
    log.info("----------------- [news read test]-----------------");
    News newsfortest = newsService.readNews(1L); // 찾을 공지 번호 입력
    log.info("readed news : "+newsfortest);
  }

  @Test
  void newsReadAllTest(){
    log.info("----------------- [news read all test]-----------------");
    List<News> newsList = newsService.readAllNews();
    log.info("newsList : "+newsList);
  }

  @Test
  void newsUpdateTest(){
    log.info("----------------- [news update test]-----------------");
    //1번 정보를 2번에다가 업데이트
    News willUpdateNews = newsService.readNews(1L);
    NewsDTO updateNewsDTO = modelMapper.map(willUpdateNews, NewsDTO.class);

    log.info("willUpdateNews modifyDate (1L) : "+willUpdateNews.getNewsModifyDate());

    //modify date 추가
    updateNewsDTO.setNewsModifyDate(Instant.now());
    updateNewsDTO.setNewsNo(2L);
    newsService.updateNews(updateNewsDTO);


    News newsCheckUpdated = newsService.readNews(2L);
    log.info("newsCheckUpdated modifyDate (2L) : "+newsCheckUpdated.getNewsModifyDate());

  }

  @Test
  void newsDeleteTest(){
    log.info("---------------- [news delete test]---------------");
    //지울 공지 넘버 입력
    Long numberWillDelete = 4L;

    if (newsService.readNews(numberWillDelete) == null){ // 삭제하기전 존재하는지 확인
      log.info("test Failed /----------------해당 번호 공지사항 존재 x ");
    } else {
      newsService.deleteNews(numberWillDelete);
      log.info("test Passed");
    }
  }

}