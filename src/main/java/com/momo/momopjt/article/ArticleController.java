package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.reply.ReplyDTO;
import com.momo.momopjt.reply.ReplyService;
import com.momo.momopjt.schedule.ScheduleDTO;
import com.momo.momopjt.schedule.ScheduleService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/article")
@Log4j2
public class ArticleController {

  @Autowired
  private ArticleService articleService;
  @Autowired
  private ScheduleService scheduleService;
  @Autowired
  private UserService userService;
  @Autowired
  private ReplyService replyService;
  @Autowired
  private PhotoService photoService;


  // 새로운 후기글 작성 폼을 보여주는 페이지
  @GetMapping("/create")
  public String createArticleGet(@RequestParam(value = "scheduleNo", required = false)Long scheduleNo, Model model, HttpSession session) {
    log.info("----------------- [GET article /create]-----------------");

    //마감된 일정에서 '후기작성'을 통해 넘어온 경우
    if (scheduleNo != null) {
      ScheduleDTO scheduleDTO = scheduleService.readOneSchedule(scheduleNo);
      model.addAttribute("scheduleDTO", scheduleDTO);
    }

    //후기글 작성자가 참가했던 일정 조회
    Long club = (Long) session.getAttribute("clubNo");
    Club clubNo = new Club();
    clubNo.setClubNo(club);
    User userNo = userService.getCurrentUser();

    List<ScheduleDTO> participatedSchedules = scheduleService.readMyParticipatedSchedules(clubNo, userNo);
    model.addAttribute("participatedSchedules",participatedSchedules);

    log.info("-------- [Article Create]-------you");
    return "article/create";
  }

  // 새로운 후기글을 생성하는 메서드
  @PostMapping("/create")
  public String createArticlePost(@ModelAttribute ArticleDTO articleDTO, HttpSession session,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    log.info("----------------- [POST article /create]-----------------");

    //후기글 기본 사진 설정
    if(articleDTO.getArticlePhotoUUID() == null || articleDTO.getArticlePhotoUUID().isEmpty()){
      articleDTO.setArticlePhotoUUID("ArticleDefaultPhoto");
    }

    if (bindingResult.hasErrors()) {
      log.info(" article has error -----------------");
      redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
      return "redirect:/article/create";
    }

    // 현재 로그인 유저 정보
    User user = userService.getCurrentUser();
    articleDTO.setUserNo(user);

    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo);
    articleDTO.setClubNo(club);

    //후기글 기본 사진 설정



    //실제 후기글 작성 로직
    Long articleNo = articleService.createArticle(articleDTO);

    redirectAttributes.addFlashAttribute("result", articleNo);

    return "redirect:/article/" + articleNo;
  }

  // 특정 아이디의 후기글을 보여주는 페이지
  @GetMapping("/{articleNo}")
  public String getArticleById(@PathVariable Long articleNo, Model model) {
    log.info("-------- [GET ArticleById /{articleNo}]-------you");

    //출력할 게시글 조회
    ArticleDTO articleDTO = articleService.getArticleById(articleNo);
    //출력할 댓글 조회 YY
    List<ReplyDTO> replyList = replyService.readReplyAllByArticle(articleNo);

    model.addAttribute("articleDTO", articleDTO);
    //출력할 댓글 추가 YY
    model.addAttribute("replyList",replyList);
    //출력할 사진 조회 YY
    String articleUserPhoto = photoService.getPhoto(articleDTO.getUserNo().getUserPhoto()).toString();
    //출력할 사진 추가 YY
    String articlePhoto = photoService.getPhoto(articleDTO.getArticlePhotoUUID()).toString();
    model.addAttribute("articlePhoto",articlePhoto);
    model.addAttribute("userPhoto",articleUserPhoto);

    return "article/detail"; // "articles/detail.html" 뷰를 반환
  }

  // 기존 후기글 수정 폼을 보여주는 페이지
  @GetMapping("/update/{articleNo}")
  public String showEditForm(@PathVariable Long articleNo, Model model) {
    log.info("-------- [GET article update/ ]-------you");
    ArticleDTO articleDTO = articleService.getArticleById(articleNo);
    model.addAttribute("articleDTO", articleDTO);

    //출력할 일정 사진 추가
    String articlePhoto = photoService.getPhoto(articleDTO.getArticlePhotoUUID()).toString();
    model.addAttribute("articlePhoto", articlePhoto);
    log.trace("--------------------------------articlePhoto {}", articlePhoto);


    return "article/update"; // "article/update.html" 뷰를 반환
  }

  // 기존 후기글을 업데이트하는 메서드
  @PostMapping("/update")
  public String updateArticle(@Valid @ModelAttribute ArticleDTO articleDTO,
                              RedirectAttributes redirectAttributes) {
    log.info("-------- [POST article update]-------you");

    Long articleNo = articleDTO.getArticleNo();
    log.warn(articleNo);
    log.info("----------------- [08-01 15:04:40]-----------------");
    log.warn(articleDTO.getArticlePhotoUUID());

    articleService.updateArticle(articleNo, articleDTO);

    redirectAttributes.addFlashAttribute("result", "modified");
    redirectAttributes.addAttribute("articleNo", articleNo);

    return "redirect:/article/" + articleNo; // 수정 후 후기글 목록 페이지로 리디렉션
  }


  // 기존 후기글을 삭제하는 메서드
  @GetMapping("/delete/{articleNo}")
  public String deleteArticle(@PathVariable Long articleNo, HttpSession session, RedirectAttributes redirectAttributes) {
    log.info("-------- [POST article /delete] -------");
    Long clubNo = (Long) session.getAttribute("clubNo");
    articleService.deleteArticle(articleNo);
    redirectAttributes.addFlashAttribute("message", "후기글이 삭제되었습니다.");
    return "redirect:/club/main/" + clubNo; // 삭제 후 글 목록 페이지로 리디렉션
  }

}
