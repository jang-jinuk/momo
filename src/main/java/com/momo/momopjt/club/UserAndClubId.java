package com.momo.momopjt.club;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;


@Getter
@Setter
@Embeddable
public class UserAndClubId implements java.io.Serializable {
  private static final long serialVersionUID = -6426202124576276779L;
  @Column(name = "user_no", nullable = false)
  private Long userNo;

  @Column(name = "club_no", nullable = false)
  private Long clubNo;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    UserAndClubId entity = (UserAndClubId) o;
    return Objects.equals(this.userNo, entity.userNo) &&
        Objects.equals(this.clubNo, entity.clubNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userNo, clubNo);
  }

}