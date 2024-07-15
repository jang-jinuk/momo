package com.momo.momopjt.club;

//모임 CRUD기능 구현

import com.momo.momopjt.photo.Photo;
import com.momo.momopjt.photo.PhotoDTO;
import com.momo.momopjt.photo.PhotoService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import com.momo.momopjt.userandclub.UserAndClubDTO;
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
  private final PhotoService photoService;
  private final ModelMapper modelMapper;
  private final UserAndClubService userAndClubService;

  //모임 생성
  //모임 생성 후 생성된 모임으로 이동할 수 있게 clubNo 반환
  @Override
  public Long createClub(ClubDTO clubDTO, PhotoDTO photoDTO, UserAndClubDTO userAndClubDTO) {

    //사진을 등록하지 않았다면 default 사진으로 등록
    Photo photo = new Photo();
    if(photoDTO.getPhotoUUID() != "") {
      photo = photoService.savePhoto(photoDTO);
    } else {
      photo.setPhotoUUID("default.jpg"); //TODO 나중에 실제 default 사진 UUID로 변경
    }

    clubDTO.setPhotoUUID(photo);
    Instant instant = Instant.now();
    clubDTO.setClubCreateDate(instant);//모임 생성일 추가
    Club club = modelMapper.map(clubDTO, Club.class);
    Long clubNo = clubRepository.save(club).getClubNo();

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
    List<ClubDTO> clubDTOS = modelMapper.map(clubs, List.class);
    return clubDTOS;
  }

  //모임 정보 수정
  //수정 가능 정보 : 사진, 카테고리, 소개글, 지역, 정원
  @Override
  public Long updateClub(ClubDTO clubDTO, PhotoDTO photoDTO) {
    Photo photo = photoService.savePhoto(photoDTO);
    clubDTO.setPhotoUUID(photo);
    Optional<Club> result = clubRepository.findById(clubDTO.getClubNo());
    Club club = result.orElseThrow();
    log.info(clubDTO);
    club.change(clubDTO.getPhotoUUID(), clubDTO.getClubCategory(), clubDTO.getClubContent(),
            clubDTO.getClubArea(), clubDTO.getClubMax()
    );
    clubRepository.save(club);

    return club.getClubNo();
  }

  //모임 해산
  @Override
  public void disbandClub(Long clubNo) {
    //해당 모임 맴버 전체 삭제
    userAndClubService.deleteAllMembers(clubNo);
    log.info("------------ [07-03-15:12:55]----------jinuk");
    //해당 모임 대표사진 조회
    Optional<Club> result = clubRepository.findById(clubNo);
    Club club = result.orElseThrow();
    Photo photo = club.getClubPhoto();
    String clubPhoto = photo.getPhotoUUID();
    log.info(clubPhoto);
    
    // 모임 해산
    clubRepository.deleteById(clubNo);
    log.info("------------ [07-03-15:13:27]----------jinuk");
    
    //해당 모임 대표사진 삭제
    photoService.deletePhoto(clubPhoto);
    log.info("------------ [07-03-15:13:43]----------jinuk");


  }

}