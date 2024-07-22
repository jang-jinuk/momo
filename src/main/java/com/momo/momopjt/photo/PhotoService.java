package com.momo.momopjt.photo;

public interface PhotoService {

  Photo savePhoto(PhotoDTO photoDTO);

  //photo 객체는 update가 필요 없을듯?
  //필요시 삭제 후 재등록으로 구현?

  Photo getPhoto(String photoUUID);

  void deletePhoto(String photoUUID);

  //페이지에서 조회(렌더링)를 편하게 하기위해 base64 String을 반환하는 기능
  String getPhoto64(String photoUUID);

}
