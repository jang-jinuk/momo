package com.momo.momopjt.photo;

import com.momo.momopjt.article.ArticleRepository;
import com.momo.momopjt.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Log4j2
public class PhotoController {

  private final PhotoService photoService;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;

  // MIME 타입 선언
//  private static final Map<String, String> MIME_TYPES = new HashMap<>();
//
//  static {
//    MIME_TYPES.put("jpg", MediaType.IMAGE_JPEG_VALUE);
//    MIME_TYPES.put("jpeg", MediaType.IMAGE_JPEG_VALUE);
//    MIME_TYPES.put("png", MediaType.IMAGE_PNG_VALUE);
//  }

  // 파일경로 확인 test
//    String uploadPath = System.getProperty("user.dir");
//    log.info("----------------- [{}]-----------------",uploadPath);

  @GetMapping("/photo/photo")
  public String photoGet() {
    log.info("----------------- [GET Photo]-----------------");
    return "photo/photo";
  }

  @PostMapping("/photo/photo")
  public void photoPost(@RequestParam("file") MultipartFile file, Model model) throws IOException {
    log.info("----------------- [POST Photo]-----------------");

    if (!file.isEmpty()) {
      //DB 저장 전 새로운 UUID 생성 TODO serviceImple 로 이동?
        String newUUID = UUID.randomUUID().toString();

        //DB 저장 로직 1 DTO 생성
        PhotoDTO photoDTO = PhotoDTO.builder()
            .photoUUID(newUUID)
//            .photoCreateDate() // serviceImpl에서 처리
            .photoOriginalName(file.getOriginalFilename())
//            .photoThumbnail() //임시
//            .articleNo(articleRepository.findById(6L).orElseThrow()) // not null 아님
            .uploader(userRepository.findById(1L).orElseThrow())
            .build();
        //DB 저장 로직 2 저장
        photoService.savePhoto(photoDTO);

        //TODO 리뷰 필요 0724 YY
        model.addAttribute("message", "File uploaded successfully!");


    } else {
      model.addAttribute("message", "Please select a file to upload.");
    }

    log.info("----------------- [+++ photo 저장 완료 +++]-----------------");
//    return "photo/photo";
  }

  @GetMapping("/photo/photo/{photoUUID}")
  public String getPhoto(@PathVariable String photoUUID, Model model) {
    log.info("----------------- [GET Photo /photo/photo/{photoUUID}]-----------------");

    Photo photo = photoService.getPhoto(photoUUID);
    String photoURL = photo.getPhotoURL();
    model.addAttribute("photoURL", photoURL);

    return "photo/photo";
  }

}