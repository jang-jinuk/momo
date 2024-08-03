package com.momo.momopjt.home;

import com.momo.momopjt.club.ClubDTO;
import com.momo.momopjt.club.ClubService;
import com.momo.momopjt.news.News;
import com.momo.momopjt.news.NewsService;
import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log4j2
public class HomeController {

  @Autowired
  private ClubService clubService;
  @Autowired
  private UserService userService;
  @Autowired
  private PhotoService photoService;
  @Autowired
  private NewsService newsService;

  @GetMapping("/home")
  public String home(Model model) {

    // 현재 로그인된 사용자 정보를 얻기
    User user = userService.getCurrentUser();

    List<ClubDTO> myClubDTOList = clubService.readMyClubs(user);

    List<String> myClubPhotoList = myClubDTOList.stream()
        .map(clubDTO -> photoService.getPhoto(clubDTO.getClubPhotoUUID()).toString())
        .collect(Collectors.toList());

    log.info("----------------- [myclubPhotoList]-----------------{}", myClubPhotoList);

    model.addAttribute("myClubDTOList", myClubDTOList);
    model.addAttribute("myClubPhotoList", myClubPhotoList);


    List<ClubDTO> clubDTOList = clubService.readAllClub();
    List<String> clubPhotoList = clubDTOList.stream()
        .map(clubDTO -> photoService.getPhoto(clubDTO.getClubPhotoUUID()).toString())
        .collect(Collectors.toList());


    model.addAttribute("clubDTOList", clubDTOList);
    model.addAttribute("clubPhotoList", clubPhotoList);
    log.trace("----------------- [clubdtolist : {}]-----------------", clubDTOList);
    log.trace("----------------- [clubPhotoList : {}}]-----------------", clubPhotoList);

    //프사 추가 (비활성화) aop?

    if(user != null ) {
      String userPhoto = photoService.getPhoto(user.getUserPhoto()).toString();
      model.addAttribute("userPhoto", userPhoto);
    }
    //공지사항 추가

    List<News> newsList = newsService.readAllNews();
    Collections.reverse(newsList); // 최신순 정렬
    model.addAttribute("newsList", newsList);


      return "home"; // 홈 페이지의 Thymeleaf 템플릿 이름
    }
    return "home";
  }
}
