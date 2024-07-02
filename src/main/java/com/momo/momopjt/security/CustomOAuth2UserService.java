package com.momo.momopjt.security;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserRole;
import com.momo.momopjt.user.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String username = oAuth2User.getAttribute("kakao_account.email"); // OAuth2 제공자에 따라 다를 수 있음
        String email = oAuth2User.getAttribute("email");
        if (provider.equals("kakao")) {
            email = getKakaoEmail(oAuth2User.getAttributes());
        }

        UserSecurityDTO userSecurityDTO = generateDTO(email, oAuth2User.getAttributes(), provider.charAt(0));

        return new DefaultOAuth2User(
                userSecurityDTO.getAuthorities(),
                oAuth2User.getAttributes(),
            "kakao_account"// OAuth2 제공자의 주요 키 (예: Google은 "sub", Kakao는 "id" 등)
        );
    }

    private UserSecurityDTO generateDTO(String email, Map<String, Object> params, char userSocial) {

        Optional<User> result = userRepository.findByUserEmail(email);
        if (result.isEmpty()) {
            //회원추가 -- userId는 이메일주소/패스워드는 1111
            User user = User.builder()
                    .userId(email)
                    .userPw(passwordEncoder.encode("1111"))
                    .userEmail(email)
                    .userSocial(userSocial)
                    .build();
            user.addRole(UserRole.USER);
            userRepository.save(user);
            //UserSecurityDTO 구성 및 반환
            UserSecurityDTO userSecurityDTO = new UserSecurityDTO(email, "1111", email, true,
                    userSocial, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            userSecurityDTO.setProps(params);

            return userSecurityDTO;
        } else {
            User user = result.get();
            UserSecurityDTO userSecurityDTO =
                    new UserSecurityDTO(
                            user.getUserId(),
                            user.getUserPw(),
                            user.getUserEmail(),
                            true, // enabled 상태
                            user.getUserSocial(),
                            user.getRoleSet().stream().map(userRole ->
                                            new SimpleGrantedAuthority("ROLE_" + userRole.name()))
                                    .collect(Collectors.toList())
                    );
            userSecurityDTO.setProps(params);
            return userSecurityDTO;
        }
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("KAKAO -------------------------");

        Object value = paramMap.get("kakao_account");
        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");
        log.info("email...." + email);
        return email;
    }
}