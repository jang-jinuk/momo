package com.momo.momopjt.photo;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Log4j2
public class PhotoResolveServ {

  @Service
  public class FileStorageService {

    private final Path rootLocation = Paths.get("/Users/yjpark/upload");

    public void store(MultipartFile file) {
      try {
        if (file.isEmpty()) {
          throw new RuntimeException("Failed to store empty file.");
        }
        Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
      } catch (IOException e) {
        throw new RuntimeException("Failed to store file.", e);
      }
    }
  }

}
