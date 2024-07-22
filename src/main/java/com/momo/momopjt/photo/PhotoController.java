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


  @GetMapping("/photo/photo")
  public String photoGet() {
    log.info("----------------- [GET Photo]-----------------");
    return "photo/photo";
  }

  @PostMapping("/photo/photo")
  public void photoPost(@RequestParam("file") MultipartFile file, Model model) throws IOException {
    log.info("----------------- [POST Photo]-----------------");

    if (!file.isEmpty()) {

      try {

        //DB 저장 전 파일 데이터 가져옴
        byte[] photoBytes = file.getBytes();

        //DB 저장 전 새로운 UUID 생성 TODO serviceImple 로 이동?
        String newUUID = UUID.randomUUID().toString();

        //DB 저장 로직 1 DTO 생성
        PhotoDTO photoDTO = PhotoDTO.builder()
            .photoUUID(newUUID)
//            .photoCreateDate() // serviceImpl에서 처리
            .photoData(photoBytes)
            .photoOriginalName(file.getOriginalFilename())
            .photoSize(file.getSize())
//            .photoThumbnail() //임시
//            .articleNo(articleRepository.findById(6L).orElseThrow()) // not null 아님
            .userNo(userRepository.findById(1L).orElseThrow())
            .build();
        //DB 저장 로직 2 저장
        photoService.savePhoto(photoDTO);

        log.info("----------------- [base64 처리]-----------------");
        String base64Image = Base64.getEncoder().encodeToString(photoBytes);
        model.addAttribute("base64Image", base64Image);

        model.addAttribute("message", "File uploaded successfully!");

      } catch (IOException e) {
        e.printStackTrace();
        model.addAttribute("message", "Failed to upload file.");
      }
    } else {
      model.addAttribute("message", "Please select a file to upload.");
    }

    log.info("----------------- [+++ photo 저장 완료 +++]-----------------");
//    return "photo/photo";
  }

  @GetMapping("/photo/photo/{photoUUID}")
  public String getPhoto(@PathVariable String photoUUID, Model model) {
    log.info("----------------- [GET Photo]-----------------");
    Photo photo = photoService.getPhoto(photoUUID);
    byte[] data = photo.getPhotoData();
    String base64data = Base64.getEncoder().encodeToString(data);
    model.addAttribute("base64data", base64data);
    return "photo/photo";
  }


/*        try {
            // Create a ByteArrayInputStream from the byte array
//            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);

            // Read the image from the ByteArrayInputStream
//            BufferedImage bimage = ImageIO.read(bais);

            // Do something with the BufferedImage object
//            System.out.println("Image width: " + bimage.getWidth());
//            System.out.println("Image height: " + bimage.getHeight());

            // ... Additional image processing or rendering code ...
          log.info("----------------- [done]-----------------");

          log.info("----------------- [convert]-----------------");

//          ByteArrayOutputStream baos = new ByteArrayOutputStream();
//          ImageIO.write(bimage,"png",baos);
//          byte[] pngBytes = baos.toByteArray();

*/
}

