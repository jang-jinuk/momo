package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Log4j2
public class ScheduleServiceTests {

  @Autowired
  private ScheduleService scheduleService;
  @Autowired
  private ScheduleRepository scheduleRepository;

  //모임 생성 테스트
  @Test
  public void createScheduleTest() {
    //Given
    Club club = new Club();
    club.setClubNo(1L);

    User user = new User();
    user.setUserNo(4L);

    LocalDateTime localDateTime = LocalDateTime.of(2024, 9, 5, 12, 40);

    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

    Instant instant = zonedDateTime.toInstant();

    //일정 정보
    ScheduleDTO scheduleDTO = ScheduleDTO.builder()
        .clubNo(club)
        .scheduleTitle("마감되지 않은 일정")
        .scheduleContent("마감되지 않은 일정")
        .scheduleMax(5)
        .schedulePlace("서울시 강남구")
        .scheduleStartDate(instant)
        .build();
    //일정 주체자 정보
    UserAndScheduleDTO userAndScheduleDTO = UserAndScheduleDTO.builder()
        .userNo(user)
        .build();

    //When
     Long createScheduleNo = scheduleService.createSchedule(scheduleDTO, userAndScheduleDTO);

    //Then
    Long expectedScheduleNo = scheduleService.findSchedule(createScheduleNo).getScheduleNo();
    assertEquals(createScheduleNo, expectedScheduleNo);
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
    user.setUserNo(3L);
    UserAndScheduleDTO userAndScheduleDTO = UserAndScheduleDTO.builder()
        .userNo(user)
        .build();

    Long scheduleNo = 4L;

    ScheduleDTO scheduleDTO = scheduleService.findSchedule(scheduleNo);
    Integer expectedParticipantsNumber = scheduleDTO.getScheduleParticipants() + 1;

    //When
    String addedParticipantsNumber = scheduleService.joinSchedule(scheduleNo, userAndScheduleDTO);

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
    String subtractedParticipantsNumber = scheduleService.leaveSchedule(scheduleNo, userAndScheduleDTO);

    //Then
    assertEquals(expectedParticipantsNumber, subtractedParticipantsNumber);
  }

  //마감되지 않은 일정 조회 기능 테스트
  @Test
  public void getOngoingSchedulesTest() {
    //Given
    Club club = new Club();
    club.setClubNo(1L);
    //When
    List<ScheduleDTO> scheduleDTOS = scheduleService.getOngoingSchedules(club);
    //Then
    scheduleDTOS.forEach(schedule -> log.info(
        "no : {}, title : {}, content : {}, photo : {}, place : {}, max : {}, participants : {}, start_date : {}",
        schedule.getScheduleNo(),
        schedule.getScheduleTitle(),
        schedule.getScheduleContent(),
        schedule.getSchedulePhoto(),
        schedule.getSchedulePlace(),
        schedule.getScheduleMax(),
        schedule.getScheduleParticipants(),
        schedule.getScheduleStartDate()));
  }

  //일정 삭제 테스트
  @Test
  public void deleteScheduleTest() {
    Long deleteScheduleNo = 5L;
    scheduleService.deleteSchedule(deleteScheduleNo);
  }
}
