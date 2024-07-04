package com.momo.momopjt.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/join")
@RequiredArgsConstructor
@Log4j2
@Validated
public class UserJoinController {

  @PostMapping("/register")
  public ResponseEntity<UserJoinDTO> registerUser(
      @Valid @RequestBody UserJoinDTO userJoinDTO) {
    // 회원가입 로직 처리 (예: 서비스 호출)
    return new ResponseEntity<>(userJoinDTO, HttpStatus.CREATED);
  }
}