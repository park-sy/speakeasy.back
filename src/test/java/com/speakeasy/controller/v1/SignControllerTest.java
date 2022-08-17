package com.speakeasy.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakeasy.domain.user.User;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.request.UserSignIn;
import com.speakeasy.request.UserSignUp;
import com.speakeasy.service.SignService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class SignControllerTest {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignService signService;

    @BeforeEach
    void clean(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인을 실행한다")
    void signIn() throws Exception{
        UserSignUp user = UserSignUp
                .builder()
                .uid("id@naver.com")
                .password("1234")
                .name("이름")
                .build();
        signService.join(user);
        Assertions.assertEquals(1, userRepository.count());
        UserSignIn request = UserSignIn
                .builder()
                .uid("id@naver.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(request);
        mockMvc .perform(post("/v1/signin")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void signOut() throws Exception{
    }

    @Test
    void signup() throws Exception{
        UserSignUp request = UserSignUp
                .builder()
                .uid("id@naver.com")
                .password("1234")
                .name("이름")
                .build();
        String json = objectMapper.writeValueAsString(request);
        mockMvc .perform(post("/v1/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
        Assertions.assertEquals(1, userRepository.count());
        User user = userRepository.findAll().get(0);

        assertEquals("id@naver.com",user.getUid());
        assertEquals("이름",user.getName());

    }

    @Test
    void reissue() throws Exception{
    }
}