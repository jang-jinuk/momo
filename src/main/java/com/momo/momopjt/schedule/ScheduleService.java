package com.momo.momopjt.schedule;

import com.momo.momopjt.userandschedule.UserAndScheduleDTO;

import java.util.List;

public interface ScheduleService {
   Long createSchedule(ScheduleDTO scheduleDTO, UserAndScheduleDTO userAndScheduleDTO);
   ScheduleDTO findSchedule(Long scheduleNo);
   Long updateSchedule(ScheduleDTO scheduleDTO);
   Integer joinSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO);
   Integer leaveSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO);
   List<ScheduleDTO> getOngoingSchedules();
}
