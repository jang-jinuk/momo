package com.momo.momopjt.user;

import org.springframework.stereotype.Service;

@Service
public interface UserSerivce {
  User readUser(Long UserNo);
}
