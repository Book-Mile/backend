package com.bookmile.backend.domain.user.service.impl;

import com.bookmile.backend.domain.image.service.Impl.ImageService;
import com.bookmile.backend.domain.user.dto.req.PasswordReqDto;
import com.bookmile.backend.domain.user.dto.req.SignInReqDto;
import com.bookmile.backend.domain.user.dto.req.SignUpReqDto;
import com.bookmile.backend.domain.user.dto.req.UserInfoReqDto;
import com.bookmile.backend.domain.user.dto.res.UserInfoDto;
import com.bookmile.backend.domain.user.dto.res.TokenResDto;
import com.bookmile.backend.domain.user.dto.res.UserDetailResDto;
import com.bookmile.backend.domain.user.dto.res.UserResDto;
import com.bookmile.backend.domain.user.entity.User;
import com.bookmile.backend.domain.user.repository.UserRepository;
import com.bookmile.backend.domain.user.service.UserService;
import com.bookmile.backend.global.exception.CustomException;
import com.bookmile.backend.global.jwt.JwtTokenProvider;
import com.bookmile.backend.global.oauth.nickname.RandomNickname;
import com.bookmile.backend.global.redis.RefreshToken;
import com.bookmile.backend.global.redis.RefreshTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.bookmile.backend.global.common.StatusCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;
    private final RandomNickname randomNickname;
    private final ImageService imageService;

    @Value("${spring.mail.username}")
    private String maileSenderEmail;

    @Value("${aws.bucket.name.profile}")
    private String bucketName;

    private static final String[] ALLOWED_EXTENSIONS = {"png", "jpg", "jpeg"};

    @Override
    @Transactional
    public UserResDto signUp(SignUpReqDto signUpReqDto) {
        existsByEmail(signUpReqDto.getEmail());

        // 비밀번호 일치 여부 확인
        if (!(signUpReqDto.getPassword().equals(signUpReqDto.getCheckPassword()))) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        String enCodePassword = passwordEncoder.encode(signUpReqDto.getPassword());

        // 닉네임 자동 생성
        String nickname = randomNickname.generateNickname();

        User user = userRepository.save(signUpReqDto.toEntity(signUpReqDto.getEmail(), nickname, enCodePassword));
        return UserResDto.toDto(user);
    }

    @Override
    @Transactional
    public TokenResDto signIn(SignInReqDto signInReqDto) {
        User user = findByEmail(signInReqDto.getEmail());

        if (!passwordEncoder.matches(signInReqDto.getPassword(), user.getPassword())) {
            throw new CustomException(AUTHENTICATION_FAILED); // 유저는 아이디, 비밀번호 중 한개만 틀려도 '일치하는 정보가 없음' 메세지 표시
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getId(), user.getRole().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getId());

        return TokenResDto.toDto(accessToken, refreshToken);
    }

    // 토큰 재발급 (refreshToken으로)
    @Override
    public TokenResDto reIssue(HttpServletRequest request) {

        Map<String, Object> userInfo = getUserIdByToken(request);
        String token = (String) userInfo.get("token");
        Long userId = (Long) userInfo.get("userId");

        log.info("userId: {}",  userId);

        RefreshToken refreshToken = refreshTokenRepository.findById("refreshToken" + userId).orElseThrow(() -> new CustomException(TOKEN_NOT_FOUND));
        log.info("Redis RefreshToken : {}", refreshToken.getRefreshToken());

        if(!refreshToken.getRefreshToken().equals(token)) {
            throw new CustomException(INVALID_TOKEN);
        }

        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getId(), user.getRole().toString());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getId());

        return TokenResDto.toDto(newAccessToken, newRefreshToken);
    }

    // 회원 정보 조회
    @Override
    public UserInfoDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return UserInfoDto.toDto(user);
    }

    // 회원 정보 조회 (토큰)
    @Override
    public UserDetailResDto getUser(String email) {
        User user = findByEmail(email);
        return UserDetailResDto.toDto(user);
    }

    // 닉네임 중복 확인
    @Override
    @Transactional
    public Boolean checkNickname(String nickname) {
        Boolean isUseNickname = userRepository.existsByNickname(nickname);
        return isUseNickname;
    }

    // 회원 정보 수정 ( 닉네임, 이메일(확인용))
    // 닉네임
    @Override
    @Transactional
    public TokenResDto updateUser(String email, UserInfoReqDto userInfoReqDto) {
        User user = findByEmail(email);
        user.updateUser(userInfoReqDto.getNickname(), userInfoReqDto.getEmail());

        // 토큰 재생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getId(), user.getRole().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getId());


        return TokenResDto.toDto(accessToken, refreshToken);
    }

    // 이메일 전송
    @Override
    @Async
    public void sendEmailCode(String email) {

        // 변경하려는 이메일이 이미 존재하는지 확인
        existsByEmail(email);

        // 일최대 5개 제한
        long count = getEmailRequestCount(email);
        if(count == 5){
            throw new CustomException(EMAIL_TOO_MANY_REQUESTS);
        }

        Random random = new Random();
        int code = random.nextInt(888888) + 111111;  // 111111 ~ 999999 (6자리 난수)
        String subject = "[Bookmile] 이메일 인증 번호 ";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(maileSenderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject(subject, "UTF-8");
            message.setText(mailText(String.valueOf(code)), "UTF-8","html");
            mailSender.send(message);

            // redis 저장
            saveVerificationCode(email, String.valueOf(code));
            // 요청 카운트 증가
            increaseEmailRequestCount(email);

        }catch (MessagingException e){
            throw new CustomException(MAIL_SERVER_ERROR);
        }
    }

    // 이메일 인증
    @Override
    @Transactional
    public void verificationCode( String email, String requestCode) {

        String verificationCode = getVerificationCode(email);
        if(!requestCode.equals(verificationCode)) {
            throw new CustomException(EMAIL_CODE_NOT_MATCH);
        }

    }

    // 비밀번호 변경
    @Override
    @Transactional
    public void changePassword(String email, PasswordReqDto passwordReqDto) {

        User user = findByEmail(email);

        // 기존 비밀번호 확인
        if (!passwordEncoder.matches(passwordReqDto.getOriginPassword(), user.getPassword())) {
            throw new CustomException(AUTHENTICATION_FAILED);
        }

        // 기존, 새로운 비밀번호 동일 여부 확인
        if(passwordEncoder.matches(passwordReqDto.getNewPassword(), user.getPassword())){
            throw new CustomException(PASSWORD_DUPLICATE);
        }

        // 새로운 비밀번호 확인
        // 비밀번호 일치 여부 확인
        if (!(passwordReqDto.getNewPassword().equals(passwordReqDto.getCheckPassword()))) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        String enCodePassword = passwordEncoder.encode(passwordReqDto.getNewPassword());
        user.updatePassword(enCodePassword);

    }

    // 프로필 이미지 수정
    @Override
    @Transactional
    public void updateProfile(String email, MultipartFile file) {
        User user = findByEmail(email);

        if(!validateImageFile(file)){
            throw new CustomException(INVALID_FILE_TYPE);
        }

        if(user.getImage() != null) {
            imageService.deleteFileFromS3Bucket(bucketName, user.getImage());
        }

        String url = imageService.uploadFileToS3Bucket(bucketName, file);
        user.updateImage(url);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        User user = findByEmail(email);
        user.updateIsDeleted();
    }

    private boolean validateImageFile(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();

        for(String allowedExtension : ALLOWED_EXTENSIONS) {
            if(extension.equals(allowedExtension)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Object> getUserIdByToken(HttpServletRequest request) {
        Map<String, Object > map = new HashMap<>();

        String token = jwtTokenProvider.resolveToken(request);
        if(token == null) {
            throw new CustomException(INVALID_TOKEN);
        }
        Long userId = jwtTokenProvider.getUserId(token);

        map.put("token", token);
        map.put("userId", userId);

        return map;
    }

    // 이메일 중복 확인
    private void existsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(USER_ALREADY_EXISTS);
        };
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(AUTHENTICATION_FAILED));
    }

    // 이메일 요청 카운트 증가
    private void increaseEmailRequestCount(String email) {
        String key = "email_request_count:" + email;
        long count = redisTemplate.opsForValue().increment(key);

        if (count == 5) {
            redisTemplate.expire(key, 24, TimeUnit.HOURS);
        }
    }

    // 이메일 인증요청 카운트
    private long getEmailRequestCount(String email) {
        String key = "email_request_count:" + email;
        String value = redisTemplate.opsForValue().get(key);
        return value == null ? 0 : Long.parseLong(value);
    }

    // redis에 인증코드 저장
    private void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES); //3분 타임아웃
    }

    //redis에서 인증코드 가져오기
    private String getVerificationCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    // 요청 본문
    private String mailText(String code) {
        return """
        <html>
            <body>
                <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <h3>안녕하세요, Bookmile 입니다.</h3>
                    <p>이메일 인증을 위해 요청하신 인증 코드입니다.</p>
                    <div style="border: 1px solid #ddd; padding: 10px; text-align: center;">
                        <h2>이메일 인증 코드</h2>
                        <h1 style="color: #1d72b8;">%s</h1>
                    </div>
                    <p>상단의 인증 코드를 입력하여 이메일 인증을 완료해주세요.</p>
                    <p>감사합니다.</p>
                </div>
            </body>
        </html>
        """.formatted(code);
    }
}


