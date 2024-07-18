package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;

import com.momo.momopjt.club.ClubRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {


  private final ArticleRepository articleRepository;
  private final ClubRepository clubRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public ArticleServiceImpl(ArticleRepository articleRepository, ClubRepository clubRepository, ModelMapper modelMapper) {
    this.articleRepository = articleRepository;
    this.clubRepository = clubRepository;
    this.modelMapper = modelMapper;
  }

  //새로운 후기글을 생성하는 메서드
  @Override
  public Article createArticle(ArticleDTO articleDTO) {
    articleDTO.setArticleNo(1L);
    articleDTO.setArticleCreateDate(Instant.now());
    Article article = modelMapper.map(articleDTO, Article.class);
    return articleRepository.save(article);
  }


 //모든 후기글 가져오는 메서드
 @Override
 public List<ArticleDTO> getAllArticles() {
   List<Article> articles = articleRepository.findAll();
   List<ArticleDTO> articleDTOS = articles.stream()
       .map(article -> modelMapper.map(article, ArticleDTO.class))
       .collect(Collectors.toList());

   return articleDTOS;
 }


 // 특정 아이디의 후기글을 가져오는 메서드
  @Override
  public ArticleDTO getArticleById(Long articleNo) {
    Optional<Article> optionalArticle = articleRepository.findById(articleNo);
    return optionalArticle.map(article -> {
      modelMapper.getConfiguration().setAmbiguityIgnored(true); // 충돌 무시 설정
      return modelMapper.map(article, ArticleDTO.class);
    }).orElse(null);
  }


  // 기존 후기글 업데이트 메서드
  // 수정 가능한 부분 : 제목, 내용
  @Override
  public ArticleDTO updateArticle(Long articleNo, ArticleDTO articleDTO) {
    Optional<Article> optionalArticle = articleRepository.findById(articleNo);
    if (optionalArticle.isEmpty()) {
      return null; // 혹은 예외를 던질 수도 있음
    }

    Article article = optionalArticle.get();

    Club club = clubRepository.findById(articleDTO.getClubNo().getClubNo())
        .orElseThrow(() -> new RuntimeException("해당 클럽을 찾을 수 없습니다"));

    article.setArticleTitle(articleDTO.getArticleTitle());
    article.setArticleContent(articleDTO.getArticleContent());

    Article updatedArticle = articleRepository.save(article);
    return modelMapper.map(updatedArticle, ArticleDTO.class);
  }


  // 특정 ID의 후기글을 삭제하는 메서드
  @Override
  public void deleteArticle(Long articleNo) {
    articleRepository.deleteById(articleNo);
  }
}




