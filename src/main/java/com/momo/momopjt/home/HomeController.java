package com.momo.momopjt.home;

import com.momo.momopjt.club.ClubDTO;
import com.momo.momopjt.club.ClubService;
import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

  @GetMapping("/home")
  public String home(Model model) {

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);


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
    log.trace("----------------- [clubdtolist : {}]-----------------",clubDTOList);
    log.trace("----------------- [clubPhotoList : {}}]-----------------",clubPhotoList);


    return "home"; // 홈 페이지의 Thymeleaf 템플릿 이름

}
}
