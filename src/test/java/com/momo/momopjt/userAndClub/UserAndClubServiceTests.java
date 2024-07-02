package com.momo.momopjt.userAndClub;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.club.UserAndClubDTO;
import com.momo.momopjt.club.UserAndClubService;
import com.momo.momopjt.user.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class UserAndClubServiceTests {
  @Autowired
  private UserAndClubService userAndClubService;

  //모임 가입 신청 기능 테스트
  @Test
  public void joinClubTest() {
    User user = new User();
    user.setUserNo(1L);

    Club club = new Club();
    club.setClubNo(1L);


    UserAndClubDTO userAndClubDTO = UserAndClubDTO.builder()
        .userNo(user)
        .clubNo(club)
        .build();
    userAndClubService.joinClub(userAndClubDTO);
  }

  //가입 신청 승인 기능 테스트
  @Test
  public void approveJoinTest() {
    Long userNo = 2L;
    log.info("-------------승인 요청-------------");
    userAndClubService.approveJoin(userNo);
  }
}
