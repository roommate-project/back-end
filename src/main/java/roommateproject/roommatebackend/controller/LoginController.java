package roommateproject.roommatebackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.KakaoOauthService;
import roommateproject.roommatebackend.service.LoginService;
import roommateproject.roommatebackend.token.JwtTokenProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

@RestController @Slf4j
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenProvider tokenProvider;
    private final KakaoOauthService kakaoOauthService;
    @PostMapping("/api/login")
    public ResponseMessage login(@RequestBody Map<String, String> request){

        String requestEmail = request.get("email");
        String requestPassword = request.get("password");
        boolean check;
        try{
            check = loginService.login(requestEmail,requestPassword);
        }catch (NoSuchAlgorithmException e){
            return new ResponseMessage(e);
        }catch (IllegalArgumentException e){
            return new ResponseMessage(e);
        }
        if(check){
            return new ResponseMessage(tokenProvider.createToken(requestEmail));
        }
        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "로그인 불가", new Date());
    }

    @GetMapping("/api/login/oauth/kakao")
    public ResponseMessage kakaoAddUser(@RequestParam String code){
        //https://kauth.kakao.com/oauth/authorize?client_id=26fc82315434c1ec0e23b5a0aa5076e5&redirect_uri=http://localhost:8080/api/login/oauth/kakao&response_type=code
        log.info("kakao return code : {}",code);
        String kakaoEmail = kakaoOauthService.findKakaoEmail(kakaoOauthService.getKakaoAccessToken(code,"http://localhost:8080/api/login/oauth/kakao"));

        if(kakaoEmail == null){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "제대로 정보 동의하세요",new Date());
        }
        loginService.kakaoLogin(kakaoEmail);
        log.info("token");
        return new ResponseMessage(tokenProvider.createToken(kakaoEmail));
    }
}
