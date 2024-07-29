package com.momo.momopjt.file;

import com.momo.momopjt.photo.PhotoDTO;
import com.momo.momopjt.photo.PhotoRepository;
import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@RestController
@Log4j2
@RequiredArgsConstructor
public class FileController {

  private final PhotoService photoService;
  private final PhotoRepository photoRepository;
  private final UserService userService;
  //600p

  //로컬 파일 저장 경로
  @Value("${UploadPath}")
  private String uploadPath;

  //599p
  @ApiOperation(value = "파일업로드")
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//  public String uploadFile(FileDto fileDto){
  //605p
  public List<UploadResultDTO> uploadFile(UploadFileDTO uploadFileDTO){
    log.info("----------------- [FileController uploadFile()]-----------------");
    log.info("uploadFileDTO : {}",uploadFileDTO);

    //600p add
    if(uploadFileDTO.getFiles() != null){

      //605p
      final List<UploadResultDTO> uploadResultDTOList = new ArrayList<>();

      uploadFileDTO.getFiles().forEach(multipartFile -> {

        log.info("foreach file.getOrgfileName : {}",multipartFile.getOriginalFilename());
        //602p
        String originalFileName = multipartFile.getOriginalFilename();

        int lastDotIndex = originalFileName.lastIndexOf('.');
        String extension = (lastDotIndex != -1) ? originalFileName.substring( originalFileName.lastIndexOf('.') ) : "";

        log.info("----------------- [ext : {}]-----------------", extension);
        log.info(originalFileName);

        String uuid = UUID.randomUUID().toString();
        log.info("----------------- [uuid : {}]-----------------",uuid);
        log.info("----------------- [07-26 15:46:17]-----------------");

//        Path savePath = Paths.get(uploadPath, uuid+"_"+originalFileName);
        Path savePath = Paths.get(uploadPath, uuid+extension);

        boolean isImage = false;


        //실제 파일 저장
        try{
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
          log.trace("auth name 찾아옴 : "+userName);

          User user = userService.findByUserId(userName);

//           TODO 필요시 수정  0724 YY //


          photoService.savePhoto(PhotoDTO.builder()
                  .photoUUID(uuid)
                  .photoCreateDate(Instant.now())
                  .photoExtension(extension)
              .uploader(user)
              .build());

          log.info("----------------- [need fix]-----------------");


          //603p 이미지 파일인 경우 썸네일 파일 생성
          if(Files.probeContentType(savePath).startsWith("image")){
            isImage = true;
//            String thumbUUID = UUID.randomUUID().toString(); // 새로운 uuid X
            File thumbFile = new File(uploadPath, "t_"+uuid+extension);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
            log.trace("썸네일 파일 생성--");

//            photoService.savePhoto(PhotoDTO.builder()
//                .photoUUID(uuid) // 썸네일 uuid 새로 지정
//                .photoCreateDate(Instant.now())
//                .photoExtension(extension)
//                .uploader(user)
//                .build());

            log.info("----------------- [썸네일 파일 저장 / DB에 x]-----------------");

          }

        } catch (IOException e) {
          log.error("----------------- [file save FAIL]-----------------");
          log.error(e.getMessage());
        }

        //606p
        uploadResultDTOList.add(UploadResultDTO.builder()
                .uuid(uuid)
                .extension(extension)
                .isImage(isImage).build()
        );

      });// end each
      return uploadResultDTOList;
    }// end if
    return null;


  }

//  GET방식 파일 조회
    //608p
  @ApiOperation(value = "파일조회")
  @GetMapping("/{fileName}") // 원래 view/{fileName}
  public ResponseEntity<Resource> viewFileGet(@PathVariable String fileName){
    log.info("----------------- [GET File  /{fileName}]-----------------");

    Resource resource = new FileSystemResource(uploadPath +File.separator+ fileName);

    String resourceName = resource.getFilename();
    log.info("..................resourceName = "+resourceName);

    HttpHeaders headers = new HttpHeaders();

    try{
      headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));

    } catch(Exception e){

      log.info("----------------- [조회 Fail return]-----------------");
      return ResponseEntity.internalServerError().build();
    }

    log.info("----------------- [조회 정상 return]-----------------");
    return ResponseEntity.ok().headers(headers).body(resource);
  }

  // Delete 방식 삭제
//  609p
  @ApiOperation(value = "파일삭제")
  @DeleteMapping("/remove/{fileName}")
  public Map<String, Boolean> deleteFile(@PathVariable String fileName){

    log.info("----------------- [deleteFile]-----------------");

    Resource resource = new FileSystemResource(uploadPath +File.separator+ fileName);
    String resourceName = resource.getFilename();
    Map<String, Boolean> resultMap = new HashMap<>();
    boolean isRemoved = false;

    try {
      String contentType = Files.probeContentType(resource.getFile().toPath());
      isRemoved = resource.getFile().delete();

      //섬네일 처리 TODO t_ThumbUUID + extension 임. 수정필요
      if(contentType.startsWith("image")){
        log.info("----------------- [썸네일 파일 처리 IF문]-----------------");
        File thumbFile = new File(uploadPath, File.separator+"t_"+fileName);
        thumbFile.delete(); // 썸네일 삭제
      }//end if

    } catch(Exception e) {
      log.info("----------------- [delete Fail]-----------------");
      log.error(e.getMessage());
    }

    resultMap.put("result", isRemoved);


//    db에서도 삭제

    int lastDotIndex = fileName.lastIndexOf('.');
    photoService.deletePhoto(fileName.substring(0,lastDotIndex));

    log.info("----------------- [filedeleted]-----------------{}",fileName);

    return resultMap;
  }


}
