package com.momo.momopjt.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

  //625p
 @Query("select r from Reply r where r.articleNo.articleNo =: articleNo")
    List<Reply> findByArticleId(Long articleNo, Pageable pageable);
 //TODO Pageable 확인

  void deleteByArticleNoArticleNo(Long articleNo);


}
