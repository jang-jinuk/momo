package com.momo.momopjt.club;

//모임 CRUD기능 구현

import com.momo.momopjt.Photo.Photo;
import com.momo.momopjt.Photo.PhotoDTO;
import com.momo.momopjt.Photo.PhotoService;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
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

  //모임 생성
  //모임 생성 후 생성된 모임으로 이동할 수 있게 clubNo 반환
  @Override
  public Long createClub(ClubDTO clubDTO, PhotoDTO photoDTO) {
    Photo photo = photoService.savePhoto(photoDTO);
    clubDTO.setPhotoUuid(photo);
    Club club = modelMapper.map(clubDTO, Club.class);
    Long clubNo = clubRepository.save(club).getClubNo();
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
    clubDTO.setPhotoUuid(photo);
    Optional<Club> result = clubRepository.findById(clubDTO.getClubNo());
    Club club = result.orElseThrow();
    log.info(clubDTO);
    club.change(clubDTO.getPhotoUuid(), clubDTO.getClubCategory(), clubDTO.getClubContent(),
            clubDTO.getClubArea(), clubDTO.getClubMax()
    );
    clubRepository.save(club);

    return club.getClubNo();
  }

  //모임 삭제
  //모임 가입 신청 및 승인 기능구현 후 구현 예정
}