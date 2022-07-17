package com.speakeasy.controller.v1;

import com.speakeasy.domain.User;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.request.UserCreate;
import com.speakeasy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController // 결과값을 JSON으로 출력합니다.
@RequestMapping(value = "/v1")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    @GetMapping(value = "/user")
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @PostMapping(value = "/user")
    public void create(@RequestBody @Valid UserCreate request) {
        //세번 이상의 반복 작업은 피한다. 매번 메소드마다 값을 검증해야함. 이는 실수를 발생시킴, 응답 값에 맞는 클래스를 만들어 주는 게 좋음
        //CASE 1. ENTITY 반환 -> return postService.write(request)
        //CASE 2. 저장한 데이터의 primary_id -> return Map.of("post Id", postId)
        //CASE 3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함
        userService.write(request);
    }

//    public User save() {
//        User user = User.builder()
//                .uid("yumi@naver.com")
//                .name("유미")
//                .build();
//        return userRepository.save(user);
//    }
}