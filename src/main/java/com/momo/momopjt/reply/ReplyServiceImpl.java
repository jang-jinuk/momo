package com.momo.momopjt.reply;

import com.momo.momopjt.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {

  private final UserService userService;
  private final ReplyRepository replyRepository;
  private final ModelMapper modelMapper;

  @Override
  public void createReply(ReplyDTO replyDTO) {
    log.info("----------------- [createReply]-----------------");
    Reply reply = modelMapper.map(replyDTO, Reply.class);

    replyDTO.setReplyNo(-1L);

    replyRepository.save(reply);

  }

  @Override
  public Reply readReply(Long replyNo) {
    log.info("----------------- [readReply]-----------------");
    Reply reply = replyRepository.findById(replyNo).orElseThrow();
    return reply;

  }

  @Override
  public List<ReplyDTO> readReplyAll() {
    log.info("----------------- [readReplyAll]-----------------");
    List<Reply> replyList = replyRepository.findAll();

    List<ReplyDTO> replyDTOList = new ArrayList<>();
    for (Reply reply : replyList) {
      ReplyDTO replyDTO = modelMapper.map(reply, ReplyDTO.class);
      replyDTO.setWriter(userService.findByUserNo(reply.getUserNo().getUserNo()).orElseThrow().getUserNickname());
      replyDTOList.add(replyDTO);
    }

    log.info("replyDTOList: {}", replyDTOList);
    return replyDTOList;
  }

  @Override
  public List<ReplyDTO> readReplyAllBySchedule(Long scheduleNo) {
    log.info("----------------- [readReplyAllBySchedule]-----------------");
    List<ReplyDTO> replyByScheduleList = readReplyAll()
        .stream()
        .filter(reply -> reply.getScheduleNo() != null && reply.getScheduleNo().getScheduleNo() == scheduleNo)
        .collect(Collectors.toList());
    return replyByScheduleList;
  }

  @Override
  public List<ReplyDTO> readReplyAllByArticle(Long articleNo) {
    log.info("----------------- [readReplyAllByArticle]-----------------");
    List<ReplyDTO> replyByArticleList = readReplyAll()
        .stream()
        .filter(reply -> reply.getArticleNo() != null && reply.getArticleNo().getArticleNo() == articleNo)
        .collect(Collectors.toList());
    return replyByArticleList;
  }

  @Override
  public void updateReply(ReplyDTO replyDTO) {
    log.info("----------------- [updateReply]-----------------");
    //check exist
    Reply reply = replyRepository.findById(replyDTO.getReplyNo()).orElseThrow();
    log.info("check exist before update--");
    log.info("----------------- [{}, {}, {}]-----------------",
        reply.getReplyNo(), reply.getReplyContent(), reply.getReplyCreateDate());

    reply = modelMapper.map(replyDTO, Reply.class);
    replyRepository.save(reply);
  }

  @Override
  public void deleteReply(Long replyNo) {
    log.info("----------------- [deleteReply]-----------------");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String name = auth.getName();

    if (checkReplyExist(replyNo) && userService.findByUserId(name) == readReply(replyNo).getUserNo()) {
      replyRepository.deleteById(replyNo);
    } else {
      log.warn("no exist reply or no Auth to delete");
    }
  }

  @Override
  public void forceDeleteReply(Long replyNo) {
    log.info("----------------- [forceDeleteReply]-----------------");

    replyRepository.deleteById(replyNo);
  }


  @Override
  public boolean checkReplyExist(Long replyNo) {
    if (replyRepository.existsById(replyNo)) {
      log.info("-------- [reply exist]-------");
      return true;
    }
    log.warn("---------[reply not exist]--------");
    return false;
  }
}
