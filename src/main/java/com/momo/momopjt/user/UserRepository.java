package com.momo.momopjt.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select u from User u where u.userId = :userId and u.userSocial = 'M'")
    Optional<User> getWithRoles(String userId);

    boolean existsByUserId(String userId); // userId로 존재 여부 확인

    @EntityGraph(attributePaths ="roleSet")
    Optional<User> findByUserEmail(String email);
}