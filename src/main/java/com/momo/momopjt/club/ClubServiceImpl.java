package com.momo.momopjt.club;

//모임 CRUD기능 구현

import com.momo.momopjt.alarm.AlarmService;
import com.momo.momopjt.article.Article;
import com.momo.momopjt.article.ArticleRepository;
import com.momo.momopjt.article.ArticleService;
import com.momo.momopjt.photo.Photo;
import com.momo.momopjt.photo.PhotoDTO;
import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.reply.Reply;
import com.momo.momopjt.reply.ReplyService;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.schedule.ScheduleRepository;
import com.momo.momopjt.schedule.ScheduleService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.userandclub.UserAndClubDTO;
import com.momo.momopjt.userandclub.UserAndClubRepository;
import com.momo.momopjt.userandclub.UserAndClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

  private final UserRepository userRepository;
  private final ClubRepository clubRepository;
  private final ScheduleRepository scheduleRepository;
  private final ArticleRepository articleRepository;
  private final UserAndClubRepository userAndClubRepository;

  private final ArticleService articleService;
  private final ScheduleService scheduleService;
  private final AlarmService alarmService;
  private final ReplyService replyService;
  private final PhotoService photoService;
  private final UserAndClubService userAndClubService;

  private final ModelMapper modelMapper;
  
  //모임 생성
  //모임 생성 후 생성된 모임으로 이동할 수 있게 clubNo 반환
  @Override
  public Long createClub(ClubDTO clubDTO, PhotoDTO photoDTO, UserAndClubDTO userAndClubDTO) throws ClubNameException {

    Boolean existClubName = clubRepository.existsByClubName(clubDTO.getClubName());

    if (existClubName) {
      throw new ClubNameException();
    }

//    Photo photo = photoService.savePhoto(photoDTO);
    Photo photo = photoService.getPhoto("12f2b64b-0fe2-43bf-8b5c-423e3e9aacad");
    clubDTO.setPhotoUUID(photo);
    Instant instant = Instant.now();
    clubDTO.setClubCreateDate(instant);//모임 생성일 추가
    Club club = modelMapper.map(clubDTO, Club.class);
    Long clubNo = clubRepository.save(club).getClubNo();//TODO club create error

    userAndClubDTO.setClubNo(club);//생성된 모임 번호
    userAndClubDTO.setIsLeader(true);//모임장 표시

    userAndClubService.joinClub(userAndClubDTO);//모임장 등록
    // 현재 로그인된 사용자 정보를 얻기
    User user = getCurrentUser(); // 현재 사용자 정보를 반환하는 메서드

    // 알림 생성
    alarmService.createClubCreatedAlarm(user, club); // User 객체와 Club 객체를 전달
    log.info("-------- [모임장 생성 알림 이벤트]-------you");


    return clubNo;
  }
  // 현재 로그인된 사용자 정보를 반환하는 메서드 예제
  private User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof UserDetails) {
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUserId(username); // 사용자 ID로 User 객체를 조회
      }
    }
    return null;
  }

  // 특정 모임 조회
  @Override
//  @Transactional TODO 지울지 확인 0724 YY 
  public ClubDTO readOneClub(Long clubNo) {
    Optional<Club> result = clubRepository.findById(clubNo);

    Club club = result.orElseThrow();

    ClubDTO clubDTO = modelMapper.map(club, ClubDTO.class);

    return clubDTO;
  }

  //전체 모임 조회
  //메인 페이지 전체 모임 조희를 위한 기능
  @Override
  public List<ClubDTO> readAllClub() {
    List<Club> clubs = clubRepository.findAll();
    List<ClubDTO> clubDTOS = clubs.stream()
        .map(club -> modelMapper.map(club, ClubDTO.class))
        .collect(Collectors.toList());
    return clubDTOS;
  }

  //모임 정보 수정
  //수정 가능 정보 : 사진, 카테고리, 소개글, 지역, 정원
  @Override
  public Boolean updateClub(ClubDTO clubDTO, PhotoDTO photoDTO) {
    Photo photo = photoService.savePhoto(photoDTO);
    clubDTO.setPhotoUUID(photo);
    Optional<Club> result = clubRepository.findById(clubDTO.getClubNo());
    Club club = result.orElseThrow();

    //현재 모임의 인원수 확인
    int membersCount = userAndClubService.countMembers(club);

    //현재 모임 인원수보다 적게는 수정하지 못함
    if(membersCount > clubDTO.getClubMax()) {
      return false;
    }

    club.change(clubDTO.getPhotoUUID(), clubDTO.getClubCategory(), clubDTO.getClubContent(),
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
      List<Reply> replyList = replyService.readReplyAllBySchedule(schedule.getScheduleNo());
      for (Reply reply : replyList) {
        replyService.deleteReply(reply.getReplyNo());
      }
    }

    //해당 모임의 모든 일정 삭제
    scheduleService.deleteScheduleByClub(club);

    //해당 모임의 모든 후기글 댓글 삭제
    List<Article> articleList = articleRepository.findByClubNo(club);
    for (Article article : articleList) {
      List<Reply> replyList = replyService.readReplyAllByArticle(article.getArticleNo());
      for (Reply reply : replyList) {
        replyService.deleteReply(reply.getReplyNo());
      }
    }

    //해당 모임의 모든 후기글 삭제
    for (Article article : articleList) {
      articleService.deleteArticle(article.getArticleNo());
    }

    //해당 모임 맴버 전체 삭제
    userAndClubService.deleteAllMembers(clubNo);

    //해당 모임 대표사진 조회
    Optional<Club> result = clubRepository.findById(clubNo);
    club = result.orElseThrow();
    Photo photo = club.getClubPhoto();
    String clubPhoto = photo.getPhotoUUID();
    log.info(clubPhoto);

    // 모임 삭제
    clubRepository.deleteById(clubNo);

    //해당 모임 대표사진 삭제
    if(!clubPhoto.equals("default.jpg")) {//TODO 나중에 실제 디폴트 사진으로 변경
      photoService.deletePhoto(clubPhoto);
    }
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