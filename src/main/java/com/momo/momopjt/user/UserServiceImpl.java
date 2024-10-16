package com.momo.momopjt.user;


import com.momo.momopjt.club.Club;
import com.momo.momopjt.userandclub.UserAndClubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private static final int PASSWORD_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String SOCIAL_LOGIN_PASSWORD = "1111";
    private final UserAndClubRepository userAndClubRepository;

    public void signup(UserDTO userDTO) throws UserIdException, UserEmailException, UserNicknameException {
      log.info("----------------- [signup()]-----------------YY");

      String userId = userDTO.getUserId();
        String userEmail = userDTO.getUserEmail();
        String userNickname = userDTO.getUserNickname();
        String password = userDTO.getUserPw();
        String confirmPassword = userDTO.getConfirmUserPw();

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 아이디 중복 검사
        if (userRepository.existsByUserId(userId)) {
            throw new UserIdException();
        }

        // 이메일 중복 검사
        if (userRepository.existsByUserEmail(userEmail)) {
            throw new UserEmailException();
        }

        // 닉네임 중복 검사
        if (userRepository.existsByUserNickname(userNickname)) {
            throw new UserNicknameException();
        }

        // User 엔티티 생성 및 설정
        User user = new User();
        user.setUserId(userId);
        user.setUserEmail(userEmail);
        user.setUserNickname(userNickname);
        user.changePassword(passwordEncoder.encode(password));
        user.addRole(UserRole.USER);

        // 비밀번호 암호화 TODO check 필요한건지 모르겠음
//        user.changePassword(passwordEncoder.encode(userDTO.getUserPw()));

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
        user.setUserPhoto("UserDefaultPhoto");


        user.setUserAddress(userDTO.getUserAddress());
        user.setUserCategory(userDTO.getUserCategory());
        user.setUserGender(userDTO.getUserGender());
        user.setUserMBTI(userDTO.getUserMBTI());
        user.setUserBirth(userDTO.getUserBirth());

        userRepository.save(user);
    }


    //나이 계산 로직 메소드
  private int calculateAge(LocalDate userBirth) {
    if (userBirth == null) {
      throw new IllegalArgumentException("생년월일이 null입니다.");
    }
    return Period.between(userBirth, LocalDate.now()).getYears();
  }



    //TODO 리뷰 필요 YY JJ
    @Override
    public void updateUser(UserDTO userDTO) {

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

        if (userDTO.getUserGender() != null) {
        user.setUserGender(userDTO.getUserGender());
        }

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
        log.info("----------------- [findUsernameByEmail()]-----------------");
        Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);

        // Optional에 값이 있는지 확인하고 값 추출
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getUserId();
            // 예를 들어 User 객체에서 getUserID()을 사용하여 사용자 아이디반환
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

            return true;
            // 성공적으로 비밀번호를 재설정한 경우
        } else {

            return false; // 사용자를 찾지 못한 경우
        }
    }

    @Override
    public User findByEmail(String userEmail) {
        log.info("----------------- [findByEmail()]-----------------");
        return userRepository.findByUserEmail(userEmail).orElse(null);
    }

    @Override
    public User findByUserIdAndUserEmail(String userId, String userEmail) {
        log.info("----------------- [findByUserIdAndUserEmail()]-----------------");
        return userRepository.findByUserIdAndUserEmail(userId, userEmail);
    }

    @Override
    public String generateTemporaryPassword() {
        log.info("----------------- [generateTemporaryPassword()]-----------------");
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(index));
        }
        log.info("----------------- [password generated]-----------------{}",stringBuilder.toString());
        return stringBuilder.toString();
    }
    @Override
    public void updateUserPassword(User user, String temporaryPassword) {
        log.info("----------------- [updateUserPassword]-----------------");
        String encodedPW = passwordEncoder.encode(temporaryPassword);
        // 비밀번호 업데이트 로직
        user.setUserPw(encodedPW);
        // 데이터베이스에 저장하는 로직 추가
        log.info("-------- [07-09-11:49:35]-------you-{}",encodedPW);
        userRepository.save(user);
    }

    @Override
    public User findByUserId(String userId) {
        log.info("----------------- [findByUserId]-----------------");
        // userId로 사용자를 찾는 로직 구현
        return userRepository.findByUserId(userId); // UserRepository를 사용하여 실제 사용자 조회
    }

    @Override
    public boolean resetPasswordByUserId(String userId, String newPassword) {
        log.info("----------------- [resetPasswordByUserId]-----------------");
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
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        User user = userRepository.findByUserId(userId);

        if (user != null) {
            if (user.getUserSocial() != null && user.getUserSocial() != ' ') {
                // 소셜 로그인 사용자일 경우 기본 비밀번호와 비교
                if ("1111".equals(userPw)) {
                    // 사용자가 가입된 클럽에서 삭제
                    List<Club> clubs = userAndClubRepository.findMyClubs(user);
                    for (Club club : clubs) {
                        userAndClubRepository.deleteByClubNoAndUserNo(club, user);
                    }

                    // 사용자 삭제
                    userRepository.delete(user);
                    log.info("Social login user account has been deleted.");
                } else {
                    throw new IllegalArgumentException("Invalid password for social login user");
                }
            } else {
                // 일반 사용자일 경우 실제 비밀번호와 비교
                if (passwordEncoder.matches(userPw, user.getUserPw())) {
                    // 사용자가 가입된 클럽에서 삭제
                    List<Club> clubs = userAndClubRepository.findMyClubs(user);
                    for (Club club : clubs) {
                        userAndClubRepository.deleteByClubNoAndUserNo(club, user);
                    }

                    // 사용자 삭제
                    userRepository.delete(user);
                    log.info("User account has been deleted.");
                } else {
                    throw new IllegalArgumentException("Incorrect password");
                }
            }
        } else {
            log.warn("User not found.");
        }
    }
    @Override
    public Optional<User> findByUserNo(Long userNo) {
        return userRepository.findById(userNo); // userNo로 User 찾기
    }

  @Override
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof UserDetails) {
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUserId(username); // 사용자 ID로 User 객체를 조회
      }
    }
    return null;
  }
  //User 모두 조회
  @Override
  public List<UserDTO> readALLUsers() {
    // 모든 유저를 조회하여 리스트에 저장
    List<User> users = userRepository.findAll();
    // users 리스트를 userDTO 리스트로 변환
    return users.stream()
        .map(user -> modelMapper.map(user, UserDTO.class))
        .collect(Collectors.toList());
  }
  //User 검색
  @Override
  public List<UserDTO> searchUsers(String query) {
    List<UserDTO> allUsers = readALLUsers(); // 모든 유저를 조회
    if (query == null || query.isEmpty()) {
      return allUsers;
    }
    return allUsers.stream() // 각 칼럼이 쿼리를 포함하면 리스트로 수집
        .filter(user -> user.getUserNo().toString().contains(query) || // userNo
            user.getUserId().contains(query) || // userId
            user.getUserNickname().contains(query) || // userNickname
            user.getUserEmail().contains(query)) // userEmail
        .collect(Collectors.toList());
  }

  @Override
  public void chageRoleUser(UserDTO userDTO) {
    // 메서드 시작 로그
    log.info("권한 부여 시작: {}", userDTO.getUserNo());

    // userDTO 에서 userNo를 가져옵니다.
    Long userNo = userDTO.getUserNo();

    // userNo를 사용해 사용자 정보를 조회합니다.
    User user = userRepository.findById(userNo)
        .orElseThrow(() -> {
          log.error("찾을 수 없음: {}", userNo);
          return new IllegalArgumentException("없는 유저: " + userNo);
        });

    // 현재 역할을 가져옵니다.
    UserRole currentRole = user.getUserRole();
    log.info("현재 유저 권한 {}: {}", userNo, currentRole);

    // 역할을 전환합니다.
    UserRole newRole = (currentRole == UserRole.ADMIN) ? UserRole.USER : UserRole.ADMIN;
    log.info("새 유저 권한 {}: {}", userNo, newRole);

    // 새로운 역할을 설정합니다.
    user.setUserRole(newRole);
    // 변경된 사용자 정보를 저장합니다.
    userRepository.save(user);
    log.info("권한 설정 완료: {}", userNo);
  }
}
