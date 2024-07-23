/*
 *
 *   임시 파일 ! 나중에 Schedule 에서 처리할 예정
 *
 */
package com.momo.momopjt.reply;

import com.momo.momopjt.article.ArticleService;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.schedule.ScheduleDTO;
import com.momo.momopjt.schedule.ScheduleService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ReplyController {


  private final UserService userService;
  private final ScheduleService  scheduleService;
  private final ArticleService artcleService;
  private final ReplyService replyService;
  private final ModelMapper modelMapper;

  @PostMapping("/reply/create")
    String createReplyPost(ReplyDTO replyDTO, HttpServletRequest request) {
    log.info("----------------- [POST /reply/create]-----------------");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = userService.findByUserId(auth.getName());

    /*  확인용 로그
    if(scheduleService.readOneSchedule(replyDTO.getScheduleNo().getScheduleNo()) != null) {
      log.info("----------------- [schedule found]-----------------");
    }
    if(artcleService.getArticleById(replyDTO.getArticleNo().getArticleNo()) != null) {
      log.info("----------------- [article found]-----------------");
    }*/
    
    replyDTO.setReplyNo(-1L); //중복코드임 나중에 확인후처리
    replyDTO.setReplyCreateDate(Instant.now());
    replyDTO.setReplyLikeNumber(0);
    replyDTO.setReplyState('0');
    replyDTO.setUserNo(user);

    ScheduleDTO s = scheduleService.readOneSchedule(replyDTO.getScheduleNo().getScheduleNo());
    Schedule schedule = modelMapper.map(s, Schedule.class);
    replyDTO.setScheduleNo(schedule);
    //test
//    Article article = new Article();
//    article.setArticleNo(-1L);
//    replyDTO.setArticleNo(article);

    replyService.createReply(replyDTO);

    log.info("----------------- [user info : ]-----------------{},{},{},",user.getUserNo(),user.getUserId(),user.getUserEmail());
    String referer = request.getHeader("Referer");
    log.info("----------------- [end of reply create]-----------------");
    return "redirect:"+referer;
  }

  @PostMapping("/reply/delete")
  String deleteReplyPost(Long replyNo, HttpServletRequest request) {
    log.info("----------------- [POST /reply/delete]-----------------");
    replyService.deleteReply(replyNo);
    String referer = request.getHeader("Referer");
    return "redirect:"+referer;
  }


}
