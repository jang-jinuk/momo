package com.momo.momopjt.photo;

import com.momo.momopjt.user.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
@Log4j2
public class PhotoServiceTests {

    @Autowired
    PhotoService photoService;
    @Test
    public void savePhotoTest() {
        User user = new User();
        user.setUserNo(1L);

        PhotoDTO photoDTO = PhotoDTO.builder()
                .photoUuid("00001test")
                .userNo(user)
                .photoSize(10)
                .photoCreateDate(Instant.now())
                .photoOriginalName("test img")
                .photoSaveName("test save img")
                .photoThumbnail("test thumbnail img")
                .build();
        photoService.savePhoto(photoDTO);
    }

}
