package com.momo.momopjt.photo;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserSerivce;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class PhotoServiceTests {

  @Autowired
  private PhotoService photoService;


  @Test
  public void savePhotoTest() {
    User user = new User();
    user.setUserNo(1L);
    byte[] bytes = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05}; // Sample byte[]

    PhotoDTO photoDTO = PhotoDTO.builder()
        .photoUUID("00001test")
        .userNo(user)
        .photoSize(10)
        .photoCreateDate(Instant.now())
        .photoOriginalName("test img")
        .photoSaveName("test save img")
        .photoThumbnail("test thumbnail img")
        .photoData(bytes)
        .build();


    MultipartFile file = new MockMultipartFile("Filename", bytes);

    try {
      photoService.savePhoto(file, photoDTO);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("UUID 테스트")
  void testUUID() {
    UUID uuid1 = UUID.randomUUID();
    String uuidStr = uuid1.toString();
    log.info(uuidStr);
    /// 결과 : varchar(32) , 하이픈 포함 varchar(36)
    /// ee5b7b2c-3eaa-4ccc-a30e-2d1ce9338c5d (예시)
  }

  @Test
  @DisplayName("user dir확인")
  void checkDir() {
    String uploadPath = System.getProperty("user.dir");
    log.info("----------------- [{}]-----------------", uploadPath);
    /// 결과 : Users/yjpark/reactprojects/momo
  }


  @Test
  public void updatePhotoTest() {

    User user = new User();
    user.setUserNo(1L);

    byte[] bytes = new byte[]{0x01, 0x02, 0x03}; // Sample byte[]

    PhotoDTO photoDTO = PhotoDTO.builder()
        .photoUUID("00001test")
        .userNo(user)
        .photoSize(10)
        .photoCreateDate(Instant.now())
        .photoOriginalName("test img2")
        .photoSaveName("test save img2")
        .photoThumbnail("test thumbnail img2")
        .photoData(bytes)
        .build();


    MultipartFile file = new MockMultipartFile("Filename", bytes);

    try {
      photoService.updatePhoto(file, photoDTO);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
