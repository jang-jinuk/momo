package com.momo.momopjt.global.security;


import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: " + username);
        List<Character> socialTypes = Arrays.asList('K', 'N', 'G','M');
        // findByUserIdAndUserSocialIn 메서드 호출 (사용자 정보를 데이터베이스에서 조회하는기능.
        // 이때 사용자 ID와 소셜 로그인 타입(socialTypes)을 조건으로 사용)
        Optional<User> result = userRepository.findByUserIdAndUserSocialIn(username, socialTypes);
        if (result.isEmpty()) { // 해당 아이디를 가진 사용자가 없다면
            throw new UsernameNotFoundException("userId not found...");
            //조회 결과가 비어있다면 UsernameNotFoundException을 던져 사용자를 찾을 수 없음을 알린다.
        }
        User user = result.get();  //조회된 사용자 정보를 User 객체에 저장합니다
        //★★★ 빌더사용법 ★★★
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
        // 조회된 사용자 정보를 UserSecurityDTO 객체로 변환한다.
        // UserSecurityDTO는 Spring Security에서 사용자 인증 정보를 담는 DTO 클래스이다.
        // 여기에는 사용자 ID, 비밀번호, 이메일, 계정 활성화 여부, 소셜 로그인 타입, 권한 정보 등이 포함된다.
        log.info("userSecurityDTO: " + userSecurityDTO);

        return userSecurityDTO;
        //마지막으로 UserSecurityDTO 객체를 반환합니다.
        //이 코드는 Spring Security의 UserDetailsService 인터페이스를 구현하는 서비스 클래스의 일부이다.
        // UserDetailsService는 사용자 인증 정보를 제공하는 역할을한다.
    }
}