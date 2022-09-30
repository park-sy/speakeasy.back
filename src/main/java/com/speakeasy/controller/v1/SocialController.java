package com.speakeasy.controller.v1;


// import 생략


import com.google.gson.Gson;
import com.speakeasy.domain.User;
import com.speakeasy.response.KakaoAuth;
import com.speakeasy.response.KakaoProfile;
import com.speakeasy.service.KakaoService;
import com.speakeasy.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/social/login")
public class SocialController {

    private final Environment env;
    private final RestTemplate restTemplate;
    private final Gson gson;
    private final KakaoService kakaoService;
    private final SignService signService;

    @Value("${spring.url.base}")
    private String baseUrl;

    @Value("${spring.social.kakao.client_id}")
    private String kakaoClientId;

    @Value("${spring.social.kakao.redirect}")
    private String kakaoRedirect;



    /**
     * 카카오 로그인 페이지, rest api 형식이므로 버튼만 나오도록
     */
    @GetMapping
    public ModelAndView socialLogin(ModelAndView mav) {

//        카카오 로그인을 리퀘스트 생성
        StringBuilder loginUrl = new StringBuilder()
                .append(env.getProperty("spring.social.kakao.url.login"))
                .append("?client_id=").append(kakaoClientId)
                .append("&response_type=code")
                .append("&redirect_uri=").append(baseUrl).append(kakaoRedirect);
        mav.addObject("loginUrl", loginUrl);
//        mav.setViewName("social/login");
        return mav;
    }

    /**
     * 카카오 인증 완료 후 리다이렉트 화면
     */
    @GetMapping(value = "/kakao")
    public @ResponseBody void redirectKakao(@RequestParam String code, HttpServletResponse response) throws IOException {
//        mav.addObject("authInfo", kakaoService.getKakaoTokenInfo(code));
//        mav.setViewName("social/redirectKakao");
        KakaoAuth kakaoAuth = kakaoService.getKakaoTokenInfo(code);
        KakaoProfile profile =kakaoService.getKakaoProfile(kakaoAuth.getAccess_token());
        System.out.println(profile);
        Optional<User> user= signService.getByEmailAndProvider("kakao",profile);
        System.out.println(user);
        // 이후 만약 empty일 경우, 회원가입으로 이동 아닐 경우 로그인 진행
        if(user.isEmpty()){
            response.sendRedirect("http://localhost:3000/join");
//            return kakaoAuth.getAccess_token()+" 가입화면 redirect";
        }
        else{
           System.out.println("로그인 처리");//로그인 처리
        }
    }

}