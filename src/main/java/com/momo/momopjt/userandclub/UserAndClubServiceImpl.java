package com.momo.momopjt.userandclub;

//모임 맴버 관리 기능

import com.momo.momopjt.club.Club;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;



@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class UserAndClubServiceImpl implements UserAndClubService {

  private final UserAndClubRepository userAndClubRepository;
  private final ModelMapper modelMapper;


  //모임 가입 신청
  @Override
  public void joinClub(UserAndClubDTO userAndClubDTO) {
    UserAndClub userAndClub = modelMapper.map(userAndClubDTO, UserAndClub.class);
    userAndClubRepository.save(userAndClub);
  }

  //모입 가입 승인
  @Override
  public void approveJoin(UserAndClubDTO userAndClubDTO) {
    UserAndClub userAndClub = userAndClubRepository.findMember(userAndClubDTO.getClubNo(),userAndClubDTO.getUserNo());
    //가입 승인 날짜 추가
    userAndClub.setJoinDate(Instant.now());
    userAndClub.setIsLeader(false); //모임원 등록
    userAndClubRepository.save(userAndClub);
    log.info("-------------가입 승인 완료-------------");
  }

  //모임 탈퇴
  @Override
  public void leaveClub(UserAndClubDTO userAndClubDTO) {
    userAndClubRepository.deleteClubMember(userAndClubDTO.getClubNo(),userAndClubDTO.getUserNo());
    log.info("-------------모임 탈퇴 완료-------------");
  }

  //모임 맴버 조회
  @Override
  public List<UserAndClubDTO> readAllMembers(Club clubNo) {
    Boolean isLeader = false; //모임원등급으로 표시
    List<UserAndClub> userAndClubs = userAndClubRepository.findMemberList(clubNo, isLeader);
    log.info("--------------------쿼리실행 완료--------------------");
    List<UserAndClubDTO> userAndClubDTOList = userAndClubs.stream()
        .map(userAndClub -> modelMapper.map(userAndClub, UserAndClubDTO.class))
        .collect(Collectors.toList());
    return userAndClubDTOList;
  }

  //모임 가입 신청 내역 조회
  @Override
  public List<UserAndClubDTO> readAllJoinList(Club clubNo) {
    List<UserAndClub> userAndClubs = userAndClubRepository.findJoinList(clubNo);
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
    userAndClubRepository.deleteClubMembers(club);
  }

  //모임 맴버 확인
  @Override
  public int isMember(UserAndClubDTO userAndClubDTO) {
    UserAndClub userAndClub = userAndClubRepository.findMember(userAndClubDTO.getClubNo(), userAndClubDTO.getUserNo());

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
    int count = userAndClubRepository.countMembers(clubNo);
    return count;
  }
}