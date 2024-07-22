/*
 * 파일에 대한 설명을
 * 써주면 좋을것 같은데
 *
 */
package com.momo.momopjt.photo;

import com.momo.momopjt.user.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
@Log4j2
public class PhotoServiceTests {

    @Autowired
    private PhotoService photoService;

    //CRUD 중 Create test
    @Test
    public void savePhotoTest() {
        User user = new User();
        user.setUserNo(1L);

        PhotoDTO photoDTO = PhotoDTO.builder()
                .photoUUID("")
                .userNo(user)
                .photoSize(10L)
                .photoCreateDate(Instant.now())
                .photoOriginalName("test img")
                .photoSaveName("test save img")
                .photoThumbnail("test thumbnail img")
                .build();
        photoService.savePhoto(photoDTO);
    }

    //CRUD 중 Read-1개 test
    @Test
    public void getPhotoTest() {
        String photoUUID = "5b2b26dc-17c3-4ae6-b528-943ef6fa2b44";
        Photo photo = photoService.getPhoto(photoUUID);
        log.info("test ^0^ photo : {}", photo);
        log.info("test *_* photoUUID : {}",photoUUID);
    }

    //CRUD 중 Delete test
    @Test
    public void removePhotoTest() {
        String photoUUID = "46afcc3e-ca2f-4441-a3e6-5c8407a5deb4";
        photoService.deletePhoto(photoUUID);
        Photo photo;
      try {
          photo = photoService.getPhoto(photoUUID);

          log.info("test -_- photo is not deleted");
      } catch (Exception e) {
          log.info("test +_+ photo is deleted");
      }
    }

    //Base64 String타입 변환, 리턴 테스트
    @Test
    public void getPhoto64Test() {
        String photoUUID = "5b2b26dc-17c3-4ae6-b528-943ef6fa2b44"; // DB에 있는 것 설정 필요

        String photo64 = photoService.getPhoto64(photoUUID);

        log.info("will get 64 of PhotoUUID : {}",photoUUID);
        log.info("photo64 : {}",photo64.substring(0,100)); // 너무길어서 자름
    }

}
