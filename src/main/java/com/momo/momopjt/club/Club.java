package com.momo.momopjt.club;

import com.momo.momopjt.Photo.Photo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "club")
public class Club {
    @Id
    @Column(name = "clubNo", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clubMainPhoto", nullable = false)
    private Photo clubMainPhoto;

    @Column(name = "clubName", nullable = false, length = 50)
    private String clubName;

    @Column(name = "clubCategory", length = 100)
    private String clubCategory;

    @Column(name = "clubContent")
    private String clubContent;

    @Column(name = "clubArea", length = 50)
    private String clubArea;

    @Column(name = "clubMax", nullable = false)
    private Integer clubMax;

    @Column(name = "clubGender", nullable = false)
    private Character clubGender;

    @Column(name = "clubCreatedDate", nullable = false)
    private Instant clubCreatedDate;

}