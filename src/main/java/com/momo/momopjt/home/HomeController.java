package com.momo.momopjt.home;

import com.momo.momopjt.club.ClubDTO;
import com.momo.momopjt.club.ClubService;
import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

  @GetMapping("/")
  public String home(Model model) {
    log.info("----------------- [homeController]-----------------");


    // 현재 로그인된 사용자 정보를 얻기
    User user = userService.getCurrentUser();
    if (user != null) {
      log.info("----------------- [정지유저 확인로직]-----------------{}",user.getUserState());
      model.addAttribute("state",user.getUserState());
    }

    List<ClubDTO> myClubDTOList = clubService.readMyClubs(user);

    List<String> myClubPhotoList = myClubDTOList.stream()
        .map(clubDTO -> photoService.getPhoto(clubDTO.getClubPhotoUUID()).toString())
        .collect(Collectors.toList());

    log.trace("----------------- [myclubPhotoList]-----------------{}", myClubPhotoList);

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

    //user nickname 표시를 위해 모델에 추가 0804 YY
    model.addAttribute("user", user);


    return "home"; // 홈 페이지의 Thymeleaf 템플릿 이름
  }
}


