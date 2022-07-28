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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SignService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;



    public UserDetails loadUserByUsername(String userPk) {
        return userRepository.findById(Long.valueOf(userPk)).orElseThrow(UserNotFoundException::new);
    }
    //쉽게 말하면 UserDetails 인터페이스는 VO 역할

    public void join(UserSignUp request){
        User user= User.builder()
                .uid(request.getUid())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userRepository.save(user);
    }

    public User comparePassword(UserSignIn request){
        User user = userRepository.findByUid(request.getUid()).orElseThrow(EmailSigninFailedException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new EmailSigninFailedException();
        }
        return user;
    }
    public User signInByKakao(String provider, String kakaoToken){
        KakaoProfile profile = kakaoService.getKakaoProfile(kakaoToken);
        User user = userRepository.findByUidAndProvider(String.valueOf(profile.getId()), provider).orElseThrow(UserNotFoundException::new);
        return user;
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
}

