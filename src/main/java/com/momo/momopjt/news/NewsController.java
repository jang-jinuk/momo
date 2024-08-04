package com.momo.momopjt.news;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {


  private final NewsService newsService;

  @GetMapping("/main") // 배포용 페이지 아님 (테스트용) 0802 YY
  public String newsMainGet(Model model) {
    log.info("----------------- [news/main getMapping]-----------------");
    List<News> newsList = newsService.readAllNews();
    log.info("----------------- [check news is loaded]-----------------");
    log.info(newsList);
    model.addAttribute("newsList",newsList);
    return "news/main";
  }

  @GetMapping("/read/{newsNo}")
  public String NewsReadGet(@PathVariable("newsNo") Long newsNo, Model model) {
    log.info("--------------- [news/read getMapping]---------------");
    News news = newsService.readNews(newsNo);
    model.addAttribute("news", news);
    return "news/read";
  }

  //공지사항 신규 생성
  @GetMapping("/create")
  public String newsCreateGet(Model model) {
    log.info("--------------- [news/create getMapping]---------------");
    return "news/create";
  }

  @PostMapping("/create")
  public String newsCreatePost(NewsDTO newsDTO){
    log.info("----------------- [news/create PostMapping]-----------------");

    // -1로 설정하면 자동 생성 추가됨.
    newsDTO.setNewsNo(-1L);

    //에러 방지용 임시
    newsDTO.setNewsCreateDate(Instant.now());

    newsService.createNews(newsDTO);

    return "redirect:/home";
  }

  @PostMapping("/update")
  public String newsUpdatePost(NewsDTO newsDTO){
    log.info("----------------- [news/update PostMapping]-----------------");
    newsDTO.setNewsCreateDate(newsService.readNews(newsDTO.getNewsNo()).getNewsCreateDate());

    log.info(newsDTO.toString()+"--------------");
    newsService.updateNews(newsDTO);
    return "redirect:/news/read/" + newsDTO.getNewsNo();
  }

  @PostMapping("/delete/")
  public String newsDeletePost(Long newsNo){
    log.info("----------------- [news/delete postMapping]-----------------");
    newsService.deleteNews(newsNo);
    log.info(newsNo.toString()+"--------------deleted");
    return "redirect:../main";
  }



}
