package com.momo.momopjt.photo;

import java.time.Instant;
import java.util.Optional;

import com.momo.momopjt.club.ClubRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
@Log4j2
public class PhotoServiceImpl implements PhotoService {

  private final ModelMapper modelMapper;
  private final PhotoRepository photoRepository;
  private final ClubRepository clubRepository;

  @Override
  public Photo savePhoto(PhotoDTO photoDTO) {
    photoDTO.setPhotoCreateDate(Instant.now()); //등록일
    Photo photo = modelMapper.map(photoDTO, Photo.class);
    photoRepository.save(photo);
    log.info(photo);
    log.info(photo.getPhotoUUID());
    log.info("------------------Photo saved--------------------");
    return photo;
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