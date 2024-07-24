package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.reply.Reply;
import com.momo.momopjt.reply.ReplyService;
import com.momo.momopjt.schedule.ScheduleDTO;
import com.momo.momopjt.schedule.ScheduleService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/article")
public class ArticleController {

  @Autowired
  private ArticleService articleService;
  @Autowired
  private ScheduleService scheduleService;
  @Autowired
  private UserService userService;
  @Autowired
  private ReplyService replyService;


  // 새로운 후기글 작성 폼을 보여주는 페이지
  @GetMapping("/create")
  public String showCreateForm(@RequestParam(value = "scheduleNo", required = false)Long scheduleNo, Model model) {
    if (scheduleNo != null) {
      ScheduleDTO scheduleDTO = scheduleService.readOneSchedule(scheduleNo);
      model.addAttribute("scheduleDTO", scheduleDTO);
    }
    log.info("-------- [Article Create]-------you");
    return "article/create";
  }

  // 새로운 후기글을 생성하는 메서드
  @PostMapping("/create")
  public String createArticle(@ModelAttribute ArticleDTO articleDTO, HttpSession session) {
    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    articleDTO.setUserNo(user);

    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo);
    articleDTO.setClubNo(club);

    articleService.createArticle(articleDTO);
    return "redirect:/article/list"; // 생성 후 후기글 목록 페이지로 리디렉션
  }

  // 특정 아이디의 후기글을 보여주는 페이지
  @GetMapping("/{articleNo}")
  public String getArticleById(@PathVariable Long articleNo, Model model) {
    log.info("-------- [get ArticleById : {}]-------you",articleNo);
    ArticleDTO articleDTO = articleService.getArticleById(articleNo);
    model.addAttribute("articleDTO", articleDTO);
    model.addAttribute("articleNo", articleNo);

    //출력할 댓글 조회 YY
    List<Reply> replyList = replyService.readReplyAllByArticle(articleNo);

    //출력할 댓글 추가 YY
    model.addAttribute("replyList",replyList);

    return "article/detail"; // "articles/detail.html" 뷰를 반환
  }

  // 기존 후기글 수정 폼을 보여주는 페이지
  @GetMapping("/update/{articleNo}")
  public String showEditForm(@PathVariable Long articleNo, Model model) {
    log.info("-------- [get article edit]-------you");
    ArticleDTO article = articleService.getArticleById(articleNo);
    model.addAttribute("articleDTO", article);
    return "article/update"; // "article/update.html" 뷰를 반환
  }

  // 기존 후기글을 업데이트하는 메서드
  @PostMapping("/update/{articleNo}")
  public String updateArticle(@PathVariable Long articleNo, @ModelAttribute ArticleDTO articleDTO) {
    log.info("-------- [article update]-------you");
    articleService.updateArticle(articleNo, articleDTO);
    return "redirect:/article/list"; // 수정 후 후기글 목록 페이지로 리디렉션
  }

  // 기존 후기글을 삭제하는 메서드
  @PostMapping("/delete/{articleNo}")
  public String deleteArticle(@PathVariable Long articleNo) {
    log.info("-------- [article delete] -------");
    articleService.deleteArticle(articleNo);
    return "redirect:/article/list"; // 삭제 후 글 목록 페이지로 리디렉션
  }

}