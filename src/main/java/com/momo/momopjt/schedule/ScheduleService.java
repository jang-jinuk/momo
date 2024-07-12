package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
   Long createSchedule(ScheduleDTO scheduleDTO, UserAndScheduleDTO userAndScheduleDTO);
   ScheduleDTO findSchedule(Long scheduleNo);
   Map<String, String> updateSchedule(ScheduleDTO scheduleDTO);
   String joinSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO);
   String leaveSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO);
   List<ScheduleDTO> getOngoingSchedules(Club clubNo);
   List<ScheduleDTO> getEndSchedules(Club clubNo);
   void deleteSchedule(Long scheduleNo);
}
