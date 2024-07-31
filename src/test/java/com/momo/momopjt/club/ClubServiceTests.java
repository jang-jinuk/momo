package com.momo.momopjt.club;

import com.momo.momopjt.photo.PhotoDTO;
import com.momo.momopjt.user.User;
import com.momo.momopjt.userandclub.UserAndClubDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@SpringBootTest
@Log4j2
public class ClubServiceTests {

  @Autowired
  private ClubService clubService;

  //모임 생성 테스트
  @Test
  public void createTest() {
    User user = new User();
    user.setUserNo(1L);

    UserAndClubDTO userAndClubDTO = new UserAndClubDTO();
    userAndClubDTO.setUserNo(user);

    PhotoDTO photoDTO = PhotoDTO.builder()
        .photoUUID("test.jpg")
        .uploader(user)
        .photoCreateDate(Instant.now())
        .build();

    ClubDTO clubDTO = ClubDTO.builder()
        .clubArea("서울시 영등포구 신길동")
        .clubCategory("운동")
        .clubName("조축저축")
        .clubContent("조기축구하면서 저축도 하자")
        .clubMax(20)
        .clubGender('m')
        .clubCreateDate(Instant.now())
        .build();

    try {
      clubService.createClub(clubDTO, userAndClubDTO); //PhotoDTO 제거 0729 YY
    } catch (ClubService.ClubNameException e) {
      e.printStackTrace();
    }

  }

  //특정 모임 조회 테스트
  @Test
  @Transactional
  public void readOneTest() {
    ClubDTO clubDTO = clubService.readOneClub(1L);
    log.info(clubDTO);
  }

  //모든 모임 조회 테스트
  @Test
  @Transactional
  public void readAllTest() {
    List<ClubDTO> clubDTOs = clubService.readAllClub();
    log.info(clubDTOs);
  }

  //모임 정보 수정 테스트
  @Test
  public void updateTest() {

    User user = new User();
    user.setUserNo(1L);

//    PhotoDTO photoDTO = PhotoDTO.builder()
//        .photoUUID("00002test")
//        .uploader(user)
//        .photoCreateDate(Instant.now())
//        .build();

    ClubDTO clubDTO = ClubDTO.builder()
        .clubNo(2L)
        .clubPhotoUUID("testPhoto")
        .clubArea("테스트 지역 수정")
        .clubCategory("테스트 카테고리 수정")
        .clubContent("테스트 모임 소개 수정")
        .clubMax(5)
        .build();
    clubService.updateClub(clubDTO);
  }

  //모인 해산 기능 테스트
  @Test
  public void disbandClubTest() {
    clubService.deleteClub(3L);
  }

}