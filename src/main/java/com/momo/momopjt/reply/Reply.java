package com.momo.momopjt.reply;

import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.article.Article;
import com.momo.momopjt.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reply")
public class Reply {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reply_no", nullable = false)
  private Long replyNo;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_no", nullable = false)
  private User userNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "schedule_no")
  private Schedule scheduleNo;

  @Column(name = "reply_content", nullable = false, length = 50)
  private String replyContent;

  @Column(name = "reply_create_date", nullable = false)
  private Instant replyCreateDate;

  @Column(name = "reply_modify_date")
  private Instant replyModifyDate;

  @Column(name = "reply_state", nullable = false)
  private Character replyState;

  @Column(name = "reply_like_number")
  private Integer replyLikeNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_no")
  private Article articleNo;

}