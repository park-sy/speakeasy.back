package com.speakeasy.service;

import com.speakeasy.exception.CUserNotFoundException;
import com.speakeasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository UserRepository;

    public UserDetails loadUserByUsername(String userPk) {
        return UserRepository.findById(Long.valueOf(userPk)).orElseThrow(CUserNotFoundException::new);
    }
}

