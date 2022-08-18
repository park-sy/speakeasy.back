package com.speakeasy.controller.v1;



import com.speakeasy.domain.User;
import com.speakeasy.exception.CookieNotFoundException;
import com.speakeasy.request.TokenRequest;
import com.speakeasy.request.UserSignIn;
import com.speakeasy.request.UserSignUp;
import com.speakeasy.response.CommonResult;
import com.speakeasy.response.TokenResponse;
import com.speakeasy.service.ResponseService;
import com.speakeasy.response.SingleResult;
import com.speakeasy.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import com.speakeasy.exception.UserExistException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class SignController {

    @Autowired
    private final ResponseService responseService;
    private final SignService signService;


    @PostMapping("/signin")
    public SingleResult<TokenResponse> signIn(@RequestBody @Valid UserSignIn request, HttpServletResponse response) {
        System.out.println("로그인을 시도");
        //Access Token, RefreshToken 발행
        TokenResponse tokenResponse = signService.signIn(request);
        
        Cookie accessCookie = new Cookie("AccessToken", tokenResponse.getAccessToken());
        accessCookie.setPath("/");
        //테스트를 위해 잠시 주석 처리
//         accessCookie.setHttpOnly(true);
//         accessCookie.setSecure(true);

        response.addCookie(accessCookie);
        Cookie resfreshCookie = new Cookie("RefreshToken", tokenResponse.getRefreshToken());
        resfreshCookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        response.addCookie(resfreshCookie);

        return responseService.
                getSingleResult(tokenResponse);
        //        response.setHeader("X-AUTH-TOKEN", tokenResponse.getAccessToken());
        //        Cookie cookie = new Cookie("X-AUTH-TOKEN", tokenResponse.getAccessToken());
    }

    @PostMapping("/signout")
    public CommonResult signOut(HttpServletResponse response){
//        Cookie cookie = new Cookie("X-AUTH-TOKEN", null);
        Cookie cookie = new Cookie("AccessToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return responseService.getSuccessResult();
    }
    @PostMapping("/signup")
    public CommonResult signup(@RequestBody @Valid UserSignUp request) {
//        request.setPassword(passwordEncoder.encode(request.getPassword()));
        signService.join(request);
        return responseService.getSuccessResult();
    }

    @GetMapping("/signup/check-email")
    public CommonResult checkEmail(@RequestParam String email) {
//        request.setPassword(passwordEncoder.encode(request.getPassword()));
        System.out.println(email);
        Optional<User> user =signService.getByEmail(email);
        System.out.println(user);
        if(user.isEmpty()==true) return responseService.getSuccessResult();
        else throw new UserExistException("이메일이 중복되었습니다");
    }

    @GetMapping("/reissue")
    public CommonResult reissue(HttpServletRequest request, HttpServletResponse response) {
        String RefreshToken = null;
        Cookie cookie = WebUtils.getCookie(request, "RefreshToken");
        if(cookie == null)  throw new CookieNotFoundException("쿠키가 존재하지 않습니다");

        RefreshToken = cookie.getValue();
        TokenResponse tokenResponse = signService.reissue(TokenRequest.builder().refreshToken(RefreshToken).build());
        Cookie accessCookie = new Cookie("AccessToken", tokenResponse.getAccessToken());
        accessCookie.setPath("/");
        response.addCookie(accessCookie);
//        Cookie refreshCookie = new Cookie("RefreshToken", tokenResponse.getRefreshToken());
//        refreshCookie.setPath("/");
//        response.addCookie(refreshCookie);
        return responseService.getSuccessResult();
    }


    @PostMapping("/signin/{provider}")
    public SingleResult<TokenResponse> signInByProvider(
            @PathVariable String provider,
            @RequestParam String socialToken,
            HttpServletResponse response) {
        TokenResponse tokenResponse = signService.signInByKakao(provider,socialToken);

//        response.setHeader("Set-Cookie", String.format("AccessToken=%s; Secure; SameSite=None",tokenResponse.getAccessToken()));
//        response.addHeader("Set-Cookie", String.format("RefreshToken=%s; Secure; SameSite=None",tokenResponse.getRefreshToken()));
        Cookie accessCookie = new Cookie("AccessToken", tokenResponse.getAccessToken());
        accessCookie.setPath("/");
////        accessCookie.setHttpOnly(true);
////        accessCookie.setSecure(true);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("RefreshToken", tokenResponse.getAccessToken());
        refreshCookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        response.addCookie(refreshCookie);

        return responseService.
                getSingleResult(tokenResponse);
    }

    @PostMapping("/signup/{provider}")
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
//        User user = userJpaRepo.findByEmail(request.getEmail()).orElseThrow(CEmailSigninFailedException::new);
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
//        User user = userJpaRepo.findByEmailAndProvider(String.valueOf(profile.getId()), provider).orElseThrow(UserNotFoundException::new);
//        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
//    }


//    @PostMapping(value = "/reissue")
//    public CommonResult reissue(HttpServletRequest request, HttpServletResponse response) {
//        TokenResponse tokenResponse = signService.reissue(request);
//        Cookie accessCookie = new Cookie("AccessToken", tokenResponse.getAccessToken());
//        accessCookie.setPath("/");
//        response.addCookie(accessCookie);
////        Cookie refreshCookie = new Cookie("RefreshToken", tokenResponse.getRefreshToken());
////        refreshCookie.setPath("/");
////        response.addCookie(refreshCookie);
//        return responseService.getSuccessResult();
//    }