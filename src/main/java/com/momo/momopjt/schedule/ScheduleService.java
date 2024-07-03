package com.momo.momopjt.schedule;

import com.momo.momopjt.userAndSchedule.UserAndScheduleDTO;

public interface ScheduleService {
   Long createSchedule(ScheduleDTO scheduleDTO, UserAndScheduleDTO uandScheduleDTO);
}
