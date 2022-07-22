package com.speakeasy.service;

import com.speakeasy.domain.User;
import com.speakeasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService {
    private final UserRepository userRepository;
    //lombok을 통해 requireArg로 생성자로 만듦


    public void join(User user){
        // postCreate(request)-> Entity
        userRepository.save(user);
    }


}