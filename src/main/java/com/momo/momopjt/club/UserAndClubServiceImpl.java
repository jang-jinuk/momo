package com.momo.momopjt.club;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;




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
}
