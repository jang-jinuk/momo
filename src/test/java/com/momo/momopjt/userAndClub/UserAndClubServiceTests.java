package com.momo.momopjt.userAndClub;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.club.UserAndClub;
import com.momo.momopjt.club.UserAndClubDTO;
import com.momo.momopjt.club.UserAndClubService;
import com.momo.momopjt.user.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
@Log4j2
public class UserAndClubServiceTests {
  @Autowired
  private UserAndClubService userAndClubService;

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
}
