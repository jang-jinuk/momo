package com.momo.momopjt.userandclub;

//모임 맴버 관리 기능

import com.momo.momopjt.alarm.AlarmService;
import com.momo.momopjt.club.Club;
import com.momo.momopjt.club.ClubRepository;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.schedule.ScheduleService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserService;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import com.momo.momopjt.userandschedule.UserAndScheduleRepository;
import com.momo.momopjt.userandschedule.UserAndScheduleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class UserAndClubServiceImpl implements UserAndClubService {

  private final ClubRepository clubRepository;
  private final UserAndClubRepository userAndClubRepository;
  private final UserAndScheduleRepository userAndScheduleRepository;

  private final ScheduleService scheduleService;
  private final UserAndScheduleService userAndScheduleService;
  private final AlarmService alarmService; // 알림 서비스
  private final UserRepository userRepository;

  private final ModelMapper modelMapper;
  private final UserService userService;


  //모임 가입 신청
  @Override
  public void joinClub(UserAndClubDTO userAndClubDTO) {
    UserAndClub userAndClub = modelMapper.map(userAndClubDTO, UserAndClub.class);
    userAndClubRepository.save(userAndClub);
  }

  @Override
  public Boolean approveJoin(UserAndClubDTO userAndClubDTO) {
    Optional<Club> result = clubRepository.findById(userAndClubDTO.getClubNo().getClubNo());
    Club club = result.orElseThrow();

    int countMembers = countMembers(userAndClubDTO.getClubNo());

    if (club.getClubMax() == countMembers) {
      return false;
    }

    UserAndClub userAndClub = userAndClubRepository.findByUserNoAndClubNo(
        userAndClubDTO.getUserNo(), userAndClubDTO.getClubNo());

    // 가입 승인 날짜 추가
    userAndClub.setJoinDate(Instant.now());
    userAndClub.setIsLeader(false); // 모임원 등록
    userAndClubRepository.save(userAndClub);
    log.info("-------------가입 승인 완료-------------");

    // User 객체와 Club 객체가 null이 아닌지 확인
    User user = userAndClub.getUserNo();
    if (user == null || club == null) {
      throw new IllegalArgumentException("User or Club cannot be null");
    }
    log.info("----------------모임 가입 신청 알림 이벤트 --------------");
    alarmService.createJoinApprovalAlarm(user, club);

    return true;
  }
  //모임 탈퇴
  //등록한 일정, 게시글, 사진과 참석한 일정에서 삭제
  @Override
  public void leaveClub(UserAndClubDTO userAndClubDTO) {

    //참가했던 일정 목록에서 제거
    List<Schedule> participatedScheduleList = userAndScheduleRepository.findSchedulesParticipatedByUser(
        userAndClubDTO.getUserNo());

    UserAndScheduleDTO userAndScheduleDTO = new UserAndScheduleDTO();
    userAndScheduleDTO.setUserNo(userAndClubDTO.getUserNo());

    for (Schedule schedule : participatedScheduleList) {
      userAndScheduleDTO.setScheduleNo(schedule);
      userAndScheduleService.subtractParticipant(userAndScheduleDTO);
    }

    //회원이 주최한 일정 삭제
    List<Schedule> hostedScheduleList = userAndScheduleRepository.findSchedulesHostedByUser(userAndClubDTO.getUserNo());

    for (Schedule schedule : hostedScheduleList) {
      scheduleService.deleteSchedule(schedule.getScheduleNo());
    }

    userAndClubRepository.deleteByClubNoAndUserNo(userAndClubDTO.getClubNo(),userAndClubDTO.getUserNo());
    log.info("-------------모임 탈퇴 완료-------------");

    // User 객체와 Club 객체를 가져옴
    Optional<User> userOptional = userRepository.findById(userAndClubDTO.getUserNo().getUserNo());
    Optional<Club> clubOptional = clubRepository.findById(userAndClubDTO.getClubNo().getClubNo());

    User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User not found"));
    Club club = clubOptional.orElseThrow(() -> new IllegalArgumentException("Club not found"));

    // 모임 탈퇴 알림 이벤트 생성
    log.info("----------------모임 탈퇴 알림 이벤트 --------------");
    alarmService.createLeaveAlarm(user, club);

  }

  //모임 맴버 조회
  @Override
  public List<UserAndClubDTO> readAllMembers(Club clubNo) {
    Boolean isLeader = false; //모임원등급으로 표시
    List<UserAndClub> userAndClubs = userAndClubRepository.findMemberByClubNoAndIsLeader(clubNo, isLeader);
    log.info("--------------------쿼리실행 완료--------------------");
    List<UserAndClubDTO> userAndClubDTOList = userAndClubs.stream()
        .sorted(Comparator.comparing(UserAndClub::getJoinDate).reversed()) //최신 가입 순으로 정렬
        .map(userAndClub -> modelMapper.map(userAndClub, UserAndClubDTO.class))
        .collect(Collectors.toList());
    return userAndClubDTOList;
  }

  //모임 가입 신청 내역 조회
  @Override
  public List<UserAndClubDTO> readAllJoinList(Club clubNo) {
    List<UserAndClub> userAndClubs = userAndClubRepository.findByClubNoAndJoinDateIsNull(clubNo);
    List<UserAndClubDTO> joinList = userAndClubs.stream()
        .map(userAndClub -> modelMapper.map(userAndClub, UserAndClubDTO.class))
        .collect(Collectors.toList());
    return joinList;
  }

  // 해당 모임 맴버 전체 삭제
  @Override
  public void deleteAllMembers(Long clubNo) {
    Club club = new Club();
    club.setClubNo(clubNo);
    userAndClubRepository.deleteByClubNo(club);
  }

  //모임 맴버 확인
  @Override
  public int isMember(UserAndClubDTO userAndClubDTO) {
    UserAndClub userAndClub = userAndClubRepository.findByUserNoAndClubNo(
        userAndClubDTO.getUserNo(), userAndClubDTO.getClubNo());

    if (userAndClub == null) {
      return 0; //모임 미가입자
    } else if (userAndClub.getIsLeader() == null) {
      return 1; //모임 가입 신청자
    } else if (userAndClub.getIsLeader()) {
      return 2; //모임장일 경우
    }
    return 3; //모임원일 경우
  }

  //모임맴버 총 인원 확인
  @Override
  public int countMembers(Club clubNo) {
    int count = userAndClubRepository.countByClubNoAndJoinDateIsNotNull(clubNo);
    return count;
  }

  //모임장 조회
  @Override
  public UserAndClubDTO findLeader(Club clubNo) {
    List<UserAndClub> userAndClub = userAndClubRepository.findMemberByClubNoAndIsLeader(clubNo,true);
    UserAndClubDTO userAndClubDTO = modelMapper.map(userAndClub.get(0), UserAndClubDTO.class);
    return userAndClubDTO;
  }

  //모임 즐겨찾기 여부 조회
  @Override
  public List<Club> findMyWishClubs(User user) {
    log.info("...... [U and C ServiceImpl/findMyWishClubs/Start]..........KSW");
    // userNo로 isWish 가 Y인 clubNo 들을 조회해서 Long 타입 List 로 받음
    List<Long> clubNumbers = userAndClubRepository.findClubNumbersByUser(user);
    log.info("User 의 즐겨찾기 클럽 번호 목록: {}", clubNumbers);

    if (clubNumbers.isEmpty()) {
      return Collections.emptyList();
    }
    List<Club> clubs = clubRepository.findAllById(clubNumbers);
    log.info("즐겨찾기 클럽 목록: {}", clubs);
    return clubs;
  }

  //모임 즐겨찾기/해제 todo 0726 시간 남으시면 코드 한번 봐주세요 SW
  @Override
  public void updateWishClub(UserAndClubDTO userAndClubDTO) {
    log.info("...... [U and C ServiceImpl/updateWishClub/Start]..........KSW");
    UserAndClub updateWish = userAndClubRepository.findByUserNoAndClubNo(
    userAndClubDTO.getUserNo(), userAndClubDTO.getClubNo()); //현재 클럽과 유저를 받아와서 주입
    //isWish 값 변경
    if(userAndClubDTO.getIsWish() == 'N'){
      updateWish.setIsWish('Y');
    } else if(userAndClubDTO.getIsWish() == 'Y'){
      updateWish.setIsWish('N');
    }
    userAndClubRepository.save(updateWish);
  }
}