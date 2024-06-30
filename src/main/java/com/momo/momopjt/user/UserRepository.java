package com.momo.momopjt.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.momo.momopjt.user.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select u from User u where u.userId = :userId and u.userSocial = 'M'")
    Optional<User> getWithRoles(String userId);


}
