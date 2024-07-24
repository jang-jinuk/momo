package com.momo.momopjt.user;

import org.springframework.stereotype.Service;
// todo 0724 내려받았는데 알 수 없는 파일입니다 SW
@Service
public interface UserSerivce {
  User readUser(Long UserNo);
}
