# Momo  Project

---

### 모두의 모임 - 하나의 접점, 무한한 연결



## Outline
**소개**

- 관심사별 모임을 통해 새로운 인연을 만들고 외로움을 해소하는 온오프라인 커뮤니티 플랫폼 입니다.

**프로젝트 기간** 

- 개발 기간 : 2024-06-27 ~ 2024-08-07


## ERD
<img width="800" alt="모두의 모임 ERD" src="https://github.com/user-attachments/assets/6423c6d9-42a0-45cc-bb39-6f866bc08a63">

---

## Skill Stack

[//]: # (<p align="center">)

[//]: # (  <a href="https://skillicons.dev">)

[//]: # (    <img src="https://skillicons.dev/icons?i=java,spring,gradle,mysql,html,css,js,bootstrap&theme=light" />)

[//]: # (    <img src="https://skillicons.dev/icons?i=aws,ubuntu,nginx,idea,git,github,notion,discord&theme=light" />)

[//]: # (  </a>)

[//]: # (</p>)
#### BackEnd
<img src="https://img.shields.io/badge/java 11-00000F?style=for-the-badge&logo=openjdk&logoColor=white">
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/springboot 2.7.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">

#### FrontEnd
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
<img src="https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white">
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white">
<img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">

#### DataBase
<img src="https://img.shields.io/badge/mariadb 11.5.2-003545?style=for-the-badge&logo=mariadb&logoColor=white">

#### Infra
<img src="https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">
<img src="https://img.shields.io/badge/amazon ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">

---

## 🗂️ Foldering

```
📂momopjt_
          |_📂alaram_
          |          |_📋Alarm.java
          |          |_📋AlarmController.java
          |          |_📋AlarmDTO.java
          |          |_📋AlarmRepository.java
          |          |_📋AlarmService.java
          |          |_📋AlarmServiceImpl.java
          |          |_📋AlarmType.java
          |
          |_📂article_
          |           |_📋Article.java
          |           |_📋ArticleController.java
          |           |_📋ArticleDTO.java
          |           |_📋ArticleRepository.java
          |           |_📋ArticleService.java
          |           |_📋ArticleServiceImpl.java
          |
          |_📂club_
          |         |_📋Club.java
          |         |_📋ClubController.java
          |         |_📋ClubDTO.java
          |         |_📋ClubRepository.java
          |         |_📋ClubService.java
          |         |_📋ClubServiceImpl.java
          |_📂file_
          |        |_📋FileController.java
          |        |_📋UploadFileDTO.java
          |        |_📋UploadResultDTO.java
          |
          |_📂global_
          |          |_📂config_
          |          |          |_📋AppConfig.java
          |          |          |_📋CustomServletConfig.java
          |          |          |_📋EmailConfig.java
          |          |          |_📋EmailConfig.java
          |          |          |_📋RootConfig.java
          |          |          |_📋SecurityConfig.java
          |          |
          |          |_📂security_
          |          |            |_📂handler_
          |          |            |           |_📋CustomSocialLoginSuccessHandler.java
          |          |            |           |_📋GlobalExceptionHandler.java
          |          |            |
          |          |            |_📋CustomOAuth2UserService.java
          |          |            |_📋CustomUserDetailService.java
          |          |
          |          |__📋FileCheckTask.java
          |          |__📋UerPhotoInterceptor.java           
          |        
          |_📂home_
          |        |_📋HomeController.java
          |
          |_📂photo_
          |         |_📋Photo.java
          |         |_📋Photoontroller.java
          |         |_📋PhotoDTO.java
          |         |_📋PhotoRepository.java
          |         |_📋PhotoResolveService.java
          |         |_📋PhotoService.java
          |         |_📋PhotoServiceImpl.java
          |
          |_📂reply_
          |         |_📋Reply.java
          |         |_📋ReplyController.java
          |         |_📋ReplyDTO.java
          |         |_📋ReplyRepository.java
          |         |_📋ReplyService.java
          |         |_📋ReplyServiceImpl.java
          |
          |_📂report_
          |          |_📋AdminController.java
          |          |_📋Report.jav
          |          |_📋ReportController.java
          |          |_📋ReportDTO.java
          |          |_📋ReportRepository.java
          |          |_📋ReportService.java
          |          |_📋ReportServiceImpl.java
          |         
          |_📂schedule_
          |            |_📋Schedule.jav
          |            |_📋ScheduleController.java
          |            |_📋ScheduleDTO.java
          |            |_📋ScheduleRepository.java
          |            |_📋ScheduleService.java
          |            |_📋ScheduleServiceImpl.java
          |                   
          |_📂user_
          |        |_📂find_
          |        |        |_📋EmailService.java
          |        |        |_📋FindController.java
          |        |        |_📋FindPasswordRequest.java
          |        |        |_📋FindUserIdRequest.java
          |        |        |_📋ResetPasswordRequest.java
          |        |
          |        |_📋User.java
          |        |_📋UserController.java
          |        |_📋UserDTO.java
          |        |_📋UserRepository.java
          |        |_📋UserRole.java
          |        |_📋UserSecurityDTO.java
          |        |_📋UserService.java
          |        |_📋UserServiceImpl.java
          |        
          |_📂userandclub_
          |               |_📋UserAndClub.jav
          |               |_📋UserAndClubController.java
          |               |_📋UserAndClubDTO.java
          |               |_📋UserAndClubRepository.java
          |               |_📋UserAndClubService.java
          |               |_📋UserAndClubServiceImpl.java
          |
          |_📂userandschedule_
                              |_📋UserAndSchedule.jav
                              |_📋UserAndScheduleController.java
                              |_📋UserAndScheduleDTO.java
                              |_📋UserAndScheduleRepository.java
                              |_📋UserAndScheduleService.java
                              |_📋UserAndScheduleServiceImpl.java
```