package com.momo.momopjt.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadFileDTO {
  // 598p
  private List<MultipartFile> files;
}
