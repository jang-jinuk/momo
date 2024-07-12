package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.club.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ArticleServiceImpl implements ArticleService {

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private ClubRepository clubRepository;

  // 새로운 후기글을 생성하는 메서드
  @Override
  public ArticleDTO createArticle(ArticleDTO articleDTO) {
    Article article = convertDTOToEntity(articleDTO);
    Article savedArticle = articleRepository.save(article);
    return convertEntityToDTO(savedArticle);
  }

  // 모든 후기글을 가져오는 메서드
  @Override
  public List<ArticleDTO> getAllArticles() {
    return articleRepository.findAll().stream()
        .map(this::convertEntityToDTO)
        .collect(Collectors.toList());
  }

  // 특정 ID의 후기글을 가져오는 메서드
  @Override
  public ArticleDTO getArticleById(Long articleNo) {
    return articleRepository.findById(articleNo)
        .map(this::convertEntityToDTO)
        .orElseThrow(() -> new RuntimeException("Article not found"));
  }


  // 기존 후기글을 업데이트하는 메서드
  @Override
  public ArticleDTO updateArticle(Long articleNo, ArticleDTO articleDTO) {
    Article article = articleRepository.findById(articleNo)
        .orElseThrow(() -> new RuntimeException("Article not found"));

    // 클럽 정보 가져오기
    Club club = clubRepository.findById(articleDTO.getClubNo())
        .orElseThrow(() -> new RuntimeException("Club not found"));

    // 후기글 업데이트 메서드 호출
    article.update(articleDTO, club);

    // 업데이트된 후기글 저장
    Article updatedArticle = articleRepository.save(article);
    return convertEntityToDTO(updatedArticle);
  }

  // 특정 ID의 Article을 삭제하는 메서드
  @Override
  public void deleteArticle(Long articleNo) {
    articleRepository.deleteById(articleNo);
  }


  // Article 엔티티를 후기글DTO로 변환하는 메서드
  private ArticleDTO convertEntityToDTO(Article article) {
    return new ArticleDTO(
        article.getArticleNo(),
        article.getArticleTitle(),
        article.getArticleContent(),
        article.getArticleCreateDate(),
        article.getArticleState(),
        article.getArticleScore(),
        article.getClubNo().getClubNo()
    );
  }

  // ArticleDTO를 후기글 엔티티로 변환하는 메서드
  private Article convertDTOToEntity(ArticleDTO articleDTO) {
    Club club = clubRepository.findById(articleDTO.getClubNo())
        .orElseThrow(() -> new RuntimeException("Club not found"));
    return new Article(
        articleDTO.getArticleNo(),
        articleDTO.getArticleTitle(),
        articleDTO.getArticleContent(),
        articleDTO.getArticleCreateDate(),
        articleDTO.getArticleState(),
        articleDTO.getArticleScore(),
        club
    );
  }
}
