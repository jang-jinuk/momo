package com.momo.momopjt.photo;

import com.momo.momopjt.reply.ReplyDTO;
import com.momo.momopjt.user.UserDTO;

import java.util.List;

public interface PhotoService {

  Photo savePhoto(PhotoDTO photoDTO);

  Photo getPhoto(String photoUUID);

  List<Photo> getPhotoList(Character condition);

  void deletePhoto(String photoUUID);

  UserDTO addPhotoStr(UserDTO userDTO);
  ReplyDTO addPhotoStr(ReplyDTO replyDTO);
  //YY
//  String saveFile(MultipartFile file);

}
