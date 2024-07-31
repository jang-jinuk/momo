package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.club.ClubRepository;
import com.momo.momopjt.reply.ReplyDTO;
import com.momo.momopjt.reply.ReplyService;
import com.momo.momopjt.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ArticleServiceImpl implements ArticleService {


  private final ArticleRepository articleRepository;
  private final ClubRepository clubRepository;
  private final ReplyService replyService;

  private final ModelMapper modelMapper;

  //새로운 후기글을 생성하는 메서드
  @Override
  public Long createArticle(ArticleDTO articleDTO) {
    articleDTO.setArticleNo(-1L);
    articleDTO.setArticleCreateDate(Instant.now());
    articleDTO.setArticleState('0');
    articleDTO.setArticleScore(0);
    Article article = modelMapper.map(articleDTO, Article.class);
    article = articleRepository.save(article);
    return article.getArticleNo();
  }


 //모임의 모든 후기글 가져오는 메서드
 @Override
 public List<ArticleDTO> getAllArticlesByClub(Club clubNo) {
   List<Article> articles = articleRepository.findByClubNo(clubNo);
   List<ArticleDTO> articleDTOS = articles.stream()
       .map(article -> modelMapper.map(article, ArticleDTO.class))
       .collect(Collectors.toList());

   return articleDTOS;
 }

 //유저의 후기글 가져오는 메서드
  @Override
  public List<ArticleDTO> getAllArticlesByUser(User userNo) {
    List<Article> articles = articleRepository.findByUserNo(userNo);
    List<ArticleDTO> articleDTOS = articles.stream()
        .map(article -> modelMapper.map(article, ArticleDTO.class))
        .collect(Collectors.toList());
    return articleDTOS;
  }


 // 특정 아이디의 후기글을 1개 가져오는 메서드
  @Override
  public ArticleDTO getArticleById(Long articleNo) {

    boolean check = articleRepository.existsById(articleNo);

    if(check) {
      Optional<Article> optionalArticle = articleRepository.findById(articleNo);

      return optionalArticle.map(article -> {
        modelMapper.getConfiguration().setAmbiguityIgnored(true); // 충돌 무시 설정
        return modelMapper.map(article, ArticleDTO.class);
      }).orElse(null);
    }
    return null;

  }


  // 기존 후기글 업데이트 메서드
  // 수정 가능한 부분 : 제목, 내용
  @Override
  public ArticleDTO updateArticle(Long articleNo, ArticleDTO articleDTO) {
    Optional<Article> optionalArticle = articleRepository.findById(articleNo);
    if (optionalArticle.isEmpty()) {
      return null; // Article이 없는 경우, null을 반환합니다.
    }

    Article article = optionalArticle.get();

    // 클럽 정보가 있는 경우에만 클럽을 업데이트합니다.
    if (articleDTO.getClubNo() != null && articleDTO.getClubNo().getClubNo() != null) {
      Optional<Club> optionalClub = clubRepository.findById(articleDTO.getClubNo().getClubNo());
      if (optionalClub.isPresent()) {
        // 클럽 정보가 있는 경우, 클럽 관련 처리
        Club club = optionalClub.get();
        // 클럽과 관련된 추가 처리를 여기에 추가할 수 있습니다.
      }
      // 클럽 정보가 없는 경우, 클럽 관련 업데이트를 하지 않거나 기본 처리를 할 수 있습니다.
    }

    // 업데이트할 필드 설정
    article.setArticleTitle(articleDTO.getArticleTitle());
    article.setArticleContent(articleDTO.getArticleContent());
    // 필요한 경우 다른 필드를 업데이트할 수 있습니다.

    // 변경된 데이터를 데이터베이스에 저장
    Article updatedArticle = articleRepository.save(article);

    // DTO로 변환하여 반환
    return modelMapper.map(updatedArticle, ArticleDTO.class);
  }

  // 특정 ID의 후기글을 삭제하는 메서드
  @Override
  public void deleteArticle(Long articleNo) {
    List<ReplyDTO> replyDTOList = replyService.readReplyAllByArticle(articleNo);

    for (ReplyDTO replyDTO : replyDTOList) { //해당 후기글 댓글 삭제
      replyService.deleteReply(replyDTO.getReplyNo());
    }

    articleRepository.deleteById(articleNo);
  }



}




