package com.momo.momopjt.photo;

public interface PhotoService {
  Photo savePhoto(PhotoDTO photoDTO);

  Photo getPhoto(String photoUuid);

  void deletePhoto(String photoUuid);
}
