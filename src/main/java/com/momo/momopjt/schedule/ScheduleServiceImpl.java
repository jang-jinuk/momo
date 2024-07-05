package com.momo.momopjt.schedule;
//일정 CRUD 및 일정 참가 기능

import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import com.momo.momopjt.userandschedule.UserAndScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final ModelMapper modelMapper;
  private final UserAndScheduleService userAndScheduleService;

  //일정 생성
  @Override
  public Long createSchedule(ScheduleDTO scheduleDTO, UserAndScheduleDTO userAndScheduleDTO) {

    Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);
    Long scheduleNo = scheduleRepository.save(schedule).getScheduleNo();
    log.info("------------ [일정 생성 완료] ------------");

    userAndScheduleDTO.setScheduleNo(schedule);
    log.info("------------ [생성된 일정 번호 전달] ------------");

    //일정 참가자 목록에 주체자 추가(등록)
    userAndScheduleService.addParticipant(userAndScheduleDTO);
    log.info("------------ [주체자 등록 완료] ------------");
    return scheduleNo;
  }

  //특정 일정 조회
  @Override
  public ScheduleDTO findSchedule(Long scheduleNo) {
    Optional<Schedule> result = scheduleRepository.findById(scheduleNo);
    Schedule schedule = result.orElseThrow();
    return modelMapper.map(schedule, ScheduleDTO.class);
  }

  //일정 정보 수정
  @Override
  public Map<String, String> updateSchedule(ScheduleDTO scheduleDTO)  {
    Optional<Schedule> result = scheduleRepository.findById(scheduleDTO.getScheduleNo());
    Schedule schedule = result.orElseThrow();
    log.info("------------ [수정할 일정 조회 완료] ------------");

    Integer currentParticipants = schedule.getScheduleParticipants();
    Integer updateMax = scheduleDTO.getScheduleMax();

    Map<String, String> resultMap = new HashMap<>();

    //수정할려는 정원수가 현재 정원수보다 크거나 같으면 수정 가능
    if (currentParticipants > updateMax) {
      log.info("------------ [현재 참자가 수보다 작게 설정할 수 없습니다.] ------------");
      resultMap.put("result","현재 참자가 수보다 작게 설정할 수 없습니다.");

      return resultMap;
    } else {
      schedule.setScheduleTitle(scheduleDTO.getScheduleTitle());
      schedule.setScheduleContent(scheduleDTO.getScheduleContent());
      schedule.setScheduleMax(scheduleDTO.getScheduleMax());
      schedule.setSchedulePlace(scheduleDTO.getSchedulePlace());
      schedule.setScheduleStartDate(scheduleDTO.getScheduleStartDate());

      scheduleRepository.save(schedule);
    }
    resultMap.put("result",schedule.getScheduleNo().toString());

    return resultMap;
  }


  //일정 참가
  // 일단 스케줄 번호를 넘겨주고 해당 일정을 불러온다.
  // 불러온 일정의 참가정원과 현재 참가인원을 비교한다.
  // 참가인원이 마감되지 않았으면 불러온 일정에 현재 참가인원 수를 추가한다.
  // 참가인원 테이블에 참가자 정보와 일정 번호를 저장한다.
  @Override
  public Integer joinSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO) {
    Optional<Schedule> result = scheduleRepository.findById(scheduleNo);
    Schedule schedule = result.orElseThrow();
    log.info("------------ [해당 일정 정보 조회] ------------");

    userAndScheduleDTO.setScheduleNo(schedule); //일정 번호 전달

    log.info("------------ [참가 가능 여부 확인]------------");
    if (schedule.getScheduleMax() > schedule.getScheduleParticipants()) {
      userAndScheduleService.addParticipant(userAndScheduleDTO);
      log.info("------------ [참가인원 정보 추가]------------");

      Integer participants = schedule.getScheduleParticipants();
      schedule.setScheduleParticipants(participants + 1);
      scheduleRepository.save(schedule);
      log.info("------------ [참가인원 추가 완료]------------");

    } else {
      log.info("인원이 마감되었습니다.");
    }

    return schedule.getScheduleParticipants();
  }


  //일정 참가 취소
  // 회원 번호와 일정 번호를 받아온다.
  // 해당 일정 참가자 목록에서 해당 회원을 제외한다.
  // 일정 현재 참가인원을 한명 차감한다.
  @Override
  public Integer leaveSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO) {
    Optional<Schedule> result = scheduleRepository.findById(scheduleNo);
    Schedule schedule = result.orElseThrow();
    log.info("------------ [해당 일정 정보 조회] ------------");

    userAndScheduleDTO.setScheduleNo(schedule); //일정 번호 전달
    userAndScheduleService.subtractParticipant(userAndScheduleDTO);

    Integer participants = schedule.getScheduleParticipants();
    schedule.setScheduleParticipants(participants - 1);
    scheduleRepository.save(schedule);
    log.info("------------ [참가 취소 완료] ------------");

    return schedule.getScheduleParticipants();
  }


  //마감되지 않은 일정 조회
  // 마감 기준 : 정원마감, 일정마감
  @Override
  public List<ScheduleDTO> getOngoingSchedules(Long scheduleNo) {
     List<Schedule> schedule = scheduleRepository.findOngoingSchedules(scheduleNo);
     List<ScheduleDTO> scheduleDTOList = modelMapper.map(schedule, List.class);
     return scheduleDTOList;
  }

}
