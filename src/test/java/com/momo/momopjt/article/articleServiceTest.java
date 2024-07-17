package com.momo.momopjt.article;


import com.momo.momopjt.club.Club;
import com.momo.momopjt.club.ClubDTO;
import com.momo.momopjt.club.ClubRepository;
import com.momo.momopjt.photo.PhotoDTO;
import com.momo.momopjt.club.ClubService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.userandclub.UserAndClubDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class articleServiceTest {

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ClubService clubService;
  private Long testClubId;


  // 테스트 전에 실행되는 메서드
  @BeforeEach
  public void createTest() {
    User user = new User();
    user.setUserNo(1L);

    PhotoDTO photoDTO = PhotoDTO.builder()
        .photoUUID("00002test")
        .userNo(user)
        .photoSize(10)
        .photoCreateDate(Instant.now())
        .photoOriginalName("test img")
        .photoSaveName("test save img")
        .photoThumbnail("test thumbnail img")
        .build();

    ClubDTO clubDTO = ClubDTO.builder()
        .clubArea("테스트 지역1")
        .clubCategory("테스트 카테고리1")
        .clubName("테스트 모임1")
        .clubContent("테스트 모임 소개1")
        .clubMax(5)
        .clubGender('m')
        .clubCreateDate(Instant.now())
        .build();

    //TODO 0716 YY
    testClubId = clubService.createClub(clubDTO, photoDTO, UserAndClubDTO.builder().build()); // 클럽 번호를 가져오도록 수정

    assertNotNull(testClubId);
  }

  @Test
  void 모임내후기작성() {
    // Given
    ArticleDTO articleDTO = new ArticleDTO();
    articleDTO.setArticleTitle("ㅎㅎ오늘너무즐거웠습니다");
    articleDTO.setArticleContent("아니 오늘 ~ 산을 다녀왔는데 날씨가 너무 좋은 거 있죠?");
    articleDTO.setArticleState('0');
    articleDTO.setArticleScore(111);
    articleDTO.setClubNo(testClubId);

    // When
    ArticleDTO savedArticle = articleService.createArticle(articleDTO);

    // Then

    assertNotNull(savedArticle);
    assertNotNull(savedArticle.getArticleNo());
    //savedArticle 객체가 null이 아닌지 확인하는 작업  즉, Article이 성공적으로 저장되었는지 확인!

    assertEquals(articleDTO.getArticleTitle(), savedArticle.getArticleTitle());
    //articleDTO 객체에 설정한 Article 제목과 savedArticle 객체에 저장된 Article 제목이 같은지 확인

    assertEquals(articleDTO.getArticleContent(), savedArticle.getArticleContent());
    //articleDTO 객체에 설정한 Article 내용과 savedArticle 객체에 저장된 Article 내용이 같은지 확인

    assertEquals(articleDTO.getArticleState(), savedArticle.getArticleState());
    //articleDTO 객체에 설정한 Article 상태와 savedArticle 객체에 저장된 Article 상태가 같은지 확인

    assertEquals(articleDTO.getArticleScore(), savedArticle.getArticleScore());
    //articleDTO 객체에 설정한 Article 점수와 savedArticle 객체에 저장된 Article 점수가 같은지 확인

    assertEquals(articleDTO.getClubNo(), savedArticle.getClubNo());
    //articleDTO 객체에 설정한 Club 번호와 savedArticle 객체에 저장된 Club 번호가 같은지 확인

    assertNotNull(savedArticle.getArticleCreateDate());
    //savedArticle 객체의 getArticleCreateDate() 메서드를 통해 반환된 Article 생성 날짜가
    // NULL이 아닌지 확인
  }

  @Test
  void 후기작성한거수정하기() {
    ArticleDTO articleDTO = new ArticleDTO();
    articleDTO.setArticleTitle("오늘 맛있는 밥먹었음");
    articleDTO.setArticleContent("제육볶음은진짜맛있어");
    articleDTO.setArticleState('0');
    articleDTO.setArticleScore(5);
    articleDTO.setClubNo(testClubId);

    //생성한 후기글을 실제 서비스를 통해 저장하고 저장된 객체를 반환
    ArticleDTO savedArticle = articleService.createArticle(articleDTO);

    // When
    // 저장된 후기글을 수정하기 위해 수정할 데이터를 설정
    savedArticle.setArticleTitle("오늘 맛있는 밥먹었음");
    savedArticle.setArticleContent("고등어맛있네");
    savedArticle.setArticleScore(3);

    // 수정된 후기글을 서비스를 통해 업데이트, 업데이트된 후기글 객체를 반환
    ArticleDTO updatedArticle = articleService.updateArticle(savedArticle.getArticleNo(), savedArticle);

    // Then
    // 업데이트된 후기글이 null이 아닌지 확인
    assertNotNull(updatedArticle);

    // 업데이트된 후기글의 주요 속성들이 예상대로 업데이트되었는지 검증작업~
    assertEquals(savedArticle.getArticleNo(), updatedArticle.getArticleNo());  // 후기글 번호 일치 여부 확인
    assertEquals(savedArticle.getArticleTitle(), updatedArticle.getArticleTitle());  // 후기글 제목 일치 여부 확인
    assertEquals(savedArticle.getArticleContent(), updatedArticle.getArticleContent());  // 후기글 내용 일치 여부 확인
    assertEquals(savedArticle.getArticleState(), updatedArticle.getArticleState());  // 후기글 상태 일치 여부 확인
    assertEquals(savedArticle.getArticleScore(), updatedArticle.getArticleScore());  // 후기글 점수 일치 여부 확인
    assertEquals(savedArticle.getClubNo(), updatedArticle.getClubNo());  // 후기글이 속한 클럽 번호 일치 여부 확인

    // 업데이트된 후기글의 생성 날짜가 null이 아닌지 확인
    assertNotNull(updatedArticle.getArticleCreateDate());
  }


  @Test
  public void 고등어글조회하기() {
    // 테스트할 articleNo
    Long articleNo = 11L;

    // 서비스 메서드 호출
    ArticleDTO articleDTO = articleService.getArticleById(articleNo);

    // 로깅
    log.info("Retrieved ArticleDTO: {}", articleDTO);
  }

  @Test
  void 고등어구이삭제하기() {

    Long articleNo = 11L;
    articleService.deleteArticle(articleNo);

  }


}