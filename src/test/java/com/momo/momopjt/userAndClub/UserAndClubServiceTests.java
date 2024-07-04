package com.momo.momopjt.userandclub;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class UserAndClubServiceTests {
  @Autowired
  private UserAndClubService userAndClubService;

  //모임 가입 신청 기능 테스트
  @Test
  public void joinClubTest() {
    User user = new User();
    user.setUserNo(5L);

    Club club = new Club();
    club.setClubNo(3L);


    UserAndClubDTO userAndClubDTO = UserAndClubDTO.builder()
        .userNo(user)
        .clubNo(club)
        .build();
    userAndClubService.joinClub(userAndClubDTO);
  }

  //가입 신청 승인 기능 테스트
  @Test
  public void approveJoinTest() {
    Long id = 8L;
    log.info("-------------승인 요청-------------");
    userAndClubService.approveJoin(id);
  }

  //맴버 탈퇴 기능 테스트
  @Test
  public void disbandTest() {
    Long id = 3L;
    log.info("-------------탈퇴 요청-------------");
    userAndClubService.leaveClub(id);
  }

  //특정 모임 맴버 조회 기능 테스트
  @Test
  public void readAllMembersTest() {
    Club club = new Club();
    club.setClubNo(3L);
    List<UserAndClubDTO> userAndClubDTOS = userAndClubService.readAllMembers(club);
    log.info(userAndClubDTOS);
  }

  //모임 가입 신청 내역 조회 기능 테스트
  @Test
  public void readAllJoinListTest() {
    Club club = new Club();
    club.setClubNo(2L);

    List<UserAndClubDTO> userAndClubDTOS = userAndClubService.readAllJoinList(club);
    log.info(userAndClubDTOS);
  }
}
