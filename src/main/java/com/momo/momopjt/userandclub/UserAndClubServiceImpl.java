package com.momo.momopjt.userandclub;

//모임 맴버 관리 기능

import com.momo.momopjt.club.Club;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.momo.momopjt.club.ClubRepository;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.schedule.ScheduleService;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import com.momo.momopjt.userandschedule.UserAndScheduleRepository;
import com.momo.momopjt.userandschedule.UserAndScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;



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

  private final ModelMapper modelMapper;


  //모임 가입 신청
  @Override
  public void joinClub(UserAndClubDTO userAndClubDTO) {
    UserAndClub userAndClub = modelMapper.map(userAndClubDTO, UserAndClub.class);
    userAndClubRepository.save(userAndClub);
  }

  //모입 가입 승인
  @Override
  public Boolean approveJoin(UserAndClubDTO userAndClubDTO) {
    Optional<Club> result = clubRepository.findById(userAndClubDTO.getClubNo().getClubNo());
    Club club = result.orElseThrow();

     int countMembers = countMembers(userAndClubDTO.getClubNo());

    if (club.getClubMax() == countMembers) { //모임 정원을 넘는지 확인
      return false;
    }

    UserAndClub userAndClub = userAndClubRepository.findByUserNoAndClubNo(
        userAndClubDTO.getUserNo(), userAndClubDTO.getClubNo());

    //가입 승인 날짜 추가
    userAndClub.setJoinDate(Instant.now());
    userAndClub.setIsLeader(false); //모임원 등록
    userAndClubRepository.save(userAndClub);
    log.info("-------------가입 승인 완료-------------");

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
}