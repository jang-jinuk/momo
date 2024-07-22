package com.momo.momopjt.article;


import com.momo.momopjt.club.Club;

import com.momo.momopjt.user.User;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Log4j2
public class ArticleServiceTests {

  @Autowired
  private ArticleService articleService;

  //후기글 작성
  @Test
  void createArticle() {

    Club clubNo = Club.builder().clubNo(1L).build();
    User userNo = User.builder().userNo(1L).build();
    ArticleDTO articleDTO = ArticleDTO.builder()

        .clubNo(clubNo) // 클럽 번호 설정
        .userNo(userNo) // 사용자 번호 설정
        .articleTitle("ㅎㅎ등산 너무 재밌었어요")
        .articleContent("다음에 또 가고 싶네요")
        .articleCreateDate(Instant.now())
        .articleState('0')
        .articleScore(2)
        .build();
    Article createdArticle = articleService.createArticle(articleDTO);
  }


  //후기글 조회
  @Test
  void 후기글조회() {
    Long articleNo = 1L;
    ArticleDTO articleDTO = articleService.getArticleById(articleNo);
  }

  //후기글 모두 조회
  @Test
  void getAllArticles() {
    List<ArticleDTO> articles = articleService.getAllArticles();
    log.info(articles);
  }


  @Test
  void 후기글수정() {
    Club clubNo = Club.builder().clubNo(1L).build();
    User userNo = User.builder().userNo(1L).build();

    ArticleDTO articleDTO = ArticleDTO.builder()
        .clubNo(clubNo) // 클럽 번호 설정
        .userNo(userNo) // 사용자 번호 설정
        .articleTitle("떡볶이 진짜 맛있다")
        .articleContent("다음에 또 가고 싶네요")
        .articleCreateDate(Instant.now())
        .articleState('0')
        .articleScore(2)
        .build();

    Article createdArticle = articleService.createArticle(articleDTO);

  }


  //후기글 삭제
  @Test
  void deleteArticle() {

    Long articleNo = 2L;
    articleService.deleteArticle(articleNo);
  }

}





