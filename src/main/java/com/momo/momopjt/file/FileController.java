package com.momo.momopjt.file;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class FileController {

  //600p
  @Value("${com.momo.upload.path}")
  private String uploadPath;

  //599p
  @ApiOperation(value = "파일업로드")
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//  public String uploadFile(FileDto fileDto){
  //605p
  public List<UploadResultDto> uploadFile(FileDto fileDto){
    log.info("----------------- [filecontroller uploadFile]-----------------");
    log.info(fileDto);

    //600p add
    if(fileDto.getFiles() != null){

      //605p
      final List<UploadResultDto> uploadResultDtoList = new ArrayList<>();

      fileDto.getFiles().forEach(multipartFile -> {

//        log.info(multipartFile.getOriginalFilename());
        //602p
        String originalFileName = multipartFile.getOriginalFilename();
        log.info(originalFileName);

        String uuid = UUID.randomUUID().toString();
        Path savePath = Paths.get(uploadPath, uuid+"_"+originalFileName);
        boolean isImage = false;


        //실제 파일 저장
        try{
          log.info("----------------- [TRY file save]-----------------");
          multipartFile.transferTo(savePath);

          //603p 이미지 파일인 경우 썸네일 파일 생성
          if(Files.probeContentType(savePath).startsWith("image")){
            isImage = true;
            File thumbFile = new File(uploadPath, "s_"+uuid+"_"+originalFileName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);

          }

        } catch (IOException e) {
          log.error("----------------- [file save FAIL]-----------------");
          log.error(e.getMessage());
        }

        //606p
        uploadResultDtoList.add(UploadResultDto.builder()
                .uuid(uuid)
                .fileName(originalFileName)
                .isImage(isImage)
            .build()
        );

      });// end each
      return uploadResultDtoList;
    }// end if
    return null;
  }

//  GET방식 파일 조회
    //608p
  @ApiOperation(value = "파일조회")
  @GetMapping("/view/{fileName}")
  public ResponseEntity<Resource> viewFileGet(@PathVariable String fileName){
    log.info("----------------- [viewFileGet]-----------------");

    Resource resource = new FileSystemResource(uploadPath +File.separator+ fileName);
    String resourceName = resource.getFilename();
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


}
