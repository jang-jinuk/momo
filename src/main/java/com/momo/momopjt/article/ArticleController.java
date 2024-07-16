package com.momo.momopjt.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {

  @Autowired
  private ArticleService articleService;

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<ArticleDTO>> getAllArticles() {
    return ResponseEntity.ok(articleService.getAllArticles());
  }

  @GetMapping("/{id}")
  @ResponseBody
  public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
    return ResponseEntity.ok(articleService.getArticleById(id));
  }

  @PostMapping
  @ResponseBody
  public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
    return ResponseEntity.ok(articleService.createArticle(articleDTO));
  }

  @PutMapping("/{id}")
  @ResponseBody
  public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
    return ResponseEntity.ok(articleService.updateArticle(id, articleDTO));
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
    articleService.deleteArticle(id);
    return ResponseEntity.noContent().build();
  }

}