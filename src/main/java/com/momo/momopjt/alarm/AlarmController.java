//package com.momo.momopjt.alarm;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/alarm")
//public class AlarmController {
//
//  @Autowired
//  private AlarmService alarmService;
//
//  @PostMapping("/sendNotification")
//  public String sendNotification(@RequestBody AlarmDTO alarmDTO) {
//    alarmService.sendNotification(alarmDTO);
//    return "Notification sent!";
//  }
//}