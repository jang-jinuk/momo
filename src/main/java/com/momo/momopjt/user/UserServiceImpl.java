package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;


@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;


    @Override
    @Transactional
    public void signup(UserDTO userDTO) throws UserIdException, UserEmailException {

        //UserId 중복 검사
        String userId = userDTO.getUserId();
        String userEmail = userDTO.getUserEmail();
        boolean existId = userRepository.existsByUserId(userId);
        boolean existEmail = userRepository.existsByUserEmail(userEmail); // existsByUserId 사용
        if (existId) {
            throw new UserIdException();
        }
        if(existEmail){
            throw new UserEmailException();
        }

//      Email 중복 검사
        User user = modelMapper.map(userDTO, User.class);

        // 비밀번호 암호화
        user.changePassword(passwordEncoder.encode(userDTO.getUserPw()));

        // 역할 설정
        user.addRole(UserRole.USER);

        // 나이 계산
        int userAge = calculateAge(userDTO.getUserBirth());
        user.setUserAge(userAge);

        // 현재 날짜 설정
        Instant now = Instant.now();
        user.setUserCreateDate(now);
        user.setUserModifyDate(now);

        // 기본 값 설정
        user.setUserState('0');
        user.setUserLikeNumber(0);

        log.info(user);
        log.info(user.getRoleSet());

        userRepository.save(user);
    }

    //가입된 생년월일로 데이터베이스에 age항목에 넣는다.
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        //TODO review 필요 0716 YY

        // 1. 사용자 찾기
        User user = userRepository.findByUserId(userDTO.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("User not found with userId: " + userDTO.getUserId());
        }

        // 2. 비밀번호 업데이트
        if (userDTO.getUserPw() != null && !userDTO.getUserPw().isEmpty()) {
            user.setUserPw(passwordEncoder.encode(userDTO.getUserPw()));
        }
        // 3. 이메일 업데이트
        if (userDTO.getUserEmail() != null && !userDTO.getUserEmail().isEmpty()) {
            user.setUserEmail(userDTO.getUserEmail());
        }

        // 4. 닉네임 업데이트
        if (userDTO.getUserNickname() != null && !userDTO.getUserNickname().isEmpty()) {
            user.setUserNickname(userDTO.getUserNickname());
        }

        // 5. 카테고리 업데이트
        if (userDTO.getUserCategory() != null && !userDTO.getUserCategory().isEmpty()) {
            user.setUserCategory(userDTO.getUserCategory());
        }

        // 6. 주소 업데이트
        if (userDTO.getUserAddress() != null && !userDTO.getUserAddress().isEmpty()) {
            user.setUserAddress(userDTO.getUserAddress());
        }

        // 7. MBTI 업데이트
        if (userDTO.getUserMBTI() != null && !userDTO.getUserMBTI().isEmpty()) {
            user.setUserMBTI(userDTO.getUserMBTI());
        }

        // 8. 소셜 타입 업데이트
        //if (userDTO.getUserSocial() != null) {
        //   user.setUserSocial(userDTO.getUserSocial());
        //}

        // 9. 수정일 업데이트
        user.setUserModifyDate(Instant.now());

        // 10. 변경사항 저장
        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            // 데이터베이스 저장 중 문제가 발생한 경우 처리
            throw new RuntimeException("Failed to update user with userId: " + user.getUserId(), e);
        }
    }

    @Override
    public String findUsernameByEmail(String userEmail) {
        Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);

        // Optional에 값이 있는지 확인하고 값 추출
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getUserId(); // 예를 들어 User 객체에서 getUserID()을 사용하여 사용자 아이디반환
        } else {
            return null; // 값이 없을 경우 null 반환
        }
    }

    @Override
    public boolean resetPassword(String userId, String userEmail, String newPassword) {
        Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 사용자 아이디 비교
            if (!userId.equals(user.getUserId())) {
                return false; // 사용자 아이디가 일치하지 않는 경우
            }

            // 새 비밀번호가 null이거나 비어 있는지 확인
            if (newPassword == null || newPassword.isEmpty()) {
                return false; // 새 비밀번호가 비어 있는 경우
            }

            // 새 비밀번호가 기존 비밀번호와 동일한지 확인
            if (passwordEncoder.matches(newPassword, user.getUserPw())) {
                return false; // 새 비밀번호가 기존 비밀번호와 동일한 경우
            }

            // 새 비밀번호가 일정한 규칙을 충족하는지 검증 (예: 최소 길이)
            if (newPassword.length() < 8) {
                return false; // 예시로 최소 길이가 8자 이상이어야 한다고 가정
            }

            // 새 비밀번호 암호화
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setUserPw(encryptedPassword);

            // UserRepository를 통해 사용자 정보 업데이트
            userRepository.save(user);

            return true; // 성공적으로 비밀번호를 재설정한 경우
        } else {
            return false; // 사용자를 찾지 못한 경우
        }
    }

    @Override
    public User findByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail).orElse(null);
    }

    @Override
    public User findByUserIdAndUserEmail(String userId, String userEmail) {
        return userRepository.findByUserIdAndUserEmail(userId, userEmail);
    }

    @Override
    public String generateTemporaryPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
    @Override
    public void updateUserPassword(User user, String temporaryPassword) {
        String encodedPW = passwordEncoder.encode(temporaryPassword);
        // 비밀번호 업데이트 로직
        user.setUserPw(encodedPW);
        // 데이터베이스에 저장하는 로직 추가
        log.info("-------- [07-09-11:49:35]-------you"+encodedPW);
        userRepository.save(user);
    }

    @Override
    public User findByUserId(String userId) {
        // userId로 사용자를 찾는 로직 구현
        return userRepository.findByUserId(userId); // UserRepository를 사용하여 실제 사용자 조회
    }

    @Override
    public boolean resetPasswordByUserId(String userId, String newPassword) {
        // userId로 사용자를 찾고, 비밀번호를 reset하는 로직을 구현
        User user = findByUserId(userId);
        if (user != null) {
            // 새 비밀번호가 null이거나 비어 있는지 확인
            if (newPassword == null || newPassword.isEmpty()) {
                return false; // 새 비밀번호가 비어 있는 경우
            }

            // 새 비밀번호가 기존 비밀번호와 동일한지 확인
            if (passwordEncoder.matches(newPassword, user.getUserPw())) {
                return false; // 새 비밀번호가 기존 비밀번호와 동일한 경우
            }

            // 새 비밀번호가 일정한 규칙을 충족하는지 검증 (예: 최소 길이)
            if (newPassword.length() < 8) {
                return false; // 예시로 최소 길이가 8자 이상이어야 한다고 가정
            }

            // 새 비밀번호 암호화
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setUserPw(encryptedPassword);

            // UserRepository를 통해 사용자 정보 업데이트
            userRepository.save(user);

            return true; // 성공적으로 비밀번호를 재설정한 경우
        }
        return false;
    }


    @Override
    public void deleteAccount(String userId, String userPw) {
        if (userId == null || userId.isEmpty() || userPw == null || userPw.isEmpty()) {
            throw new IllegalArgumentException("User ID and password cannot be null or empty");
        }

        User user = userRepository.findByUserId(userId);
        if (user != null) {
            if (passwordEncoder.matches(userPw, user.getUserPw())) { // 비밀번호 검증
                userRepository.delete(user);
                log.info("User account has been deleted.");
            } else {
                throw new IllegalArgumentException("Incorrect password");
            }
        } else {
            log.warn("User not found.");
        }
    }
}