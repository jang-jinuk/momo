package com.momo.momopjt.club;

import com.momo.momopjt.photo.PhotoDTO;
import com.momo.momopjt.user.User;
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

    PhotoDTO photoDTO = PhotoDTO.builder()
        .photoUuid("00002test")
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
    clubService.createClub(clubDTO, photoDTO);
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

    PhotoDTO photoDTO = PhotoDTO.builder()
        .photoUuid("00002test")
        .userNo(user)
        .photoSize(10)
        .photoCreateDate(Instant.now())
        .photoOriginalName("test img 수정")
        .photoSaveName("test save img 수정")
        .photoThumbnail("test thumbnail img 수정")
        .build();

    ClubDTO clubDTO = ClubDTO.builder()
        .clubNo(2L)
        .clubArea("테스트 지역 수정")
        .clubCategory("테스트 카테고리 수정")
        .clubContent("테스트 모임 소개 수정")
        .clubMax(5)
        .build();
    clubService.updateClub(clubDTO, photoDTO);
  }

  //모인 해산 기능 테스트
  @Test
  public void disbandClubTest() {
    clubService.disbandClub(3L);
  }
}