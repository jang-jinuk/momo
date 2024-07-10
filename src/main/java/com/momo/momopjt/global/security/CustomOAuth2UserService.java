package com.momo.momopjt.global.security;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserRole;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.momo.momopjt.user.UserSecurityDTO;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSecurityService userSecurityService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2UserRequest: {}", userRequest);

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자 정보 디버깅 로그 추가
        Map<String, Object> attributes = oAuth2User.getAttributes();
        attributes.forEach((key, value) -> {
            log.info("Attribute - {}: {}", key, value);
        });

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = null;
        String id = null;

        switch (provider) {
            case "kakao":
                email = getKakaoEmail(oAuth2User.getAttributes());
                id = getKakaoId(oAuth2User.getAttributes());
                break;
            case "google":
                email = oAuth2User.getAttribute("email");
                id = oAuth2User.getAttribute("sub");
                break;
            default:
                throw new OAuth2AuthenticationException("Unknown provider: " + provider);
        }

        log.info("Provider: {}, Email: {}, ID: {}", provider, email, id);

        if (email == null || id == null) {
            throw new OAuth2AuthenticationException("Email or ID not found from provider: " + provider);
        }

        UserSecurityDTO userSecurityDTO = userSecurityService.generateDTO(id, email, oAuth2User.getAttributes(), provider.charAt(0));

        Map<String, Object> modifiedAttributes = new HashMap<>(oAuth2User.getAttributes());
        modifiedAttributes.put("id", id); // 'id' 속성을 추가합니다.

        return new DefaultOAuth2User(
            userSecurityDTO.getAuthorities(),
            modifiedAttributes,
            "id" // 사용자 ID 속성의 이름
        );
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("KAKAO -------------------------");

        Object value = paramMap.get("kakao_account");
        log.info("Kakao Account: {}", value);

        if (value instanceof Map) {
            Map<String, Object> accountMap = (Map<String, Object>) value;
            String email = (String) accountMap.get("email");
            log.info("Kakao Email: {}", email);
            return email;
        }
        return null;
    }

    private String getKakaoId(Map<String, Object> paramMap) {
        log.info("KAKAO ID -------------------------");

        String id = String.valueOf(paramMap.get("id"));
        log.info("Kakao ID: {}", id);
        return id;
    }
}