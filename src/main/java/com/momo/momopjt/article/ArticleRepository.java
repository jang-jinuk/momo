package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  List<Article> findByClubNo( Club clubNo);
  List<Article> findByuUserNo(User userNo);
}
