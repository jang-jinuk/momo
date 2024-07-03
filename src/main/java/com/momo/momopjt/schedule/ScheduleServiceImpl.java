package com.momo.momopjt.schedule;

import com.momo.momopjt.userAndSchedule.UserAndScheduleDTO;
import com.momo.momopjt.userAndSchedule.UserAndScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

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
    //일정 주체자 등록
    userAndScheduleService.createScheduleMaster(userAndScheduleDTO);

    Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);

    return scheduleRepository.save(schedule).getScheduleNo();
  }
}
