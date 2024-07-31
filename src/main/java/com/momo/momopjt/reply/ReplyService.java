package com.momo.momopjt.reply;

import java.util.List;

public interface ReplyService {

  void createReply(ReplyDTO replyDTO);

  Reply readReply(Long replyNo);

  List<ReplyDTO> readReplyAll();

  List<ReplyDTO> readReplyAllBySchedule(Long scheduleNo);
  List<ReplyDTO> readReplyAllByArticle(Long articleNo);

  void updateReply(ReplyDTO replyDTO);

  void deleteReply(Long replyNo);

  boolean checkReplyExist(Long replyNo);
}
