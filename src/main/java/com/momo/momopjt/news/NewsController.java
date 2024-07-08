package com.momo.momopjt.news;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {


  private final NewsService newsService;

  @GetMapping("/main")
  public String newsMainGet(Model model) {
    log.info("----------------- [news/main getMapping]-----------------");
    List<News> newsList = newsService.readAllNews();
    log.info("----------------- [check news is loaded]-----------------");
    log.info(newsList);
    model.addAttribute("newsList",newsList);
    return "news/main";
  }

  @GetMapping("/read/{newsNo}")
  public String NewsReadGet(@PathVariable("newsNo") int newsNo, Model model) {
    log.info("--------------- [news/read getMapping]---------------");
    return "news/read";
  }

  @GetMapping("/update/{newsNo}")
  public String NewsUpdateGet(@PathVariable("newsNo") int newsNo, Model model) {
    log.info("--------------- [news/update getMapping]---------------");
    return "news/update";
  }

  @GetMapping("/create")
  public String newsCreateGet(Model model) {
    log.info("--------------- [news/create getMapping]---------------");
    return "news/create";
  }


  @PostMapping("/create")
  public String newsCreatePost(){
    log.info("----------------- [news/create PostMapping]-----------------");
    return null;
  }

  
  @PostMapping("/update/{newsNo}")
  public String newsUpdatePost(){
    log.info("----------------- [news/update PostMapping]-----------------");
    return null;
  }

  @PostMapping("/delete/{newsNo}")
  public String newsDeletePost(@PathVariable("newsNo") int newsNo){
    log.info("----------------- [news/delete postMapping]-----------------");
    return "sendredirect:news/main";
  }



}
