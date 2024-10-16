package com.momo.momopjt.reply;

import com.momo.momopjt.user.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;


@SpringBootTest
@Log4j2
class ReplyServiceTests {

  @Autowired
  private ReplyService replyService;

  @Autowired
  private ModelMapper modelMapper;


  @Test
  void createReply() {
    log.info("----------------- [TEST createReply]-----------------");

    //댓글 생성 위한 작성자 세팅 필요
    User user = User.builder()
        .userNo(1L)
        .build();

    //댓글 값 세팅
    ReplyDTO replyDTO = ReplyDTO.builder()
        .replyContent("댓글 내용")
        .replyCreateDate(Instant.now())
        .replyLikeNumber(321)
        .replyState('1')
        .userNo(user)
        .build();

    replyService.createReply(replyDTO);

    replyDTO.setReplyContent("댓글 내용2");
    replyService.createReply(replyDTO);

    replyDTO.setReplyContent("댓글 내용 3");
    replyService.createReply(replyDTO);

  }

  @Test
  void readReply() {
    log.info("----------------- [TEST readReply]-----------------");

    String content = replyService.readReply(1L).getReplyContent();
    log.info(content + "~댓글 내용 조회됨");

  }


  @Test
  void readReplyAll() {
    log.info("----------------- [TEST readAllReply]-----------------");

    int replyCnt = replyService.readReplyAll().size();
    log.info(replyCnt+"~개의 댓글 조회됨");

  }

  @Test
  void updateReply() {
    log.info("----------------- [TEST updateReply]-----------------");

    //1번 댓글 내용 2번에 업데이트 하는 식으로 테스트
    Reply reply = replyService.readReply(1L);

    ReplyDTO replyDTO = modelMapper.map(reply, ReplyDTO.class);

    replyDTO.setReplyNo(2L);

    replyService.updateReply(replyDTO);

  }

  @Test
  void deleteReply() {
    log.info("----------------- [TEST deleteReply]-----------------");

    replyService.deleteReply(3L);


  }

}