package com.momo.momopjt.userandschedule;

import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.user.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class UserAndScheduleTests {

  @Autowired
  UserAndScheduleService userAndScheduleService;

  @Test
  public void readAllParticipantsTest() {
    //Given
    Schedule schedule = new Schedule();
    schedule.setScheduleNo(1L);
    //When
     List<UserDTO> userDTOList= userAndScheduleService.readAllParticipants(schedule);
    //Then
    for (UserDTO userDTO : userDTOList) {
      log.info(userDTO.getUserNickname());
    }
  }

}
