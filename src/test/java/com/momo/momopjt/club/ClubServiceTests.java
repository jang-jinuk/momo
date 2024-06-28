package com.momo.momopjt.club;

import com.momo.momopjt.Photo.Photo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@SpringBootTest
@Log4j2
public class ClubServiceTests {
    @Autowired
    private ClubService clubService;

    @Test
    public void createTest() {
        Photo photo = new Photo();
        photo.setPhotoUuid("테스트모임대표사진.jpg");

        ClubDTO clubDTO = ClubDTO.builder()
                .clubArea("테스트 지역")
                .clubCategory("테스트 카테고리")
                .clubName("테스트 모임")
                .clubContent("테스트 모임 소개")
                .clubMax(10)
                .clubGender('m')
                .clubCreatedDate(Instant.now())
                .clubMainPhoto(photo)
                .build();
        clubService.createClub(clubDTO);
    }

    @Test
    @Transactional
    public void readOneTest() {
        ClubDTO clubDTO = clubService.readOneClub(1L);
        log.info(clubDTO);
    }

    @Test
    @Transactional
    public void readAllTest() {
        List<ClubDTO> clubDTOs = clubService.readAllClub();
        log.info(clubDTOs);

    }
}
