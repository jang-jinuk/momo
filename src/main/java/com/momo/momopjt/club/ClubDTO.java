package com.momo.momopjt.club;

import com.momo.momopjt.Photo.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubDTO {
    private Long id;

    private Photo clubMainPhoto;

    private String clubName;

    private String clubCategory;

    private String clubContent;

    private String clubArea;

    private Integer clubMax;

    private Character clubGender;

    private Instant clubCreatedDate;
}