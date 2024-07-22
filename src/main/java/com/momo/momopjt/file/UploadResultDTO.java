package com.momo.momopjt.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class UploadResultDTO {
  private String uuid;
  private String fileName;
  private boolean isImage; // 책에서 img

  public String getLink(){ // getLink 나중 JSON 처리?
    log.info("----------------- [getLink()]-----------------");
    if(isImage){
      return "s_"+uuid+"_"+fileName; // 이미지인 경우 섬네일
    } else {
      return uuid+"_"+fileName;
    }
  }
}
