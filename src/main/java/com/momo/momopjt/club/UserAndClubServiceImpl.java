package com.momo.momopjt.club;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;


@Service
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
  }
}
