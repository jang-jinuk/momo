package com.momo.momopjt.schedule;

import com.momo.momopjt.Photo.PhotoDTO;
import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import com.momo.momopjt.userAndSchedule.UserAndScheduleDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
@Log4j2
public class ScheduleServiceTests {
  @Autowired
  private ScheduleService scheduleService;

  //모임 생성
  @Test
  public void createScheduleTest() {
    Club club = new Club();
    club.setClubNo(2L);

    User user = new User();
    user.setUserNo(3L);

    //일정 정보
    ScheduleDTO scheduleDTO = ScheduleDTO.builder()
        .clubNo(club)
        .scheduleTitle("일정 제목 테스트1")
        .scheduleContent("일정 내용 테스트1")
        .scheduleMax(10)
        .schedulePlace("서울시 구로구")
        .scheduleStartDate(Instant.now())
        .build();
    //일정 주체자 정보
    UserAndScheduleDTO userAndScheduleDTO = UserAndScheduleDTO.builder()
        .userNo(user)
        .build();

    scheduleService.createSchedule(scheduleDTO, userAndScheduleDTO);
  }
}
