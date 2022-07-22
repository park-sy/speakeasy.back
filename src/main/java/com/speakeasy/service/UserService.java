package com.speakeasy.service;

import com.speakeasy.domain.User;
import com.speakeasy.exception.EmailSigninFailedException;
import com.speakeasy.exception.UserNotFoundException;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.request.UserSignIn;
import com.speakeasy.request.UserSignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
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
}

