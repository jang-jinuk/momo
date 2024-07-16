package com.momo.momopjt.global.security;

import com.momo.momopjt.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.*;

import com.momo.momopjt.user.UserSecurityDTO;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

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
                id = getKakaoId(oAuth2User);
                break;
            case "google":
                email = oAuth2User.getAttribute("email");
                id = oAuth2User.getAttribute("sub");
                break;
            case "naver":
                email = getNaverEmail(oAuth2User.getAttributes());
                id = getNaverId(oAuth2User);
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

    private String getKakaoId(OAuth2User oAuth2User) {
        log.info("KAKAO ID -------------------------");

        String id = oAuth2User.getAttribute("id");
        log.info("Kakao ID: {}", id);
        return id;
    }

    private String getNaverEmail(Map<String, Object> paramMap) {
        log.info("NAVER -------------------------");

        Object response = paramMap.get("response");
        log.info("Naver Response: {}", response);

        if (response instanceof Map) {
            Map<String, Object> responseMap = (Map<String, Object>) response;
            String email = (String) responseMap.get("email");
            log.info("Naver Email: {}", email);
            return email;
        }
        return null;
    }

    private String getNaverId(OAuth2User oAuth2User) {
        log.info("NAVER ID -------------------------");

        String id = oAuth2User.getAttribute("id");
        log.info("Naver ID: {}", id);
        return id;
    }
}