package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.reply.ReplyRepository;
import com.momo.momopjt.user.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ArticlePhotoTests {

  @Autowired
  private ArticleRepository articleRepository;
  @Autowired
  private ReplyRepository replyRepository;
@Autowired
private ArticleService articleService;

//  @Test
//  public void testInsertWithImage(){
//
//    User user = new User();
//    user.setUserNo(1L);
//
//    Club club = new Club();
//    club.setClubNo(1L);
//
//    Article article = Article.builder()
//        .articleNo(-1L)
//        .userNo(user)
//        .articleContent("articleContent")
//        .articleCreateDate(Instant.now())
//        .articleTitle("articleTitle")
//        .clubNo(club)
//        .build();
//
//    for (int i=0; i<3; i++){
//
//      article.addImage(UUID.randomUUID().toString(), ".jpg");
//      log.info("----------------- [addImage]-----------------");
//
//    }
//
//    articleRepository.save(article);
//
//  }


//  @Test
//  void testReadWithImages(){
////    Optional<Article> result = articleRepository.findById(8L);
//    Optional<Article> result = articleRepository.findByIdWithImages(8L);
//
//    Article article = result.orElseThrow();
//
//    log.info("----------------- [Article : {}]-----------------",article);
//    log.info("----------------- [ImageSet : {}]-----------------",article.getImageSet());
//
//    //No Session 에러는 Eager가 아니라 EntityGraph 처리;
//
//  }



//  @Test
//  @Commit
//  @Transactional
//  void testModifyImages(){
//    Article article = articleRepository.findByIdWithImages(8L).orElseThrow();
//
//    article.clearImages();
//
//    for (int i=0; i<3; i++){
//      article.addImage(UUID.randomUUID().toString(), ".jpg");
//    }
//
//    articleRepository.save(article);
//
//  }


//  @Test
//  @Transactional
//  @Commit
//  public void testRemoveAll(){
//
//    Long articleNo = 8L;
//
//    replyRepository.deleteByArticleNoArticleNo(articleNo);
//
//    articleRepository.deleteById(articleNo);
//
//  }


  //더미 데이터 추가
//  @Test
//  public void testInsertDummyData(){
//
//    User user = new User();
//    user.setUserNo(1L);
//
//    Club club = new Club();
//    club.setClubNo(1L);
//
//    for (int i=0; i<100; i++){
//
//      Article article = Article.builder()
//          .articleNo(-1L)
//          .articleCreateDate(Instant.now())
//          .articleContent("articleContent" + i)
//          .articleTitle("articleTitle" + i)
//          .userNo(user)
//          .clubNo(club)
//          .build();
//
//      for (int j=0; j<3; j++){
//
//        if(i%5==0) {
//          continue;
//        }
//        article.addImage(UUID.randomUUID().toString(), ".jpg");
//
//      }
//
//      articleRepository.save(article);
//
//    }
//
//  }

  @Test
  void testRegisterWithImages(Article article){

    log.info("----------------- [articleService.getClass.getName]-----------------{}",
        articleService.getClass().getName());

    User user = new User();
    user.setUserNo(1L);

    Club club = new Club();
    club.setClubNo(1L);



    ArticleDTO articleDTO = ArticleDTO.builder()
        .articleNo(-1L)
        .articleContent("testContent asd")
        .articleTitle("testTitle title")

        .articleState('0')
        .articleScore(0)
        .articleCreateDate(Instant.now())
        .userNo(user)
        .clubNo(club)
        .build();
//
//    List<String> fileNames = article.getImageSet().stream()
//        .map(articleImage -> articleImage.getUuid()+articleImage.getExtension())
//        .collect(Collectors.toList());

    articleDTO.setFileNames(
        Arrays.asList(
            UUID.randomUUID().toString()+".png",
            UUID.randomUUID().toString()+".png",
            UUID.randomUUID().toString()+".png"
        ));

//    long articleNo = articleService.register(articleDTO);
//    articleService.register(articleDTO);
    long articleNo = articleService.createArticle(articleDTO);
    log.info("----------------- [articleNo]-----------------{}", articleNo);

  }

  @Test


}
