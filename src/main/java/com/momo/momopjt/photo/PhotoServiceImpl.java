package com.momo.momopjt.photo;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

import com.momo.momopjt.club.ClubRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    Photo photo = new Photo();

    //사진을 등록하지 않으면 "default"사진 자동 저장
    if(photoDTO.getPhotoUUID().equals("")) {
      photo.setPhotoUUID("default.jpg"); //TODO 실제 디폴트 사진으로 변경 필요 JW

      return photo;
    }


    photoDTO.setPhotoCreateDate(Instant.now()); //생성(등록)일 자동으로 넣기

    photo = modelMapper.map(photoDTO, Photo.class);
    photoRepository.save(photo);

    log.info("photo : {}",photo);
    log.info("photo : {}",photo.getPhotoUUID());
    log.info("------------------Photo saved--------------------");
    return photo;
  }


/*
@Override
  public Photo updatePhoto(MultipartFile file, PhotoDTO photoDTO) throws IOException {

    byte[] fileBytes = file.getBytes();// 실제 작동시 이걸로
    log.info("----------------- [file.getBytes]-----------------", fileBytes);
    //이건 임시
    byte[] testNewBytes = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

    Photo photo = photoRepository.findById(photoDTO.getPhotoUUID()).orElseThrow();
    photo.setPhotoData(testNewBytes);
    if ( photo != null ){

      Photo newPhoto = modelMapper.map(photoDTO, Photo.class);
      photoRepository.save(newPhoto);
      return newPhoto;

    } else {

    return photo;

    }

  }*/




  @Override
  public Photo getPhoto(String photoUUID) {
    log.info("------------ getPhoto() ----------jinuk");
    Optional<Photo> photoOptional = photoRepository.findById(photoUUID);

    return photoOptional.orElseThrow();

  }

  @Override
  public void deletePhoto(String photoUUID) {
    photoRepository.deleteById(photoUUID);
  }


  @Override
  public String getPhoto64(String photoUUID) {

    Photo Photo = photoRepository.findById(photoUUID).orElseThrow();
    String base64str = Base64.getEncoder().encodeToString(Photo.getPhotoData());

    return base64str;
  }

}