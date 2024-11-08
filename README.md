# 모두의 모임 - 하나의 접점, 무한한 연결

---

### 



## 📖 Outline
**소개**

- 관심사별 모임을 통해 새로운 인연을 만들고 외로움을 해소하는 온오프라인 커뮤니티 플랫폼 입니다.

**프로젝트 기간** 

- 개발 기간 : 2024-06-27 ~ 2024-08-07

**주요 기능**



## 📊 ERD
<img width="6" alt="모두의 모임 ERD" src="https://github.com/user-attachments/assets/6423c6d9-42a0-45cc-bb39-6f866bc08a63">

## 🛠️ Skill Stack

#### BackEnd
<p>
    <img src="https://github.com/user-attachments/assets/c2c9f5c1-a8a5-4a53-bbf9-c68928a335e3" width="80">
    <img src="https://github.com/user-attachments/assets/987881a9-4ab7-4769-bb5a-1bd3a11e0240" width="80">
    <img src="https://github.com/user-attachments/assets/424eb591-53b9-428a-94f3-7cc303ced130" width="80">
    <img src="https://github.com/user-attachments/assets/14e8ad37-2f7d-4b08-ba69-34e33bcba6a1" width="80">
    <img src="https://github.com/user-attachments/assets/f6ee986c-e307-46a4-8ee6-1760e214aabe" width="80">
</p>

#### FrontEnd
<p>
    <img src="https://github.com/user-attachments/assets/96b4257f-da04-47b6-b0f7-4d52f968ff38" width="80">
    <img src="https://github.com/user-attachments/assets/d882cabc-cdc3-43a8-8492-7451019abfa4" width="80">
    <img src="https://github.com/user-attachments/assets/65466afc-5aa9-41e3-b306-6cd217d826a7" width="80">
    <img src="https://github.com/user-attachments/assets/0c4f2299-5e9b-4e69-b1be-6b3130f70e76" width="80">
</p>

#### Infra
<p>
    <img src="https://github.com/user-attachments/assets/4c6cb25b-f52e-4d1c-bec1-0e7799067588" width="80">
    <img src="https://github.com/user-attachments/assets/720878fa-a25c-4f3c-9117-2fb6ddec0702" width="80">
    <img src="https://github.com/user-attachments/assets/b33e916a-f2dd-4c90-b9f2-48ca9e6fedd0" width="80">
    <img src="https://github.com/user-attachments/assets/b4b42c4f-3852-490a-a520-65ed19f3a09e" width="80">
    <img src="https://github.com/user-attachments/assets/bc61bd2c-b221-465d-b1e8-6e5836fa000d" width="80">
</p>

#### Tools
<p>
    <img src="https://github.com/user-attachments/assets/e86d7071-f0d4-4747-ac70-9bfc2e5eff16" width="80">
    <img src="https://github.com/user-attachments/assets/7d69cc58-1432-49ed-a520-ddccc62b271b" width="80">
</p>


## 🗂️ Package Structure

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

##  Project Architecture