package com.momo.momopjt.article;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ArticleSearchImpl implements ArticleSearch {

  /*
  //636은 스킵
  @Override
  public Page<ArticleListRepluCountDTO> searchWithAll(String[] types,
                                                      String keyword,
                                                      Pageable pageable) {

    QArticle article = QArticle.article;
    QReply reply = QReply.reply;

    JPQLQuery<Article> articleJPQLQuery = from(Article);
    articleJPQLQuery.leftJoin(reply).on(reply.article.eq(article)); // left join

    //638
    if( (types != null && types.length >0) && keyword != null ) {
      BooleanBuilder booleanBuilder = new BooleanBuilder();

      for (String type : types) {
        switch (type) {
          case "t":
            booleanBuilder.or(article.articleTitle.contains(keyword));
            break;
          case "c":
            booleanBuilder.or(article.articleContent.contains(keyword));
            break;
          case "w":
            booleanBuilder.or(article.userNo.userNickname.contains(keyword));
            break;
        }
      } // end for

      articleJPQLQuery.where(booleanBuilder);

    }


    articleJPQLQuery.groupBy(article);

    getQueryDsl().applyPagination(pageable, articleJPQLQuery); // paging

    List<Article> articleList = articleJPQLQuery.fetch();

    JPQLQuery<Tuple> tupleJPQLQuery = articleJPQLQuery.select(article, reply.countDistinct());


    List<Tuple> tupleList = tupleJPQLQuery.fetch();


    List<ArticleListAllDTO> dtoList =  tupleList.stream().map(tuple ->
    {
      Article article1 = (Article) tuple.get(article);
      long replyCount = tuple.get(1, Long.class);

      ArticleListAllDTO dto = ArticleListAllDTO.builder()
          .articleNo(article1.getArticleNo())
          .articleTitle(article1.getArticleTitle())
          .articleContent(article1.getArticleContent())
          .articleCreateDate(article1.getArticleCreateDate())
          .articleScore(article1.getArticleScore())
          .articleState(article1.getArticleState())
          .replyCount(replyCount)
          .build();

      //637 articleImage 처리
      List<ArticleImageDTO> imageDTOS = article1.getImageSet().stream().sorted()
          .map(articleImage -> ArticleImageDTO.builder()
              .uuid(articleImage.getUuid())
              .extension(articleImage.getExtension())
              .ord(articleImage.getOrd())
              .build()
      ).collect(Collectors.toList());


      dto.setArticleImages(imageDTOS);

      return dto;

    }).collect(Collectors.toList());


    long totalCount = articleJPQLQuery.fetchCount();

    return new PageImpl<>(dtoList, pageable, totalCount);
//
//    articleList.forEach(article1 -> {
//      log.info("----------------- [{}]-----------------",article1.getArticleNo());
//      log.info("----------------- [{}]-----------------",article1.getImageSet());
//    });
//
//    return null;
  }

   */
}
