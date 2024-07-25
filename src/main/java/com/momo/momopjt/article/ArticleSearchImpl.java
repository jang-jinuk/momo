package com.momo.momopjt.article;

import com.momo.momopjt.reply.QReply;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Log4j2
public class ArticleSearchImpl implements ArticleSearch {

  @Override
  public Page<ArticleListRepluCountDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

    QArticle article = QArticle.article;
    QReply reply = QReply.reply;

    JPQLQuery<Article> articleJPQLQuery = from(Article);
    articleJPQLQuery.leftJoin(reply).on(reply.article.eq(article));
    getQueryDsl().applyPagination(pageable, articleJPQLQuery);

    List<Article> articleList = articleJPQLQuery.fetch();

    articleList.forEach(article1 -> {
      log.info("----------------- [{}]-----------------",article1.getArticleNo());
      log.info("----------------- [{}]-----------------",article1.getImageSet());
    });

    return null;
  }
}
