package com.speakeasy.service;

import com.speakeasy.config.security.JwtTokenProvider;
import com.speakeasy.domain.User;
import com.speakeasy.exception.EmailSigninFailedException;
import com.speakeasy.exception.UserNotFoundException;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.request.UserSignIn;
import com.speakeasy.request.UserSignUp;
import com.speakeasy.response.KakaoAuth;
import com.speakeasy.response.KakaoProfile;
import com.speakeasy.response.TokenResponse;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class SignService  {
//public class SignService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;

    private static final String BEARER_TYPE = "Bearer";
    private final RedisTemplate redisTemplate;

    private final JwtTokenProvider jwtTokenProvider;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;              // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일

//    public UserDetails loadUserByUsername(String userPk) {
//        return userRepository.findById(Long.valueOf(userPk)).orElseThrow(UserNotFoundException::new);
//    }
    public void join(UserSignUp request){
        User user= User.builder()
                .uid(request.getUid())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userRepository.save(user);
    }

    public TokenResponse signIn(UserSignIn request){
        User user = userRepository.findByUid(request.getUid()).orElseThrow(EmailSigninFailedException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new EmailSigninFailedException();
        }
        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles(), ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles(), REFRESH_TOKEN_EXPIRE_TIME);
        redisTemplate.opsForValue()
                .set("RT:" + user.getUid(), refreshToken , REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return TokenResponse
                .builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

//    public User comparePassword(UserSignIn request){
//        User user = userRepository.findByUid(request.getUid()).orElseThrow(EmailSigninFailedException::new);
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new EmailSigninFailedException();
//        }
//        return user;
//    }
    public TokenResponse signInByKakao(String provider, String kakaoToken){
        KakaoProfile profile = kakaoService.getKakaoProfile(kakaoToken);
        User user = userRepository.findByUidAndProvider(String.valueOf(profile.getId()), provider).orElseThrow(UserNotFoundException::new);
        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles(), ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles(), REFRESH_TOKEN_EXPIRE_TIME);
        return TokenResponse
                .builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    public void joinByKakao(String provider, String accessToken, String name){
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        Optional<User> user = userRepository.findByUidAndProvider(String.valueOf(profile.getId()), provider);
        userRepository.save(User.builder()
                .uid(String.valueOf(profile.getId()))
                .provider(provider)
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
    }
    public Optional<User> getByUidAndProvider(String provider, KakaoProfile profile){
        Optional<User> user = userRepository.findByUidAndProvider(String.valueOf(profile.getId()), provider);
        if(user.isPresent()){
            System.out.println("로그인 과정");
        }
        else{
            System.out.println("회원 가입 과정");
        }
        return user;
    }
    @Transactional
    public TokenResponse reissue(HttpServletRequest request) { //추후 리퀘스트로 수정
        String token = null;
        Cookie cookie = WebUtils.getCookie(request, "RefreshToken");
        if(cookie != null) token = cookie.getValue();

        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        if(!refreshToken.equals(token)) {
            return null;
        }
        System.out.println("refreshToken");
        System.out.println( refreshToken);
        User principal = (User) authentication.getPrincipal();
//        System.out.println( principal.getRoles());

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.equals(token)) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

//         5. 새로운 Access 토큰 생성
        String newAccessToken = jwtTokenProvider.createToken(jwtTokenProvider.getUserPk(refreshToken),principal.getRoles(),REFRESH_TOKEN_EXPIRE_TIME);

//         토큰 발급
        return TokenResponse.builder().accessToken(newAccessToken).refreshToken(token).build();
    }

    //리프레쉬 토큰을 재생성하는 로직. 구현 중
//    @Transactional
//    public TokenResponse reissue(TokenResponse tokenRequest) { //추후 리퀘스트로 수정
//
//        // 1. Refresh Token 검증
//        if (!jwtTokenProvider.validateToken(tokenRequest.getRefreshToken())) {
//            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
//        }
//
//        // 2. Access Token 에서 Member ID 가져오기
//        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequest.getAccessToken());
//
//        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
//        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
//        if(!refreshToken.equals(tokenRequest.getRefreshToken())) {
//            return null;
//        }
//        System.out.println("refreshToken");
//        System.out.println( refreshToken);
//        User principal = (User) authentication.getPrincipal();
////        System.out.println( principal.getRoles());
//
//        // 4. Refresh Token 일치하는지 검사
//        if (!refreshToken.equals(tokenRequest.getRefreshToken())) {
//            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
//        }
//
//        // 5. 새로운 토큰 생성
//        String newRefreshToken = jwtTokenProvider.createToken(jwtTokenProvider.getUserPk(refreshToken),principal.getRoles(),REFRESH_TOKEN_EXPIRE_TIME);
//
//        // 6. 저장소 정보 업데이트
//        redisTemplate.opsForValue()
//                .set("RT:" + authentication.getName(), newRefreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
//
//        // 토큰 발급
//        return TokenResponse.builder().accessToken(tokenRequest.getAccessToken()).refreshToken(newRefreshToken).build();
//    }
}

