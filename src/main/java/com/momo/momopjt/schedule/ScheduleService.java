package com.momo.momopjt.schedule;

import com.momo.momopjt.userAndSchedule.UserAndScheduleDTO;

import java.util.List;

public interface ScheduleService {
   Long createSchedule(ScheduleDTO scheduleDTO, UserAndScheduleDTO uandScheduleDTO);
   List<ScheduleDTO> getOngoingSchedules();
}
