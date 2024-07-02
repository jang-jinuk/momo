package com.momo.momopjt.file;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  public String uploadFile(FileDto fileDto){
    log.info("----------------- [filecontroller uploadFile]-----------------");
    log.info(fileDto);

    //600p add
    if(fileDto.getFiles() != null){
      fileDto.getFiles().forEach(multipartFile -> {

//        log.info(multipartFile.getOriginalFilename());
        //602p
        String originalFileName = multipartFile.getOriginalFilename();
        log.info(originalFileName);

        String uuid = UUID.randomUUID().toString();
        Path savePath = Paths.get(uploadPath, uuid+"_"+originalFileName);

        //실제 파일 저장
        try{
          multipartFile.transferTo(savePath);
        } catch (IOException e) {e.printStackTrace();}

      });// end each
    }// end if/resa
    return null;
  }

}
