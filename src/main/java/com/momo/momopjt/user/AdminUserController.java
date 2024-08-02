package com.momo.momopjt.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
@Transactional
public class AdminUserController {

  private final UserService userService;

  @GetMapping("/manage-user")
  public String manageUserGet(Model model,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "query", defaultValue = "") String query) {
    log.info("GET /manage-user");

    List<UserDTO> findUsers;
    if (query.isEmpty()) {
      findUsers = userService.readALLUsers();
    } else {
      findUsers = userService.searchUsers(query);
    }

    int totalUsers = findUsers.size();
    int pageSize = 10;
    int lastPage = (totalUsers + pageSize - 1) / pageSize;

    int fromIndex = (page - 1) * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, totalUsers);
    List<UserDTO> users = findUsers.subList(fromIndex, toIndex);

    int pageGroupSize = 10;
    int currentGroup = (page - 1) / pageGroupSize;
    int startPage = currentGroup * pageGroupSize + 1;
    int endPage = Math.min(startPage + pageGroupSize - 1, lastPage);

    model.addAttribute("UserDTO", users);
    model.addAttribute("page", page);
    model.addAttribute("lastPage", lastPage);
    model.addAttribute("query", query);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    log.trace("AdminUserController manage-User END");
    return "admin/manage-User";
  }

  @PostMapping("/change-userRole")
  public String changeUserrolePost(@RequestParam("userNo") Long userNo,
                                   @RequestParam("currentPage") int currentPage,
                                   @RequestParam("query") String query) throws UnsupportedEncodingException {
    log.info("...... [POST /change-userRole]..........KSW");
    UserDTO userDTO = new UserDTO(); // 타입에 맞게 객체를 생성하여
    userDTO.setUserNo(userNo);
    userService.chageRoleUser(userDTO); //기능으로 넘겨준다
    log.info("...... [권한부여]..........KSW");
    log.info("...... [{}]..........KSW",query);
    String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8); //쿼리를 인코더에 담아준다
    return "redirect:/admin/manage-user?page=" + currentPage + "&query=" + encodedQuery;
  }
}
