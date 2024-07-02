package com.momo.momopjt.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_no", nullable = false)
    private Long userNo;

    @Column(name = "user_id", nullable = false, length = 30)
    private String userId;

}