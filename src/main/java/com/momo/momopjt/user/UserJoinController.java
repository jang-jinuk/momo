package com.momo.momopjt.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping("/join")
@RequiredArgsConstructor
@Log4j2
@Validated
public class UserJoinController {

  @PostMapping("/register")
  public ResponseEntity<UserDTO> registerUser(
      @Valid @RequestBody UserDTO DTO) {

    // 회원가입 로직 처리 (예: 서비스 호출)

    return new ResponseEntity<>(DTO, HttpStatus.CREATED);
  }
}

