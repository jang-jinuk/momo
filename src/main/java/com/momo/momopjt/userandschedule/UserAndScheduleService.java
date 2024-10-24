package com.momo.momopjt.userandschedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.user.UserDTO;

import java.util.List;

public interface UserAndScheduleService {

  void addParticipant(UserAndScheduleDTO userAndScheduleDTO);

  void subtractParticipant(UserAndScheduleDTO userAndScheduleDTO);

  void deleteParticipant(Schedule scheduleNo);

  void deleteParticipantsByClub(Club clubNo);

  List<UserDTO> readAllParticipants(Schedule scheduleNo);

  int isParticipate(UserAndScheduleDTO userAndScheduleDTO);

}
