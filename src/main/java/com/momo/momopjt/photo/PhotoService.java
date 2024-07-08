package com.momo.momopjt.photo;

public interface PhotoService {
  Photo savePhoto(PhotoDTO photoDTO);

  Photo getPhoto(String photoUUID);

  void deletePhoto(String photoUUID);
}
