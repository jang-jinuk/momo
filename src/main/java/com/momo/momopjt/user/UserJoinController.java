package com.momo.momopjt.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping("/join")
@RequiredArgsConstructor
@Log4j2
@Validated

public class UserJoinController {

  @PatchMapping("/userId")
  public ResponseEntity userid(
      @PathVariable("user-id") @Positive String userId,
      @Valid @RequestBody UserJoinDTO userJoinDTO) {

    userJoinDTO.setUserId(userId);
    return new ResponseEntity<>(userJoinDTO, HttpStatus.OK);
  }
}
