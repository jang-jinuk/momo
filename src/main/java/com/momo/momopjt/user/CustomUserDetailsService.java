package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {

        log.info("loadUserByUsername: " + username);

        Optional<User> result = UserRepository.getWithRoles(username);

        if(result.isEmpty()){ //해당 아이디를 가진 사용자가 없다면
            throw new UsernameNotFoundException("username not found......");
        }

        User user = result.get();

        UserSecurityDTO userSecurityDTO =
                new UserSecurityDTO(
                        user.getUserId(),
                        user.getUserPw(),
                        user.getUserEmail(),
                        user.getUserState(),
                        false,
                        user.getRoleSet()
                                .stream().map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.name()))
                                .collect(Collectors.toList())
                );

        log.info("userSecurityDTO");
        log.info(userSecurityDTO);

        return userSecurityDTO;

    }
}
