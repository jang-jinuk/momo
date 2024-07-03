//모임 맴버 관리 기능
package com.momo.momopjt.userAndClub;

import javax.transaction.Transactional;

import com.momo.momopjt.club.Club;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


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
  public void approveJoin(Long id) {
    Optional<UserAndClub> result = userAndClubRepository.findById(id);
    UserAndClub userAndClub = result.orElseThrow();
    //가입 승인 날짜 추가
    userAndClub.setJoinDate(Instant.now());
    userAndClub.setIsLeader(false); //
    userAndClubRepository.save(userAndClub);
    log.info("-------------가입 승인 완료-------------");
  }

  //모임 탈퇴
  @Override
  public void disband(Long id) {
    userAndClubRepository.deleteById(id);
    log.info("-------------모임 탈퇴 완료-------------");
  }

  //모임 맴버 조회
  @Override
  public List<UserAndClubDTO> readAllMembers(Club clubNo) {
    Boolean isLeader = false; //모임원등급으로 표시
    List<UserAndClub> userAndClubs = userAndClubRepository.findMemberList(clubNo, isLeader);
    log.info("--------------------쿼리실행 완료--------------------");
    List<UserAndClubDTO> userAndClubDTOS = modelMapper.map(userAndClubs, List.class);
    return userAndClubDTOS;
  }

}
