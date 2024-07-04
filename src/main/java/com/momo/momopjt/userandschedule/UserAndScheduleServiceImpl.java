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

  @Override
  public void addParticipant(UserAndScheduleDTO userAndScheduleDTO) {
    userAndScheduleRepository.save(modelMapper.map(userAndScheduleDTO, UserAndSchedule.class));
  }
}
