package com.momo.momopjt.global.security;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class KakaoOAuth2UserInfo {
  private String id;
  private String email;

  public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

    this.id = String.valueOf(attributes.get("id"));
    this.email = (String) kakaoAccount.get("email");

  }



}
