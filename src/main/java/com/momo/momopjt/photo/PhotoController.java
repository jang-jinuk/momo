package com.momo.momopjt.photo;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@Log4j2
public class PhotoController {

  @PostMapping("/upload")
  public String saveFile(@RequestParam MultipartFile file) throws IOException {

    String uploadPath = System.getProperty("user.dir");
    log.info("----------------- [{}]-----------------",uploadPath);


    // 파일 저장
    file.transferTo(new File(uploadPath+"/"+file.getOriginalFilename()));

    return uploadPath;

  }

  @GetMapping("file/fileupload")
  public String fileuploadPageGet() {
    log.info("----------------- [Controller : fileuploadget]-----------------");
    return "file/fileupload";

  }

}
