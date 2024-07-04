package com.momo.momopjt.userandschedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class UserAndScheduleServiceImpl implements UserAndScheduleService {

  private final UserAndScheduleRepository userAndScheduleRepository;
  private final ModelMapper modelMapper;

  //참가 인원 추가
  @Override
  public void addParticipant(UserAndScheduleDTO userAndScheduleDTO) {
    userAndScheduleRepository.save(modelMapper.map(userAndScheduleDTO, UserAndSchedule.class));
  }

  //참가 인원 제거
  @Override
  public void subtractParticipant(UserAndScheduleDTO userAndScheduleDTO) {
    userAndScheduleRepository.deleteParticipant(userAndScheduleDTO.getScheduleNo(), userAndScheduleDTO.getUserNo());
    log.info("------------ [참가 인원 제거 완료] ------------");
  }

}
