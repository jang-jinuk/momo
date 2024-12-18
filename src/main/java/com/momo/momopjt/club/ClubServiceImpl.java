package com.momo.momopjt.club;

//모임 CRUD기능 구현

import com.momo.momopjt.alarm.AlarmService;
import com.momo.momopjt.article.Article;
import com.momo.momopjt.article.ArticleRepository;
import com.momo.momopjt.article.ArticleService;
import com.momo.momopjt.photo.Photo;
import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.schedule.ScheduleRepository;
import com.momo.momopjt.schedule.ScheduleService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import com.momo.momopjt.userandclub.UserAndClubDTO;
import com.momo.momopjt.userandclub.UserAndClubRepository;
import com.momo.momopjt.userandclub.UserAndClubService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ClubServiceImpl implements ClubService {


  private final ClubRepository clubRepository;
  private final ScheduleRepository scheduleRepository;
  private final ArticleRepository articleRepository;
  private final UserAndClubRepository userAndClubRepository;

  private final ArticleService articleService;
  private final ScheduleService scheduleService;
  private final AlarmService alarmService;
  private final PhotoService photoService;
  private final UserAndClubService userAndClubService;

  private final ModelMapper modelMapper;
  private final UserService userService;

  //모임 생성
  //모임 생성 후 생성된 모임으로 이동할 수 있게 clubNo 반환
  @Override
  public Long createClub(ClubDTO clubDTO, UserAndClubDTO userAndClubDTO) throws ClubNameException, ClubMaxException { // 0729 YY photoDTO 제거

    Boolean existClubName = clubRepository.existsByClubName(clubDTO.getClubName());

    if (existClubName) {
      throw new ClubNameException();
    }

    if (clubDTO.getClubMax() <= 2) {
      throw new ClubMaxException();
    }

    // 주소를 구,군까지만 저장하는 로직
    String clubArea =clubDTO.getClubArea();
    String[] areaParts = clubArea.split(" ");

    String secondPart = areaParts[1];
    char lastChar = secondPart.charAt(secondPart.length() - 1);

    if (lastChar == '시') {
      clubArea = areaParts[0] + " " + areaParts[1] + " " + areaParts[2];
    } else {
      clubArea = areaParts[0] + " " + areaParts[1];
    }

    clubDTO.setClubArea(clubArea);

    log.info("----------------- [clubphoto UUID : {}]-----------------", clubDTO.getClubPhotoUUID());
    //YY
    Photo photo = photoService.getPhoto(clubDTO.getClubPhotoUUID());

    log.info("----------------- [clubphoto photo get uuid {}]-----------------",photo.getPhotoUUID());
    clubDTO.setClubPhotoUUID(photo.getPhotoUUID()); // 0729 YY
    Instant instant = Instant.now();
    clubDTO.setClubCreateDate(instant);//모임 생성일 추가
    Club club = modelMapper.map(clubDTO, Club.class);
    Long clubNo = clubRepository.save(club).getClubNo();

    userAndClubDTO.setClubNo(club);//생성된 모임 번호
    userAndClubDTO.setIsLeader(true);//모임장 표시

    userAndClubService.joinClub(userAndClubDTO);//모임장 등록

    // 현재 로그인된 사용자 정보를 얻기
    User user = userService.getCurrentUser();

    // 알림 생성
    alarmService.createClubCreatedAlarm(user, club); // User 객체와 Club 객체를 전달
    log.info("-------- [모임장 생성 알림 이벤트]-------you");


    return clubNo;
  }


  // 특정 모임 조회
  @Override
  public ClubDTO readOneClub(Long clubNo) {
    log.info("----------------- [readOneClub clubNo : {}]-----------------", clubNo);
    Optional<Club> result = clubRepository.findById(clubNo);
    log.trace("result :{}", result);

    Club club = result.orElseThrow();
    log.trace("----------------- [club's photo (before DTO) : {}]-----------------", club.getClubPhotoUUID());

    ClubDTO clubDTO = modelMapper.map(club, ClubDTO.class);
    log.info("clubDTO :{}", result);

    return clubDTO;
  }

  //전체 모임 조회
  //메인 페이지 전체 모임 조희를 위한 기능
  @Override
  public List<ClubDTO> readAllClub() {
    log.info("----------------- [readAllClub()]-----------------");
    List<Club> clubs = clubRepository.findAll();
    List<ClubDTO> clubDTOS = clubs.stream()
        .map(club -> modelMapper.map(club, ClubDTO.class))
        .collect(Collectors.toList());
    return clubDTOS;
  }


  //모임 정보 수정
  //수정 가능 정보 : 사진, 카테고리, 소개글, 지역, 정원
  @Override
  public Boolean updateClub(ClubDTO clubDTO) throws ClubMaxException {
    log.info("----------------- [updateClub()]-----------------");

    Boolean existClubName = clubRepository.existsByClubName(clubDTO.getClubName());

    if (clubDTO.getClubMax() <= 2) {
      throw new ClubMaxException();
    }

    // 주소를 구,군까지만 저장하는 로직
    String clubArea =clubDTO.getClubArea();
    String[] areaParts = clubArea.split(" ");

    String secondPart = areaParts[1];
    char lastChar = secondPart.charAt(secondPart.length() - 1);

    if (lastChar == '시') {
      clubArea = areaParts[0] + " " + areaParts[1] + " " + areaParts[2];
    } else {
      clubArea = areaParts[0] + " " + areaParts[1];
    }

    clubDTO.setClubArea(clubArea);

    //clubNo로 업데이트할 club 불러옴
    Optional<Club> result = clubRepository.findById(clubDTO.getClubNo());
    Club club = result.orElseThrow();

    log.trace("update club photo 조회");
    String newPhotoUUID = clubDTO.getClubPhotoUUID();
    log.trace(newPhotoUUID);
    String oldPhotoUUID = club.getClubPhotoUUID();
    log.trace(oldPhotoUUID);

    if(newPhotoUUID != null && newPhotoUUID.length()>0){
      if(! newPhotoUUID.equals(oldPhotoUUID) ){
        log.trace("update club photo 실행, {} -> {}",oldPhotoUUID,newPhotoUUID);
        clubDTO.setClubPhotoUUID(newPhotoUUID);
        //photo 변경사항이 있을 떄 업데이트 실행
      }
    } else {
      log.trace("update club photo 실행 X, old : {}, new : {}", oldPhotoUUID, newPhotoUUID);
      clubDTO.setClubPhotoUUID(oldPhotoUUID);
    }


    //현재 모임의 인원수 확인
    int membersCount = userAndClubService.countMembers(club);

    //현재 모임 인원수보다 적게는 수정하지 못함
    if (membersCount > clubDTO.getClubMax()) {
      return false;
    }

    club.change(clubDTO.getClubPhotoUUID(), clubDTO.getClubCategory(), clubDTO.getClubContent(),
        clubDTO.getClubArea(), clubDTO.getClubMax()
    );
    clubRepository.save(club);

    return true;
  }

  //모임 해산
  //일정, 게시글, 사진, 인원, 댓글 삭제 필요
  //TODO 대표사진, 일정 사진, 후기 사진 삭제 로직 추가 필요 JW
  @Override
  public void deleteClub(Long clubNo) {
    Club club = new Club();
    club.setClubNo(clubNo);

    //해당 모임의 모든 일정 댓글 삭제
    List<Schedule> scheduleList = scheduleRepository.findSchedulesByClub(club);
    for (Schedule schedule : scheduleList) {
      scheduleService.deleteSchedule(schedule.getScheduleNo());
    }

    //해당 모임의 모든 후기글 삭제
    List<Article> articleList = articleRepository.findByClubNo(club);
    for (Article article : articleList) {
      articleService.deleteArticle(article.getArticleNo());
    }
    log.info("------------ [모든 후기글 삭제] ------------");

    //해당 모임 맴버 전체 삭제
    userAndClubService.deleteAllMembers(clubNo);

    //해당 모임 대표사진 조회
    Optional<Club> result = clubRepository.findById(clubNo);
    club = result.orElseThrow();

    //0729 YY
    String clubPhotoStr = club.getClubPhotoUUID();
    log.info(clubPhotoStr);

    // 모임 삭제
    clubRepository.deleteById(clubNo);

    //해당 모임 대표사진 삭제
    if (!clubPhotoStr.equals("ClubDefaultPhoto")) {

      int lastDotIndex = clubPhotoStr.lastIndexOf('.');
      String clubPhotoUUID = (lastDotIndex != -1) ? clubPhotoStr.substring(0, lastDotIndex) : "";

      photoService.deletePhoto(clubPhotoUUID);
    }

    // 현재 로그인된 사용자 정보를 얻기
    User user = userService.getCurrentUser();

    //모임 삭제 이벤트
    alarmService.createClubDeletedAlarm(user,club);
    log.info("-------- [모임 삭제시 모임장에게 알람 이벤트 전송]-------you");
  }

  // 나의 모임 조회
  @Override
  public List<ClubDTO> readMyClubs(User userNo) {
    List<Club> clubs = userAndClubRepository.findMyClubs(userNo);

    List<ClubDTO> clubDTOS = new ArrayList<>();

    for (Club club : clubs) {
      clubDTOS.add(readOneClub(club.getClubNo()));
    }

    return clubDTOS;
  }
}