package com.momo.momopjt.photo;

import java.io.IOException;
import java.util.Optional;

import com.momo.momopjt.club.ClubRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Builder
@RequiredArgsConstructor
@Log4j2
public class PhotoServiceImpl implements PhotoService {

  private final ModelMapper modelMapper;
  private final PhotoRepository photoRepository;
  private final ClubRepository clubRepository;





  @Override
  public Photo savePhoto(MultipartFile file, PhotoDTO photoDTO) throws IOException {

    byte[] fileBytes = file.getBytes();
    log.info("----------------- [file.getBytes]-----------------", fileBytes);
//    byte[] testBytes = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
    photoDTO.setPhotoData(fileBytes);

    Photo photo = modelMapper.map(photoDTO, Photo.class);
    photoRepository.save(photo);
    log.info(photo);
    log.info(photo.getPhotoUUID());
    log.info("------------------Photo saved--------------------");
    return photo;
  }

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

  }




  @Override
  public Photo getPhoto(String photoUUID) {

    log.info("------------ getphoto [07-02-11:13:41]----------jinuk");
    Optional<Photo> photoOptional = photoRepository.findById(photoUUID);

    return photoOptional.orElseThrow();

  }

  @Override
  public void deletePhoto(String photoUUID) {
    photoRepository.deleteById(photoUUID);
  }





}