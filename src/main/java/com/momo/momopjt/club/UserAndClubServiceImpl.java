//모임 맴버 관리 기능
package com.momo.momopjt.club;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    userAndClubRepository.save(userAndClub);
    log.info("-------------가입 승인 완료-------------");
  }

  //모임 탈퇴
  @Override
  public void disband(Long id) {
    userAndClubRepository.deleteById(id);
    log.info("-------------모임 탈퇴 완료-------------");
  }

}
