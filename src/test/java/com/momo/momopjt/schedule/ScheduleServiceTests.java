package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Log4j2
public class ScheduleServiceTests {

  @Autowired
  private ScheduleService scheduleService;

  //모임 생성 테스트
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

  //일정 정보 수정 테스트
  @Test
  public void updateScheduleTest() {
    //Given
    Club club = new Club();
    club.setClubNo(1L);

    LocalDateTime localDateTime = LocalDateTime.of(2024, 8, 10, 15, 0);

    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

    Instant instant = zonedDateTime.toInstant();

    ScheduleDTO scheduleDTO = ScheduleDTO.builder()
        .scheduleNo(1L)
        .clubNo(club)
        .scheduleTitle("일정 제목 수정.")
        .scheduleContent("일정 내용 수정.")
        .scheduleMax(10)
        .schedulePlace("서울시 도봉구")
        .scheduleStartDate(instant)
        .build();

    //When
    Map<String, String> result = scheduleService.updateSchedule(scheduleDTO);
    //Then
    assertEquals(scheduleDTO.getScheduleNo(),Long.parseLong(result.get("result")));
  }

  //일정 참가 테스트
  @Test
  public void joinScheduleTest() {
    //Given
    User user = new User();
    user.setUserNo(5L);
    UserAndScheduleDTO userAndScheduleDTO = UserAndScheduleDTO.builder()
        .userNo(user)
        .build();

    Long scheduleNo = 1L;

    ScheduleDTO scheduleDTO = scheduleService.findSchedule(scheduleNo);
    Integer expectedParticipantsNumber = scheduleDTO.getScheduleParticipants() + 1;

    //When
    Integer addedParticipantsNumber = scheduleService.joinSchedule(scheduleNo, userAndScheduleDTO);

    //Then
    assertEquals(expectedParticipantsNumber, addedParticipantsNumber);
  }

  //참가 취소
  @Test
  public void leaveScheduleTest() {
    //Given
    User user = new User();
    user.setUserNo(5L);
    UserAndScheduleDTO userAndScheduleDTO = UserAndScheduleDTO.builder()
        .userNo(user)
        .build();

    Long scheduleNo = 1L;

    ScheduleDTO scheduleDTO = scheduleService.findSchedule(scheduleNo);
    Integer expectedParticipantsNumber = scheduleDTO.getScheduleParticipants() - 1;

    //When
    Integer subtractedParticipantsNumber = scheduleService.leaveSchedule(scheduleNo, userAndScheduleDTO);

    //Then
    assertEquals(expectedParticipantsNumber, subtractedParticipantsNumber);
  }
}
