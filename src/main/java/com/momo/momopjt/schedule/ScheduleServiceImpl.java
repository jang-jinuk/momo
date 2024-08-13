package com.momo.momopjt.schedule;
//일정 CRUD 및 일정 참가 기능

import com.momo.momopjt.alarm.AlarmService;
import com.momo.momopjt.club.Club;
import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.reply.ReplyDTO;
import com.momo.momopjt.reply.ReplyService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import com.momo.momopjt.userandschedule.UserAndScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private final ScheduleRepository scheduleRepository;

  private final UserAndScheduleService userAndScheduleService;
  private final PhotoService photoService;
  private final AlarmService alarmService;
  private final UserService userService;
  private final ReplyService replyService;

  private final ModelMapper modelMapper;

  //일정 생성
  @Override
  public Long createSchedule(ScheduleDTO scheduleDTO, UserAndScheduleDTO userAndScheduleDTO) throws ScheduleMaxException, ScheduleDateException {

    //일정 참가 수 검사 로직
    if (scheduleDTO.getScheduleMax() <= 0) {
      throw new ScheduleMaxException();
    }

    //일정 시작 날짜 검사 로직
    if (scheduleDTO.getScheduleStartDate().isBefore(Instant.now())) {
      throw new ScheduleDateException();
    }

    Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);

    Long scheduleNo = scheduleRepository.save(schedule).getScheduleNo();
    log.info("------------ [일정 생성 완료] ------------");

    userAndScheduleDTO.setScheduleNo(schedule);
    log.info("------------ [생성된 일정 번호 전달] ------------");

    //일정 참가자 목록에 주최자 추가(등록)
    userAndScheduleDTO.setIsHost(true);
    userAndScheduleService.addParticipant(userAndScheduleDTO);
    log.info("------------ [주최자 등록 완료] ------------");

    // 현재 로그인된 사용자 정보를 얻기
    User user = userService.getCurrentUser();

    // 일정 생성 알림 생성
    alarmService.createScheduleCreatedAlarm(user, schedule);
    log.info("-------- [일정 생성 알림 이벤트] -------");


    return scheduleNo;

  }


  //특정 일정 조회
  @Override
  public ScheduleDTO readOneSchedule(Long scheduleNo) {
    // 0724 YY
    boolean checkExist = scheduleRepository.existsById(scheduleNo);
    if (checkExist) {
      Optional<Schedule> result = scheduleRepository.findById(scheduleNo);
      Schedule schedule = result.orElseThrow();
      return modelMapper.map(schedule, ScheduleDTO.class);
    }
//    return null;
    ScheduleDTO scheduleDTO = new ScheduleDTO();
    return scheduleDTO;
  }

  //일정 정보 수정
  @Override
  public Boolean updateSchedule(ScheduleDTO scheduleDTO) {
    Optional<Schedule> result = scheduleRepository.findById(scheduleDTO.getScheduleNo());
    Schedule schedule = result.orElseThrow();
    log.info("------------ [수정할 일정 조회 완료] ------------");

    Integer currentParticipants = schedule.getScheduleParticipants();
    Integer updateMax = scheduleDTO.getScheduleMax();

    //수정할려는 정원수가 현재 정원수보다 크거나 같으면 수정 가능
    if (currentParticipants > updateMax) {
      log.info("------------ [현재 참가자 수보다 작게 설정할 수 없습니다.] ------------");
      return true;
    } else {
      schedule.setScheduleTitle(scheduleDTO.getScheduleTitle());
      schedule.setScheduleContent(scheduleDTO.getScheduleContent());
      schedule.setScheduleMax(scheduleDTO.getScheduleMax());
      schedule.setSchedulePlace(scheduleDTO.getSchedulePlace());
      schedule.setScheduleStartDate(scheduleDTO.getScheduleStartDate());
      schedule.setSchedulePhotoUUID(scheduleDTO.getSchedulePhotoUUID());

      scheduleRepository.save(schedule);
    }

    return false;
  }


  //일정 참가
  // 일단 스케줄 번호를 넘겨주고 해당 일정을 불러온다.
  // 불러온 일정의 참가정원과 현재 참가인원을 비교한다.
  // 참가인원이 마감되지 않았으면 불러온 일정에 현재 참가인원 수를 추가한다.
  // 참가인원 테이블에 참가자 정보와 일정 번호를 저장한다.
  @Override
  public String joinSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO) {
    Optional<Schedule> result = scheduleRepository.findById(scheduleNo);
    Schedule schedule = result.orElseThrow();
    log.info("------------ [해당 일정 정보 조회] ------------");

    log.info("------------ [참가 가능 여부 확인]------------");
    userAndScheduleDTO.setScheduleNo(schedule); //일정 번호 전달

    if (userAndScheduleService.isParticipate(userAndScheduleDTO) == 2) {
      return "이미 참가한 일정입니다.";

    } else if (schedule.getScheduleMax() > schedule.getScheduleParticipants()) {
      userAndScheduleService.addParticipant(userAndScheduleDTO);
      log.info("------------ [참가인원 정보 추가]------------");

      Integer participants = schedule.getScheduleParticipants();
      schedule.setScheduleParticipants(participants + 1);
      scheduleRepository.save(schedule);
      log.info("------------ [참가인원 추가 완료]------------");

      // 참가 알람 생성
      User user = userAndScheduleDTO.getUserNo();
      alarmService.createParticipateAlarm(user, schedule);
      log.info("---------------[참가인원 알림 이벤트 전송 완료]-----------------");
    } else {
      return "인원이 마감되었습니다.";
    }

    return "신청이 완료되었습니다.";
  }

  //일정 참가 취소
  // 회원 번호와 일정 번호를 받아온다.
  // 해당 일정 참가자 목록에서 해당 회원을 제외한다.
  // 일정 현재 참가인원을 한명 차감한다.
  @Override
  public String leaveSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO) {
    Optional<Schedule> result = scheduleRepository.findById(scheduleNo);
    Schedule schedule = result.orElseThrow();
    log.info("------------ [해당 일정 정보 조회] ------------");

    //참석하지 않은 일정인지 확인
    userAndScheduleDTO.setScheduleNo(schedule);

    if (userAndScheduleService.isParticipate(userAndScheduleDTO) != 2) {
      return "참석하지 않은 일정입니다.";
    } else {
      userAndScheduleDTO.setScheduleNo(schedule); //일정 번호 전달
      userAndScheduleService.subtractParticipant(userAndScheduleDTO);

      Integer participants = schedule.getScheduleParticipants();
      schedule.setScheduleParticipants(participants - 1);
      scheduleRepository.save(schedule);
      log.info("------------ [참가 취소 완료] ------------");

      // 참가 취소 알람 생성
      User user = userAndScheduleDTO.getUserNo();
      alarmService.createCancelParticipateAlarm(user, schedule);
      log.info("------------[일정 참가 취소 이벤트 알림]---------------");
    }
    return "참석이 취소되었습니다.";
  }


  //마감되지 않은 일정 조회
  // 마감 기준 : 시간마감
  @Override
  public List<ScheduleDTO> readOngoingSchedules(Club clubNo) {
    List<Schedule> schedules = scheduleRepository.findOngoingSchedules(clubNo);
    List<ScheduleDTO> scheduleDTOList = schedules.stream()
        .map(schedule -> modelMapper.map(schedule, ScheduleDTO.class))
        .collect(Collectors.toList());

    return scheduleDTOList;
  }

  //마감된 일정 조회
  @Override
  public List<ScheduleDTO> readEndSchedules(Club clubNo) {
    List<Schedule> schedules = scheduleRepository.findEndSchedules(clubNo);
    List<ScheduleDTO> scheduleDTOList = schedules.stream()
        .map(schedule -> modelMapper.map(schedule, ScheduleDTO.class))
        .collect(Collectors.toList());

    return scheduleDTOList;
  }

  //일정 삭제
  @Override
  public void deleteSchedule(Long scheduleNo) {
    Optional<Schedule> result = scheduleRepository.findById(scheduleNo);
    Schedule schedule = result.orElseThrow();

    List<ReplyDTO> replyDTOList = replyService.readReplyAllBySchedule(schedule.getScheduleNo());
    for (ReplyDTO replyDTO : replyDTOList) {
      replyService.forceDeleteReply(replyDTO.getReplyNo());

    }
    log.info("------------ [일정 댓글 삭제처리 완료] ------------");
    if (!schedule.getSchedulePhotoUUID().equals("ScheduleDefaultPhoto")) {
      photoService.deletePhoto(schedule.getSchedulePhotoUUID());
    }
    log.info("------------ [일정 사진 삭제 처리 완료] ------------");
    schedule.setScheduleNo(scheduleNo);
    userAndScheduleService.deleteParticipant(schedule);
    log.info("------------ [일정 참석 인원 처리 완료] ------------");
    scheduleRepository.deleteById(scheduleNo);
    log.info("------------ [일정 삭제 처리 완료] ------------");

    // 현재 로그인된 사용자 정보를 얻기
    User user = userService.getCurrentUser();

    alarmService.createScheduleDeletedAlarm(user, schedule);
    log.info("-------- [일정 삭제 알림 이벤트] -------");

  }

  //일정 인원 마감 확인
  @Override
  public Boolean isScheduleFull(Long scheduleNo) {
    Optional<Schedule> result = scheduleRepository.findById(scheduleNo);
    Schedule schedule = result.orElseThrow();

    if (schedule.getScheduleMax() == schedule.getScheduleParticipants()) {
      return true;
    }

    return false;
  }

  @Override
  public List<ScheduleDTO> readMyParticipatedSchedules(Club clubNo, User userNo) {
    List<Schedule> schedules = scheduleRepository.findEndSchedules(clubNo); //마감된 일정 조회

    List<ScheduleDTO> scheduleDTOList = schedules.stream()
        .map(schedule -> modelMapper.map(schedule, ScheduleDTO.class))
        .collect(Collectors.toList());

    List<ScheduleDTO> participatedSchedules = new ArrayList<>();

    for (ScheduleDTO schedule : scheduleDTOList) {
      UserAndScheduleDTO userAndScheduleDTO = new UserAndScheduleDTO();
      userAndScheduleDTO.setUserNo(userNo);
      Schedule scheduleNo = new Schedule();
      scheduleNo.setScheduleNo(schedule.getScheduleNo());
      userAndScheduleDTO.setScheduleNo(scheduleNo);

      if (2 != userAndScheduleService.isParticipate(userAndScheduleDTO)) { //일정에 참석했는지 확인(2:알정에 참석하지 않은 인원)
        participatedSchedules.add(schedule);
      }

    }

    return participatedSchedules;
  }
}
