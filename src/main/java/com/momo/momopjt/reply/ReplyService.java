package com.momo.momopjt.reply;

import java.util.List;

public interface ReplyService {

  void createReply(ReplyDTO replyDTO);

  Reply readReply(Long replyNo);

  List<Reply> readReplyAll();

  void updateReply(ReplyDTO replyDTO);

  void deleteReply(Long replyNo);

}
