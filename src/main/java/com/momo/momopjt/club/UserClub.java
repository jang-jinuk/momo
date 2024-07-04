package com.momo.momopjt.club;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_club")
public class UserClub {
    @Id
    @Column(name = "clubNo", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clubNo", nullable = false)
    private Club club;

    @Column(name = "userNo", nullable = false)
    private Long userNo;

    @Column(name = "isLeader")
    private Character isLeader;

    @Column(name = "joinDate")
    private Instant joinDate;

    @Column(name = "isWish")
    private Character isWish;

}