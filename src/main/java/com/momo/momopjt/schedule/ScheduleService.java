package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;

import java.util.List;

public interface ScheduleService {

   Long createSchedule(ScheduleDTO scheduleDTO, UserAndScheduleDTO userAndScheduleDTO) throws ScheduleMaxException, ScheduleDateException;

   ScheduleDTO readOneSchedule(Long scheduleNo);

   Boolean updateSchedule(ScheduleDTO scheduleDTO);

   String joinSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO);

   String leaveSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO);

   List<ScheduleDTO> readOngoingSchedules(Club clubNo);

   List<ScheduleDTO> readEndSchedules(Club clubNo);

   void deleteSchedule(Long scheduleNo);

   Boolean isScheduleFull(Long scheduleNo);

   List<ScheduleDTO> readMyParticipatedSchedules(Club clubNo, User userNo);

   class ScheduleMaxException extends Exception {}

   class ScheduleDateException extends Exception {}
}
