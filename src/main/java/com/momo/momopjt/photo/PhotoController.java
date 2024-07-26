package com.momo.momopjt.photo;

import com.momo.momopjt.article.ArticleRepository;
import com.momo.momopjt.file.UploadResultDTO;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Log4j2
public class PhotoController {

  private final PhotoService photoService;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;
  private final UserService userService;

  //로컬 파일 저장 경로
  @Value("${UploadPath}")
  private String uploadPath;



  @GetMapping("/photo/photo")
  public String photoGet() {
    log.info("----------------- [GET /photo/photo]-----------------");
    return "photo/photo";
  }


  @PostMapping("/photo/photo")
  public void photoPost(@RequestParam("file") MultipartFile file, Model model) throws IOException {
    log.info("----------------- [POST photo/photo]-----------------");

    List<MultipartFile> files = List.of(file);

    final List<UploadResultDTO> uploadResultDTOList = new ArrayList<>();

    files.forEach(multipartFile -> {
      
      String originalFileName = multipartFile.getOriginalFilename();
      
      int lastDotIndex = originalFileName.lastIndexOf('.');
      String extension = (lastDotIndex != -1) ? originalFileName.substring(originalFileName.lastIndexOf('.')) : "";

      log.info("----------------- [ext : {}]-----------------", extension);
      log.info(originalFileName);

      String uuid = UUID.randomUUID().toString();
//      Path savePath = Paths.get(uploadPath, uuid + "_" + originalFileName);
      Path savePath = Paths.get(uploadPath, uuid + extension);
      boolean isImage = false;


      //실제 파일 저장
      try {
        log.info("----------------- [TRY file save]-----------------");
        multipartFile.transferTo(savePath);
        log.info("----------------- [TRY file save at DB]-----------------");


        //User 1번으로 테스트 할 때
//          User user = new User();
//          user.setUserNo(1L);

        //실제 로직
        log.info("--  Auth 처리 시작 --");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        log.trace("auth name 찾아옴 : " + userName);

        User user = userService.findByUserId(userName);

//           TODO 필요시 수정  0724 YY //


        photoService.savePhoto(PhotoDTO.builder()
            .photoUUID(uuid)
            .photoCreateDate(Instant.now())
            .photoExtension(extension)
            .uploader(user)
            .build());

        log.info("----------------- [07-26 15:44:57]-----------------");

        //603p 이미지 파일인 경우 썸네일 파일 생성
        if (Files.probeContentType(savePath).startsWith("image")) {
          isImage = true;
//          File thumbFile = new File(uploadPath, "t_" + uuid + "_" + originalFileName);
          File thumbFile = new File(uploadPath, "t_"+uuid+extension);
          Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
          log.trace("썸네일 파일 생성--");

//          썸네일 DB 저장 x
//          photoService.savePhoto(PhotoDTO.builder()
//              .photoUUID(UUID.randomUUID().toString()) // 썸네일 uuid 새로 지정
//              .photoCreateDate(Instant.now())
//              .photoExtension(extension)
//              .uploader(user)
//              .build());

          log.info("----------------- [썸네일 파일 저장]-----------------");

        }

      } catch (IOException e) {
        log.error("----------------- [file save FAIL]-----------------");
        log.error(e.getMessage());
      }


      model.addAttribute("message", "File uploaded successfully!");


      model.addAttribute("message", "Please select a file to upload.");

      log.info("----------------- [+++ photo 저장 완료 +++]-----------------");
//    return "photo/photo";

    }); // end each

  }


//  @GetMapping("/photo/photo/{photoUUID}")
//  public String getPhoto(@PathVariable String photoUUID, Model model) {
//    log.info("----------------- [GET Photo /photo/photo/{photoUUID}]-----------------");
//
//    Photo photo = photoService.getPhoto(photoUUID);
//    String photoURL = photo.getPhotoURL();
//    model.addAttribute("photoURL", photoURL);
//
//    return "photo/photo";
//  }
//
//
//  @GetMapping("/getphoto")
//  public void imgView(@RequestParam("imgFile") String imgFile,
//                      HttpServletResponse response) throws IOException {
//    response.addHeader(
//        "Content-disposition", "attachment;fileName=" + imgFile); //파일을 다운받고, 브라우저로 표현하고, 다운될 파일이름
//    File file = new File(uploadPath + "/" + imgFile);
//    FileInputStream in;
//    in = new FileInputStream(file);
//    FileCopyUtils.copy(in, response.getOutputStream());
//    in.close();
//
//  }
//
//  @GetMapping("/image-url/{fileName}")
//  public String getImageUrl(@PathVariable String fileName, HttpServletRequest request) {
//    log.info("----------------- [ GET ! ImageUrl]-----------------");
//    String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
//    String imageUrl = baseUrl + "/view/" + fileName;
//    return imageUrl;
//  }

//  @GetMapping()
//  public ResponseEntity<byte[]> getImage();

//  @GetMapping("/list")
//  public void list(PageRequestDTO pageRequestDTO, Model model){
//    PageResponseDTO<BoardListAllDTO> responseDTO =
//        photoService.listWithAll(pageRequestDTO);
//
//  log.info("----------------- [responseDTO]-----------------"+responseDTO);
//  model.addAttribute("responseDTO", responseDTO);
//}

}