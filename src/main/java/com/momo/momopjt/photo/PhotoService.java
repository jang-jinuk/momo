package com.momo.momopjt.photo;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

  Photo savePhoto(PhotoDTO photoDTO);

  Photo getPhoto(String photoUUID);

  List<Photo> getPhotoList(Character condition);

  void deletePhoto(String photoUUID);

  //YY
  String saveFile(MultipartFile file);

}
