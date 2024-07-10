package com.momo.momopjt.global.security;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSecurityService userSecurityService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest.....");
        log.info(userRequest);

        // 부모 클래스의 loadUser 메서드를 호출하여 OAuth2User를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2 제공자(구글, 카카오 등)의 이름을 가져옴
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 제공자의 사용자 정보를 기반으로 이메일 주소를 가져옴
        String username = oAuth2User.getAttribute("kakao_account.email"); // OAuth2 제공자에 따라 다를 수 있음
        String email = oAuth2User.getAttribute("email");
        String kakaoId = null;
        if (provider.equals("kakao")) {
            email = getKakaoEmail(oAuth2User.getAttributes());
            kakaoId = getKakaoId(oAuth2User.getAttributes());
        }

        // 사용자 정보를 기반으로 UserSecurityDTO 객체를 생성
        UserSecurityDTO userSecurityDTO = userSecurityService.generateDTO(kakaoId, email, oAuth2User.getAttributes(), provider.charAt(0));

        // DefaultOAuth2User 객체를 생성하여 반환
        return new DefaultOAuth2User(
            userSecurityDTO.getAuthorities(),
            oAuth2User.getAttributes(),
            "id" // OAuth2 제공자의 주요 키 (예: Google은 "sub", Kakao는 "id" 등)
        );
    }

    // 카카오 OAuth2 응답에서 이메일 주소를 추출하는 메서드
    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("KAKAO -------------------------");

        Object value = paramMap.get("kakao_account");
        log.info(value);

        if (value instanceof Map) {
            Map<String, Object> accountMap = (Map<String, Object>) value;
            String email = (String) accountMap.get("email");
            log.info("email...." + email);
            return email;
        }
        return null;
    }

    // 카카오 OAuth2 응답에서 ID를 추출하는 메서드
    private String getKakaoId(Map<String, Object> paramMap) {
        log.info("KAKAO ID -------------------------");

        String id = String.valueOf(paramMap.get("id"));
        log.info("id...." + id);
        return id;
    }
}