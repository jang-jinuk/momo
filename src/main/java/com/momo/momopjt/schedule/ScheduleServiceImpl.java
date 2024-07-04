package com.momo.momopjt.schedule;


import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import com.momo.momopjt.userandschedule.UserAndScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
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
    log.info("------------ [07-03-18:58:03]----------jinuk");
    
    //일정 번호 전달
    userAndScheduleDTO.setScheduleNo(schedule);
    log.info("------------ [07-03-18:57:54]----------jinuk");

    //일정 주체자 등록
    userAndScheduleService.addParticipant(userAndScheduleDTO);
    log.info("------------ [07-03-18:58:08]----------jinuk");

    return scheduleNo;
  }

  @Override
  public ScheduleDTO findSchedule(Long scheduleNo) {
    Optional<Schedule> result = scheduleRepository.findById(scheduleNo);
    Schedule schedule = result.orElseThrow();
    return modelMapper.map(schedule, ScheduleDTO.class);
  }


  // 일단 스케줄 번호를 넘겨주고 해당 일정을 불러온다.
  // 불러온 일정의 참가정원과 현재 참가인원을 비교한다.
  // 참가인원이 마감되지 않았으면 불러온 일정에 현재 참가인원 수를 추가한다.
  // 참가인원 테이블에 참가자 정보와 일정 번호를 저장한다.
  @Override
  public void joinSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO) {

    Optional<Schedule> result = scheduleRepository.findById(scheduleNo);
    Schedule schedule = result.orElseThrow();

    //일정 번호 전달
    userAndScheduleDTO.setScheduleNo(schedule);

    log.info("------------ [참가 가능 여부 확인]------------");
    if (schedule.getScheduleMax() > schedule.getScheduleParticipants()) {
      userAndScheduleService.addParticipant(userAndScheduleDTO);
      log.info("------------ [참가인원 정보 추가]------------");
      Integer participants = schedule.getScheduleParticipants();
      participants++;
      schedule.setScheduleParticipants(participants);
      scheduleRepository.save(schedule);
      log.info("------------ [참가인원 추가]------------");
    } else {
      log.info("인원이 마감되었습니다.");
    }
  }


  @Override
  public List<ScheduleDTO> getOngoingSchedules() {
    return List.of();
  }

}
