package com.momo.momopjt.reply;

import java.util.List;

public interface ReplyService {

  void createReply(ReplyDTO replyDTO);

  Reply readReply(Long replyNo);

  List<Reply> readReplyAll();

  List<Reply> readReplyAllBySchedule(Long scheduleNo);
  List<Reply> readReplyAllByArticle(Long articleNo);

  void updateReply(ReplyDTO replyDTO);

  void deleteReply(Long replyNo);

}
