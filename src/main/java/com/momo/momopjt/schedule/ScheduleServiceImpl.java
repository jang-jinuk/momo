package com.momo.momopjt.schedule;

import com.momo.momopjt.Photo.PhotoService;
import com.momo.momopjt.Photo.PhotoServiceImpl;
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
  private final PhotoService photoService;

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
    userAndScheduleService.createScheduleMaster(userAndScheduleDTO);
    log.info("------------ [07-03-18:58:08]----------jinuk");

    return scheduleNo;
  }
}
