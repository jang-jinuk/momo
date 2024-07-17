package com.momo.momopjt.club;

//모임 CRUD기능 구현

import com.momo.momopjt.photo.Photo;
import com.momo.momopjt.photo.PhotoDTO;
import com.momo.momopjt.photo.PhotoService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.momo.momopjt.user.User;
import com.momo.momopjt.userandclub.UserAndClubDTO;
import com.momo.momopjt.userandclub.UserAndClubRepository;
import com.momo.momopjt.userandclub.UserAndClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ClubServiceImpl implements ClubService {

  private final ClubRepository clubRepository;
  private final UserAndClubRepository userAndClubRepository;
  private final PhotoService photoService;
  private final ModelMapper modelMapper;
  private final UserAndClubService userAndClubService;

  //모임 생성
  //모임 생성 후 생성된 모임으로 이동할 수 있게 clubNo 반환
  @Override
  public Long createClub(ClubDTO clubDTO, PhotoDTO photoDTO, UserAndClubDTO userAndClubDTO) {

    Photo photo = photoService.savePhoto(photoDTO);
    clubDTO.setPhotoUUID(photo);
    Instant instant = Instant.now();
    clubDTO.setClubCreateDate(instant);//모임 생성일 추가
    Club club = modelMapper.map(clubDTO, Club.class);
    Long clubNo = clubRepository.save(club).getClubNo();//TODO club create error

    userAndClubDTO.setClubNo(club);//생성된 모임 번호
    userAndClubDTO.setIsLeader(true);//모임장 표시

    userAndClubService.joinClub(userAndClubDTO);//모임장 등록

    return clubNo;
  }

  // 특정 모임 조회
  @Override
  @Transactional
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
  @Override
  public void disbandClub(Long clubNo) {
    //해당 모임 맴버 전체 삭제
    userAndClubService.deleteAllMembers(clubNo);
    //해당 모임 대표사진 조회
    Optional<Club> result = clubRepository.findById(clubNo);
    Club club = result.orElseThrow();
    Photo photo = club.getClubPhoto();
    String clubPhoto = photo.getPhotoUUID();
    log.info(clubPhoto);
    
    // 모임 해산
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