package com.momo.momopjt.reply;

import com.momo.momopjt.article.Article;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {

  private Long replyNo;

  private User userNo;

  private String writer; // userNickName
  private String writerPhotoStr;

  private String replyContent;

  private Instant replyCreateDate;

  private Instant replyModifyDate;

  private Character replyState;

  private Integer replyLikeNumber;

  private Article articleNo;

  private Schedule scheduleNo;


}
