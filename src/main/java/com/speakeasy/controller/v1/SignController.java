package com.speakeasy.controller.v1;

import com.speakeasy.config.security.JwtTokenProvider;

import com.speakeasy.domain.User;
import com.speakeasy.request.UserSignIn;
import com.speakeasy.request.UserSignUp;
import com.speakeasy.response.CommonResult;
import com.speakeasy.response.TokenResponse;
import com.speakeasy.service.ResponseService;
import com.speakeasy.response.SingleResult;
import com.speakeasy.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    @Autowired
    private final ResponseService responseService;
    private final SignService signService;


    @PostMapping(value = "/signin")
    public SingleResult<TokenResponse> signIn(@RequestBody @Valid UserSignIn request) {
        TokenResponse tokenResponse = signService.signIn(request);
        return responseService.
                getSingleResult(tokenResponse);
    }

    @PostMapping(value = "/signup")
    public CommonResult signup(@RequestBody @Valid UserSignUp request) {
//        request.setPassword(passwordEncoder.encode(request.getPassword()));
        signService.join(request);
        return responseService.getSuccessResult();
    }

    @PostMapping(value = "/signin/{provider}")
    public SingleResult<TokenResponse> signInByProvider(
            @PathVariable String provider,
            @RequestParam String socialToken) {
        TokenResponse tokenResponse = signService.signInByKakao(provider,socialToken);
        return responseService.
                getSingleResult(tokenResponse);
    }

    @PostMapping(value = "/signup/{provider}")
    public CommonResult signupProvider( @PathVariable String provider,
                                       @RequestParam String accessToken,
                                    @RequestParam String name) {
        signService.joinByKakao(provider, accessToken, name);
        return responseService.getSuccessResult();
    }

}
//    @PostMapping(value = "/signin")
//    public ResponseEntity<?> signin(@RequestBody @Valid UserSignIn request, HttpServletResponse response) {
//        User user = signService.comparePassword(request);
//        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles());
////        final Cookie cookie = new Cookie("X-AUTH-TOKEN", accessToken);
////        cookie.setMaxAge(7 * 24 * 60 * 60);
////        cookie.setSecure(true);
////        cookie.setHttpOnly(true);
////        cookie.setPath("/");
//        response.addHeader("X-AUTH-TOKEN",accessToken);
//        return new ResponseEntity<>(accessToken, HttpStatus.OK);
//    }
//    @PostMapping(value = "/signin")
//    public SingleResult<String> signin(@RequestBody @Valid UserSignIn request) {
//        User user = userJpaRepo.findByUid(request.getUid()).orElseThrow(CEmailSigninFailedException::new);
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
//            throw new CEmailSigninFailedException();
//
//        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
//
//    }
//    @PostMapping(value = "/signin-kakao/{provider}")
//    public SingleResult<String> signinkakao(
//            @PathVariable String provider,
//            @RequestParam String accessToken) {
//        User user = userService.kakaoLogin(provider,accessToken);
//        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
//    }
//    @PostMapping(value = "/signin/{provider}")
//    public SingleResult<String> signinByProvider(
//            @PathVariable String provider,
//            @RequestParam String accessToken) {
//
//        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
//        User user = userJpaRepo.findByUidAndProvider(String.valueOf(profile.getId()), provider).orElseThrow(UserNotFoundException::new);
//        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
//    }