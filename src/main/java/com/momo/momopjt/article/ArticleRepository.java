package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

  List<Article> findByClubNo(Club clubNo);


//@EntityGraph(attributePaths = {"imageSet"})
//@Query("select a from Article a where a.articleNo = :articleNo")
//Optional<Article> findByIdWithImages(@Param("articleNo") Long articleNo);

}
