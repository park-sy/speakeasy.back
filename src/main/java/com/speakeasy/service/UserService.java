package com.speakeasy.service;

import com.speakeasy.domain.User;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.request.UserCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    //lombok을 통해 requireArg로 생성자로 만듦

    public void write(UserCreate userCreate){
        // postCreate(request)-> Entity
        User post =User.builder().uid(userCreate.getUid()).name(userCreate.getName()).build();
        userRepository.save(post);
    }
}
