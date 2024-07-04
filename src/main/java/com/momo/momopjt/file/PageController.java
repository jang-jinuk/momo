package com.momo.momopjt.file;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class PageController {

  @GetMapping("file/fileupload")
  public String fileuploadPageGet() {
    log.info("----------------- [Controller : fileuploadget]-----------------");
    return "file/fileupload";

  }



}
