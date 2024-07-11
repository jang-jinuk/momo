package com.momo.momopjt.userandschedule;

import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserDTO;
import com.momo.momopjt.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class UserAndScheduleServiceImpl implements UserAndScheduleService {

  private final UserAndScheduleRepository userAndScheduleRepository;
  private final UserRepository userRepository;
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

  @Override
  public void deleteParticipant(Schedule scheduleNo) {
    userAndScheduleRepository.deleteAllParticipant(scheduleNo);
    log.info("------------ [참가 인원 전원 삭제 완료] ------------");
  }

  @Override
  public List<UserDTO> readAllParticipants(Schedule scheduleNo) {
    List<UserAndSchedule> userAndSchedules = userAndScheduleRepository.findByAllParticipants(scheduleNo);
    List<User> users = new ArrayList<>();
    List<UserDTO> userDTOs = new ArrayList<>();
    for (UserAndSchedule userAndSchedule : userAndSchedules) {
      Optional<User> result = userRepository.findById(userAndSchedule.getUserNo().getUserNo());
      result.orElseThrow();
      UserDTO userDTO = modelMapper.map(result, UserDTO.class);
      userDTOs.add(userDTO);
    }
    return userDTOs;
  }
}
