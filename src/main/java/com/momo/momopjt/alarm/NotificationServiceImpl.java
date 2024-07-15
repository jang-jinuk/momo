package com.momo.momopjt.alarm;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
@Service
@Log4j2
public class NotificationServiceImpl implements NotificationService {

//  private final SimpMessagingTemplate messagingTemplate;

//  @Autowired
//  public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
//    this.messagingTemplate = messagingTemplate;
//  }

  @Override
  public void send(AlarmDTO alarmDTO) {
//    messagingTemplate.convertAndSend("/topic/notifications", alarmDTO);
    log.info("-------- [send]-------you");
  }
}