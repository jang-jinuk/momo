package com.momo.momopjt.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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