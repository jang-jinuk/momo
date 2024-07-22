package com.momo.momopjt.article;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/article")
@Log4j2
public class ArticleController {


  private final ArticleService articleService;

  @Autowired
  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  // 새로운 후기글 작성 폼을 보여주는 페이지
  @GetMapping("/create")
  public String showCreateForm(Model model) {
    log.info("...... [create/article]..........KSW");
    model.addAttribute("articleDTO", new ArticleDTO());
    return "article/create";
  }

  // 새로운 후기글을 생성하는 메서드
  @PostMapping("/create")
  public String createArticle(@ModelAttribute ArticleDTO articleDTO) {
    log.info("...... [create/articleaaasda]..........KSW");
    articleService.createArticle(articleDTO);
    return "redirect:/article"; // 생성 후 후기글 목록 페이지로 리디렉션
  }

  // 모든 후기글 목록을 보여주는 페이지
  @GetMapping("/list")
  public String getAllArticles(Model model) {
    List<ArticleDTO> articles = articleService.getAllArticles();
    model.addAttribute("article", articles);
    return "article/list";
  }

  // 특정 아이디의 후기글을 보여주는 페이지
  @GetMapping("/{articleNo}")
  public String getArticleById(@PathVariable Long articleNo, Model model) {
    ArticleDTO article = articleService.getArticleById(articleNo);
    model.addAttribute("article", article);
    return "article/detail"; // "articles/detail.html" 뷰를 반환
  }


  // 기존 후기글 수정 폼을 보여주는 페이지
  @GetMapping("/update/{articleNo}")
  public String showEditForm(@PathVariable Long articleNo, Model model) {
    ArticleDTO article = articleService.getArticleById(articleNo);
    model.addAttribute("articleDTO", article);
    return "article/update"; // "article/update.html" 뷰를 반환
  }



  // 기존 후기글을 업데이트하는 메서드
  @PostMapping("/update/{articleNo}")
  public String updateArticle(@PathVariable Long articleNo, @ModelAttribute ArticleDTO articleDTO) {
    articleService.updateArticle(articleNo, articleDTO);
     return "redirect:/article";
  }


  // 기존 후기글을 삭제하는 메서드
  @PostMapping("delete/{articleNo}")
  public String deleteArticle(@PathVariable Long articleNo) {
    articleService.deleteArticle(articleNo);
    return "redirect:/article"; // 삭제 후 후기글 목록 페이지로 리디렉션
  }

}
