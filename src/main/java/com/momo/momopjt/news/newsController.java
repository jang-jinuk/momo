package com.momo.momopjt.news;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/news")
@RequiredArgsConstructor
public class newsController {


  private final NewsService newsService;

  @GetMapping("/main")
  public String main(Model model) {
    log.info("----------------- [news/main getMapping]-----------------");
    List<News> newsList = newsService.readAllNews();
    log.info("----------------- [check news is loaded]-----------------");
    log.info(newsList);
    model.addAttribute("newsList",newsList);
    return "news/main";
  }


}
