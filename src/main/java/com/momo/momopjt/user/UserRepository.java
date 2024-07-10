package com.momo.momopjt.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = "roleSet")
  @Query("select u from User u where u.userId = :userId and u.userSocial in :userSocials")
  Optional<User> findByUserIdAndUserSocialIn(@Param("userId") String userId, @Param("userSocials") Collection<Character> userSocials);

  Optional<User> findByUserIdAndUserSocial(String userId, Character userSocial);

  boolean existsByUserId(String userId); // userId로 존재 여부 확인
  User findByUserId(String userId);
  User findByUserIdAndUserEmail(String userId, String userEmail);
  boolean existsByUserEmail(String userEmail);
  @EntityGraph(attributePaths = "roleSet")
  @Query("select u from User u where u.userId = :userId and u.userSocial = 'K'")
  Optional<User> getWithRoles(@Param("userId") String userId);

  @EntityGraph(attributePaths = "roleSet")
  Optional<User> findByUserEmail(String email);
}





//  @Modifying
//  @Transactional
//  @Query("update User u set u.userPw =:userPw where u.userId =:userId")
//  void updatePassword(@Param("userPw") String password, @Param("userId") String userId);


