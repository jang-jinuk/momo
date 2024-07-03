package com.momo.momopjt.Photo;

public interface PhotoService {
  Photo savePhoto(PhotoDTO photoDTO);

  Photo getPhoto(String photoId);
}
