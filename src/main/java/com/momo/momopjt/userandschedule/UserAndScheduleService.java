package com.momo.momopjt.userandschedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.schedule.Schedule;

public interface UserAndScheduleService {
  void addParticipant(UserAndScheduleDTO userAndScheduleDTO);
  void subtractParticipant(UserAndScheduleDTO userAndScheduleDTO);
  void deleteParticipant(Schedule scheduleNo);
}
