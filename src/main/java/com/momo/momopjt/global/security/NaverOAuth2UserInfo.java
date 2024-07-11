package com.momo.momopjt.global.security;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class NaverOAuth2UserInfo {
  private Map<String, Object> attributes;

  public NaverOAuth2UserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  public String getId() {
    Object idObj = attributes.get("id");
    if (idObj instanceof String) {
      return (String) idObj;
    } else if (idObj instanceof Number) {
      return String.valueOf(idObj);
    }
    return null; // ID가 없거나 형식이 맞지 않는 경우 null 반환
  }

  public String getName() {
    return (String) attributes.get("name");
  }

  public String getEmail() {
    Object emailObj = attributes.get("email");
    if (emailObj instanceof String) {
      return (String) emailObj;
    }
    return null; // Email이 없거나 형식이 맞지 않는 경우 null 반환
  }
}