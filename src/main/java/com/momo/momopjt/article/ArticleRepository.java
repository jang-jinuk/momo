package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

  List<Article> findByClubNo(Club clubNo);


@EntityGraph(attributePaths = {"imageSet"})
@Query("select a from Article a where a.articleNo = :articleNo")
Optional<Article> findByIdWithImages(@Param("articleNo") Long articleNo);




}
