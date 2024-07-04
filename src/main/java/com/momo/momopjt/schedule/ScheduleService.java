package com.momo.momopjt.schedule;

import com.momo.momopjt.userandschedule.UserAndScheduleDTO;

import java.util.List;

public interface ScheduleService {
   Long createSchedule(ScheduleDTO scheduleDTO, UserAndScheduleDTO uandScheduleDTO);
   ScheduleDTO findSchedule(Long scheduleNo);
   Integer joinSchedule(Long scheduleNo, UserAndScheduleDTO uandScheduleDTO);
   Integer leaveSchedule(Long scheduleNo, UserAndScheduleDTO uandScheduleDTO);
   List<ScheduleDTO> getOngoingSchedules();
}
