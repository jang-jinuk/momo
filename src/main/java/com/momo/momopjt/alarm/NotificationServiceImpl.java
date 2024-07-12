package com.momo.momopjt.alarm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

  private final SimpMessagingTemplate messagingTemplate;

  @Autowired
  public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @Override
  public void send(AlarmDTO alarmDTO) {
    messagingTemplate.convertAndSend("/topic/notifications", alarmDTO);
  }
}