package com.momo.momopjt.global.security;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserSecurityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public CustomUserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  //TODO loadUserByUserName override : db 로그인 사용 UserDetailsService 을 Impl 0716 YY
  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

    log.info("loadUserByUsername: " + userId);

    // socialTypes 컬렉션 생성
//    Collection<Character> socialTypes = Arrays.asList('K', 'N', 'G', 'M');
    List<Character> socialTypes = List.of('K', 'N', 'G', 'M');
    //TODO 위 class 확인해 보면 ArrayList 타입으로 생성되는데 정확하게 클래스 매칭해주는게 좋을듯 0716 YY +
    // 추가로 JPQL에서도 List 문제없음 (UserRepository @Query


    // findByUserIdAndUserSocialIn 메서드 호출
    Optional<User> result = userRepository.findByUserIdAndUserSocialIn(userId, socialTypes);

    if (result.isEmpty()) { // 해당 아이디를 가진 사용자가 없다면
      throw new UsernameNotFoundException("userId not found...");
    }

    User user = result.get();

    UserSecurityDTO userSecurityDTO = new UserSecurityDTO(
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