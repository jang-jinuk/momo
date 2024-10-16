package com.momo.momopjt.global;

import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserPhotoInterceptor implements HandlerInterceptor {

  @Autowired
  private UserService userService;

  @Autowired
  private PhotoService photoService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    User user = userService.getCurrentUser();
    if (user != null) {
      String userPhoto = photoService.getPhoto(user.getUserPhoto()).toString();
      request.setAttribute("userPhoto", userPhoto);
    }
    return true;
  }
}