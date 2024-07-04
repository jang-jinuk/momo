package com.momo.momopjt.Photo;

public interface PhotoService {
  Photo savePhoto(PhotoDTO photoDTO);

  Photo getPhoto(String photoUuid);

  void deletePhoto(String photoUuid);
}
