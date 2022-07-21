package com.speakeasy.controller.v1;

import com.speakeasy.config.security.JwtTokenProvider;
import com.speakeasy.domain.User;
import com.speakeasy.exception.CEmailSigninFailedException;
import com.speakeasy.exception.CUserNotFoundException;
import com.speakeasy.model.KakaoProfile;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.response.CommonResult;
import com.speakeasy.response.ResponseService;
import com.speakeasy.response.SingleResult;
import com.speakeasy.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserRepository userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;

    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    private final KakaoService kakaoService;
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@RequestParam String id, @RequestParam String password) {
        User user = userJpaRepo.findByUid(id).orElseThrow(CEmailSigninFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CEmailSigninFailedException();

        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));

    }

    @PostMapping(value = "/signup")
    public CommonResult signin( @RequestParam String id,
                               @RequestParam String password,
                               @RequestParam String name) {

        userJpaRepo.save(User.builder()
                .uid(id)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return responseService.getSuccessResult();
    }

    @PostMapping(value = "/signin/{provider}")
    public SingleResult<String> signinByProvider(
            @PathVariable String provider,
            @RequestParam String accessToken) {

        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        User user = userJpaRepo.findByUidAndProvider(String.valueOf(profile.getId()), provider).orElseThrow(CUserNotFoundException::new);
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }
}

