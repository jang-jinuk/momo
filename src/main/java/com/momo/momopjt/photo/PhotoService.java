package com.momo.momopjt.photo;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {
  Photo savePhoto(MultipartFile file,PhotoDTO photoDTO) throws IOException;

  Photo updatePhoto(MultipartFile file, PhotoDTO photoDTO) throws IOException;

  Photo getPhoto(String photoUUID);

  void deletePhoto(String photoUUID);

}
