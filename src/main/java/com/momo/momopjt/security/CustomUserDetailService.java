package com.momo.momopjt.security;


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

        // socialTypes 컬렉션 생성
        Collection<Character> socialTypes = Arrays.asList('k', 'n', 'g', 'm');

        // findByUserIdAndUserSocialIn 메서드 호출
        Optional<User> result = userRepository.findByUserIdAndUserSocialIn(username, socialTypes);

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