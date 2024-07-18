package com.momo.momopjt.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/article")
public class ArticleController {

  @Autowired
  private ArticleService articleService;

  // 새로운 후기글을 생성하는 엔드포인트
  @PostMapping
  public String createArticle(@ModelAttribute ArticleDTO articleDTO, Model model) {
    ArticleDTO createdArticle = articleService.createArticle(articleDTO);
    model.addAttribute("article", createdArticle);
    return "articleDetail"; // 생성된 후기글 상세 페이지로 리디렉션
  }

  // 모든 후기글을 가져오는 엔드포인트
  @GetMapping
  public String getAllArticles(Model model) {
    List<ArticleDTO> articles = articleService.getAllArticles();
    model.addAttribute("articles", articles);
    return "articleList"; // 모든 후기글 리스트 페이지로 리디렉션
  }

  // 특정 ID의 후기글을 가져오는 엔드포인트
  @GetMapping("/{articleNo}")
  public String getArticleById(@PathVariable Long articleNo, Model model) {
    ArticleDTO article = articleService.getArticleById(articleNo);
    model.addAttribute("article", article);
    return "articleDetail"; // 후기글 상세 페이지로 리디렉션
  }

  // 기존 후기글을 업데이트하는 엔드포인트
  @PostMapping("/{articleNo}/update")
  public String updateArticle(@PathVariable Long articleNo, @ModelAttribute ArticleDTO articleDTO, Model model) {
    ArticleDTO updatedArticle = articleService.updateArticle(articleNo, articleDTO);
    model.addAttribute("article", updatedArticle);
    return "articleDetail"; // 업데이트된 후기글 상세 페이지로 리디렉션
  }

  // 특정 ID의 Article을 삭제하는 엔드포인트
  @PostMapping("/{articleNo}/delete")
  public String deleteArticle(@PathVariable Long articleNo) {
    articleService.deleteArticle(articleNo);
    return "redirect:/articles"; // 후기글 리스트 페이지로 리디렉션
  }
}