package com.momo.momopjt.photo;

public interface PhotoService {

  Photo savePhoto(PhotoDTO photoDTO);

  Photo getPhoto(String photoUUID);

  void deletePhoto(String photoUUID);

  //페이지에서 조회(렌더링)를 편하게 하기위해 base64 String을 반환하는 기능
  String getPhoto64(String photoUUID);

}
