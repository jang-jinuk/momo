package com.momo.momopjt.global.security;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserRole;
import com.momo.momopjt.user.UserSecurityDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;
  private static final String SOCIAL_LOGIN_PASSWORD = "1111";

  @Autowired
  public CustomUserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

    log.info("loadUserByUsername: " + userId);

    // socialTypes 컬렉션 생성
    Collection<Character> socialTypes = Arrays.asList('K', 'N', 'G', 'M');

    // findByUserIdAndUserSocialIn 메서드 호출
    Optional<User> result = userRepository.findByUserIdAndUserSocialIn(userId, socialTypes);

    if (result.isEmpty()) { // 해당 아이디를 가진 사용자가 없다면
      log.info("----------------- [07-24 20:17:10 123123123123123]-----------------");
      throw new UsernameNotFoundException("userId not found...");
    }

    User user = result.get();

    // 비밀번호가 null이거나 비어있으면 소셜 로그인 사용자로 처리
    boolean isSocialUser = user.getUserPw() == null || user.getUserPw().isEmpty();

    // 기본 역할 설정
    if (user.getRoleSet().isEmpty()) {
      user.addRole(UserRole.USER); // 기본 역할 할당 (USER 역할 예시)
      userRepository.save(user); // 역할을 추가한 후에 저장
    }

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

    log.info("userSecurityDTO: " + userSecurityDTO);
    return userSecurityDTO;
  }
}