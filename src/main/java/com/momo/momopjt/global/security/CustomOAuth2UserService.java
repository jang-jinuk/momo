package com.momo.momopjt.global.security;

import com.momo.momopjt.global.security.KakaoOAuth2UserInfo;
import com.momo.momopjt.global.security.NaverOAuth2UserInfo;
import com.momo.momopjt.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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



@Log4j2
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


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
                try {
                    email = getNaverEmail(oAuth2User.getAttributes());
                    id = getNaverId(oAuth2User);
                    if (email == null || id == null) {
                        throw new OAuth2AuthenticationException("Email or ID not found from Naver provider");
                    }
                } catch (IllegalArgumentException e) {
                    log.error("Failed to authenticate with Naver", e);
                    throw new OAuth2AuthenticationException("Failed to authenticate with Naver: " + e.getMessage());
                } catch (OAuth2AuthenticationException e) {
                    log.error("OAuth2 authentication exception with Naver", e);
                    throw e;
                } catch (Exception e) {
                    log.error("General exception occurred with Naver", e);
                    throw new OAuth2AuthenticationException("Failed to authenticate with Naver");
                }
        }
        if (email == null || id == null) {
            throw new OAuth2AuthenticationException("Email or ID not found from provider: " + provider);
        }

        Map<String, Object> modifiedAttributes = new HashMap<>(oAuth2User.getAttributes());
        modifiedAttributes.put("id", id); // 'id' 속성을 추가합니다.

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            modifiedAttributes,
            "id" // 사용자 ID 속성의 이름
        );
    }

    private String getNaverEmail(Map<String, Object> paramMap) {
        log.info("Fetching Naver email...");

        Object response = paramMap.get("response");
        if (!(response instanceof Map)) {
            log.warn("'response' is not a map: {}", response);
            throw new IllegalStateException("'response' is not a map: " + response);
        }

        Map<String, Object> responseMap = (Map<String, Object>) response;
        Object emailObj = responseMap.get("email");
        if (!(emailObj instanceof String)) {
            log.warn("Naver Email is not a string: {}", emailObj);
            throw new IllegalStateException("Naver Email is not a string: " + emailObj);
        }

        String email = (String) emailObj;
        log.info("Naver Email: {}", email);
        return email;
    }

    private String getNaverId(OAuth2User oAuth2User) {
        log.info("Fetching Naver ID...");

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 'response' 속성에서 JSON 객체 추출
        Object responseObject = attributes.get("response");
        if (!(responseObject instanceof Map)) {
            log.error("'response' attribute is not a valid JSON object");
            throw new OAuth2AuthenticationException("Invalid 'response' attribute in OAuth2 user attributes");
        }

        // Map으로 캐스팅하여 'id' 값 가져오기
        Map<String, Object> responseMap = (Map<String, Object>) responseObject;
        Object idObj = responseMap.get("id");
        if (idObj == null) {
            log.error("'id' attribute not found in 'response' attribute");
            throw new OAuth2AuthenticationException("Naver ID not found in OAuth2 user attributes");
        }

        String id = idObj.toString(); // Object를 String으로 변환
        log.info("Naver ID: {}", id);
        return id;
    }


    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("Fetching Kakao email...");

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
        log.info("Fetching Kakao ID...");

        Object idObject = oAuth2User.getAttribute("id");
        if (idObject instanceof String) {
            return (String) idObject;
        } else if (idObject instanceof Long) {
            return Long.toString((Long) idObject); // Long 타입인 경우 문자열로 변환
        } else {
            throw new IllegalStateException("Unexpected type for 'id' attribute: " + idObject.getClass().getName());
        }
    }
    }