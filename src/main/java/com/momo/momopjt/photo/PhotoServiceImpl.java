package com.momo.momopjt.photo;

import com.momo.momopjt.club.ClubRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Builder
@RequiredArgsConstructor
@Log4j2
public class PhotoServiceImpl implements PhotoService {

  private final PhotoRepository photoRepository;
  private final ClubRepository clubRepository;

  private final ModelMapper modelMapper;

  @Override
  public Photo savePhoto(PhotoDTO photoDTO) {
    log.trace("--------save photo start-----------");

    Photo photo = new Photo();
    //사진을 등록하지 않으면 "default"사진 자동 저장
    if (photoDTO.getPhotoUUID().equals("") || photoDTO.getPhotoUUID() == null) {
      log.error("uuid is null or empty");
      photo.setPhotoUUID("default"); //TODO 실제 디폴트 사진으로 변경 필요 JW
      return photo;
    }

    photoDTO.setPhotoCreateDate(Instant.now()); //생성(등록)일 자동으로 넣기

    photo = modelMapper.map(photoDTO, Photo.class);

    photoRepository.save(photo);

    log.info("DB에 저장된 photoUUID : {}", photo.getPhotoUUID());
    log.info("------------------Photo saved--------------------");
    return photo;
  }


  @Override
  public Photo getPhoto(String photoUUID) {
    log.info("------------ getPhoto() ----------jinuk");

    // 기본 사진 조회 설정
    switch (photoUUID) {

      case "ClubDefaultPhoto":
        log.info("try  get photo-ClubDefaultPhoto");
        Photo clubDefaultPhoto = new Photo();
        clubDefaultPhoto.setPhotoUUID("ClubDefaultPhoto");
        clubDefaultPhoto.setPhotoExtension(".jpg");
        return clubDefaultPhoto;

      case "UserDefaultPhoto":
        log.info("try  get photo-UserDefaultPhoto");
        Photo userDefaultPhoto = new Photo();
        userDefaultPhoto.setPhotoUUID("UserDefaultPhoto");
        userDefaultPhoto.setPhotoExtension(".png");
        return userDefaultPhoto;
      case "ScheduleDefaultPhoto":

        log.info("try  get photo-ScheduleDefaultPhoto");
        Photo scheduleDefaultPhoto = new Photo();
        scheduleDefaultPhoto.setPhotoUUID("ScheduleDefaultPhoto");
        return scheduleDefaultPhoto;
    }

    boolean existCheck = photoRepository.existsById(photoUUID);

    if (!existCheck) {
      log.warn("No existing Photo...... null photo return");

      Photo NullPhoto = new Photo();
      NullPhoto.setPhotoUUID("NullPhoto");
      return NullPhoto;
    }


    log.info("----------------- [uuid : {}, exist : {}]-----------------", photoUUID, existCheck);

    Optional<Photo> photoOptional = photoRepository.findById(photoUUID);
    log.trace("----------------- [{}]-----------------", photoOptional);

    return photoOptional.orElseThrow();

  }

  //현재는 안씀
  @Override
  public List<Photo> getPhotoList(Character condition) {
    List<Photo> photoList = photoRepository.findAll();
    return photoList;
  }

  @Override
  public void deletePhoto(String photoUUID) {

    if (photoUUID == null || photoUUID.equals("")) {
      log.warn("----------------- [deletePhoto(UUID) UUID is null or empty]-----------------{}", photoUUID);
      return;
    } else {
      log.info("----------------- [deletePhoto : {} at DB]-----------------", photoUUID);
      photoRepository.deleteById(photoUUID);
    }


  }

}