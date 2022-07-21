package com.speakeasy.controller.v1;


import com.speakeasy.domain.User;
import com.speakeasy.exception.CUserNotFoundException;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.response.CommonResult;
import com.speakeasy.response.ListResult;
import com.speakeasy.response.ResponseService;
import com.speakeasy.response.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

    private final UserRepository userRepository;
    private final ResponseService responseService; // 결과를 처리할 Service


    @GetMapping(value = "/users")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userRepository.findAll());
    }

    @GetMapping(value = "/user")
    public SingleResult<User> findUser() {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(userRepository.findByUid(id).orElseThrow(CUserNotFoundException::new));
    }

    @PutMapping(value = "/user")
    public SingleResult<User> modify(
            @RequestParam String name) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        User user = userRepository.findByUid(id).orElseThrow(CUserNotFoundException::new);
        user.setName(name);
        return responseService.getSingleResult(userRepository.save(user));
    }

    @DeleteMapping(value = "/user/{msrl}")
    public CommonResult delete(@PathVariable long msrl) {
        userRepository.deleteById(msrl);
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }
}