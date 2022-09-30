package com.speakeasy.service;

import com.speakeasy.exception.UserNotFoundException;
import com.speakeasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userPk) {
        return userRepository.findById(Long.valueOf(userPk)).orElseThrow(UserNotFoundException::new);
    }
}