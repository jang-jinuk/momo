package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Log4j2
public class ScheduleServiceTests {

  @Autowired
  private ScheduleService scheduleService;
  private ScheduleRepository scheduleRepository;

  //모임 생성
  @Test
  public void createScheduleTest() {
    Club club = new Club();
    club.setClubNo(1L);

    User user = new User();
    user.setUserNo(3L);

    LocalDateTime localDateTime = LocalDateTime.of(2024, 8, 4, 13, 30);

    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

    Instant instant = zonedDateTime.toInstant();


    //일정 정보
    ScheduleDTO scheduleDTO = ScheduleDTO.builder()
        .clubNo(club)
        .scheduleTitle("일정 제목 테스트6")
        .scheduleContent("일정 내용 테스트6")
        .scheduleMax(15)
        .schedulePlace("서울시 양천구")
        .scheduleStartDate(instant)
        .build();
    //일정 주체자 정보
    UserAndScheduleDTO userAndScheduleDTO = UserAndScheduleDTO.builder()
        .userNo(user)
        .build();

    scheduleService.createSchedule(scheduleDTO, userAndScheduleDTO);
  }

  @Test
  public void joinScheduleTest() {
    //Given
    User user = new User();
    user.setUserNo(5L);
    UserAndScheduleDTO userAndScheduleDTO = UserAndScheduleDTO.builder()
        .userNo(user)
        .build();

    Long scheduleNo = 1L;

    //When
    Integer expectedParticipantsNumber = scheduleService.findSchedule(scheduleNo).getScheduleParticipants() + 1;
    scheduleService.joinSchedule(scheduleNo, userAndScheduleDTO);
    Integer addedParticipantsNumber = scheduleService.findSchedule(scheduleNo).getScheduleParticipants();

    //Then
    assertEquals(expectedParticipantsNumber, addedParticipantsNumber);
  }
}
