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
    Optional<User> findByUserIdAndUserSocialIn(String userId, Collection<Character> socialTypes);

    boolean existsByUserId(String userId); // userId로 존재 여부 확인

    @EntityGraph(attributePaths ="roleSet")
    //optional email에 꼽사리 껴서 증복처리 코드
    Optional<User> findByUserId(String userId);
    Optional<User> findByUserEmail(String useremail);

    @Modifying
    @Transactional
    @Query("update User u set u.userPw =:userPw where u.userId =:userId")
    void updatePassword(@Param("userPw") String password, @Param("userId") String userId);

    //Id Email 체크
    boolean existsUserByUserId(String userId);
    boolean existsUserByUserEmail(String userEmail);
    //join html에서 처리합니다.

}