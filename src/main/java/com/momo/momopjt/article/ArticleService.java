package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import org.modelmapper.ModelMapper;

import java.util.List;

public interface ArticleService {

  Long createArticle(ArticleDTO articleDTO);
  ArticleDTO getArticleById(Long articleNo);
  //모든 게시글을 반환하는 메서드
  //시스템에 저장된 모든 게시글을 필요로 할 때 사용
  // 클럽 번호에 관계 없이 모든 게시글을 가져올 수 있음
  List<ArticleDTO> getAllArticles(Club clubNo);
  ArticleDTO updateArticle(Long articleNo, ArticleDTO articleDTO);
  void deleteArticle(Long articleNo);


  PageResponseDTO<ArticleListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

  //641
  default Article dtoToEntity(ArticleDTO articleDTO) {

    ModelMapper modelMapper = new ModelMapper();

    Article article =

    //책 코드
        Article.builder()
            .articleNo(articleDTO.getArticleNo())
            .articleContent(articleDTO.getArticleContent())
            .articleTitle(articleDTO.getArticleTitle())
            .articleState(articleDTO.getArticleState())
            .articleScore(articleDTO.getArticleScore())
            .articleCreateDate(articleDTO.getArticleCreateDate())
            //TODO user랑 club ?
            .build();

    if(articleDTO.getFileNames() != null){
      articleDTO.getFileNames().forEach(fileName -> {
            String[] arr = fileName.split("\\.");
          article.addImage(arr[0],"."+arr[1]);
          }
      );
    }


        //내 코드
//        modelMapper.map(
//        articleDTO, Article.class
//    );
    return article;
  }



}