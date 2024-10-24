package com.momo.momopjt.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = "roleSet")
  @Query("select u from User u where u.userId = :userId and u.userSocial in :userSocials")
  Optional<User> findByUserIdAndUserSocialIn(@Param("userId") String userId,
                                             @Param("userSocials") Collection<Character> userSocials);


  // 소셜 로그인 사용자를 삭제할 때 사용
  @Modifying
  @Query("delete from User u where u.userId = :userId and u.userSocial in :userSocials")
  void deleteByUserIdAndUserSocialIn(@Param("userId") String userId,
                                     @Param("userSocials") Collection<Character> userSocials);


  User findByUserId(String userId);
  User findByUserIdAndUserEmail(String userId, String userEmail);

  boolean existsByUserEmail(String userEmail); //UserEmail로 존재 여부 확인
  boolean existsByUserId(String userId);// userId로 존재 여부 확인
  boolean existsByUserNickname(String userNickname);

  @EntityGraph(attributePaths = "roleSet")
  Optional<User> findByUserEmail(String email);

}