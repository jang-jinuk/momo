package com.momo.momopjt.global.security;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserRole;
import com.momo.momopjt.user.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSecurityService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserSecurityDTO generateDTO(String userId, String email, Map<String, Object> params, char userSocial) {
    // 이메일 주소로 사용자 정보 조회
    Optional<User> result = userRepository.findByUserEmail(email);
    User user;
    if (result.isEmpty()) {
      // 사용자가 존재하지 않으면 새 사용자 정보를 저장
      user = User.builder()
          .userId(userId)
          .userPw(passwordEncoder.encode("1111"))
          .userEmail(email)
          .userPhoto("UserDefaultPhoto")
          .userSocial(userSocial)
          .build();

      user.addRole(UserRole.USER);
      userRepository.save(user);
    } else {
      // 이미 존재하는 사용자의 경우 해당 정보를 가져옴
      user = result.get();

      // 기존 사용자도 기본 역할 설정
      if (user.getRoleSet().isEmpty()) {
        user.addRole(UserRole.USER); // 기본 역할 할당 (USER 역할 예시)
        userRepository.save(user); // 역할을 추가한 후에 저장
      }

    }

    // UserSecurityDTO 객체를 생성하여 반환
    UserSecurityDTO userSecurityDTO = new UserSecurityDTO(
        user.getUserPhoto(),
        user.getUserId(),
        user.getUserPw(),
        user.getUserEmail(),
        true, // enabled 상태
        user.getUserSocial(),
        user.getRoleSet().stream()
            .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.name()))
            .collect(Collectors.toList())
    );
    userSecurityDTO.setProps(params);
    return userSecurityDTO;
  }
}