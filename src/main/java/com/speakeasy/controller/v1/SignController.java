package com.speakeasy.controller.v1;

import com.speakeasy.config.security.JwtTokenProvider;
import com.speakeasy.domain.KakaoProfile;
import com.speakeasy.domain.User;
import com.speakeasy.exception.UserExistException;
import com.speakeasy.exception.UserNotFoundException;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.request.UserSignIn;
import com.speakeasy.request.UserSignUp;
import com.speakeasy.response.CommonResult;
import com.speakeasy.response.ResponseService;
import com.speakeasy.response.SingleResult;
import com.speakeasy.service.KakaoService;
import com.speakeasy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserRepository userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final ResponseService responseService;

    private final UserService userService;
    private final KakaoService kakaoService;
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@RequestBody @Valid UserSignIn request) {
        User user =userService.comparePassword(request);
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));

    }
//    @PostMapping(value = "/signin")
//    public SingleResult<String> signin(@RequestBody @Valid UserSignIn request) {
//        User user = userJpaRepo.findByUid(request.getUid()).orElseThrow(CEmailSigninFailedException::new);
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
//            throw new CEmailSigninFailedException();
//
//        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
//
//    }

    @PostMapping(value = "/signup")
    public CommonResult signup(@RequestBody @Valid UserSignUp request) {
//        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.join(request);
        return responseService.getSuccessResult();
    }

    @PostMapping(value = "/signin/{provider}")
    public SingleResult<String> signinByProvider(
            @PathVariable String provider,
            @RequestParam String accessToken) {

        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        User user = userJpaRepo.findByUidAndProvider(String.valueOf(profile.getId()), provider).orElseThrow(UserNotFoundException::new);
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }
    @PostMapping(value = "/signup/{provider}")
    public CommonResult signupProvider( @PathVariable String provider,
                                       @RequestParam String accessToken,
                                    @RequestParam String name) {

        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        Optional<User> user = userJpaRepo.findByUidAndProvider(String.valueOf(profile.getId()), provider);
        if(user.isPresent())
            throw new UserExistException();

        userJpaRepo.save(User.builder()
                .uid(String.valueOf(profile.getId()))
                .provider(provider)
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return responseService.getSuccessResult();
    }
}

