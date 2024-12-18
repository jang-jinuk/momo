package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;

import java.util.List;

public interface ScheduleService {

   Long createSchedule(ScheduleDTO scheduleDTO, UserAndScheduleDTO userAndScheduleDTO) throws MinimumParticipantNotMetException, ScheduleDateException, ScheduleParticipantLimitExceededException;

   ScheduleDTO readOneSchedule(Long scheduleNo);

   Long updateSchedule(ScheduleDTO scheduleDTO) throws MinimumParticipantNotMetException, ScheduleDateException, ScheduleParticipantLimitExceededException;

   String joinSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO);

   String leaveSchedule(Long scheduleNo, UserAndScheduleDTO userAndScheduleDTO);

   List<ScheduleDTO> readOngoingSchedules(Club clubNo);

   List<ScheduleDTO> readEndSchedules(Club clubNo);

   void deleteSchedule(Long scheduleNo);

   Boolean isScheduleFull(Long scheduleNo);

   List<ScheduleDTO> readMyParticipatedSchedules(Club clubNo, User userNo);

   void validateScheduleData(ScheduleDTO scheduleDTO, int participants) throws MinimumParticipantNotMetException, ScheduleDateException, ScheduleParticipantLimitExceededException;

   class MinimumParticipantNotMetException extends Exception {}

   class ScheduleParticipantLimitExceededException extends Exception {}

   class ScheduleDateException extends Exception {}


}
