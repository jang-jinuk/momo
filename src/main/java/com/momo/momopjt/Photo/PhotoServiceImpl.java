package com.momo.momopjt.Photo;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Builder
@RequiredArgsConstructor
@Log4j2
public class PhotoServiceImpl implements PhotoService {

  private final ModelMapper modelMapper;
  private final PhotoRepository photoRepository;

  @Override
  public Photo savePhoto(PhotoDTO photoDTO) {
    Photo photo = modelMapper.map(photoDTO, Photo.class);
    photoRepository.save(photo);
    log.info("------------------Photo saved--------------------");
    return photo;
  }

  @Override
  public Photo getPhoto(String photoUuid) {

    log.info("------------ getphoto [07-02-11:13:41]----------jinuk");
    Optional<Photo> photoOptional = photoRepository.findById(photoUuid);

    return photoOptional.orElseThrow();

  }


}
