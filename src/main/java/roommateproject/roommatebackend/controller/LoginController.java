package roommateproject.roommatebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.KakaoOauthService;
import roommateproject.roommatebackend.service.LoginService;
import roommateproject.roommatebackend.service.NaverOauthService;
import roommateproject.roommatebackend.token.JwtTokenProvider;

import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

@RestController @Slf4j
@CrossOrigin
public class LoginController {

    @Value("${spring.ip}")
    private String ip;

    private final LoginService loginService;
    private final JwtTokenProvider tokenProvider;
    private final KakaoOauthService kakaoOauthService;
    private final NaverOauthService naverOauthService;

    public LoginController(LoginService loginService, JwtTokenProvider tokenProvider, KakaoOauthService kakaoOauthService, NaverOauthService naverOauthService) {
        this.loginService = loginService;
        this.tokenProvider = tokenProvider;
        this.kakaoOauthService = kakaoOauthService;
        this.naverOauthService = naverOauthService;
    }

    @PostMapping("/api/login")
    public ResponseMessage login(HttpServletResponse res,
                                 @RequestBody Map<String, String> request){

        String requestEmail = request.get("email");
        String requestPassword = request.get("password");
        Long id;
        try{
            id = loginService.login(requestEmail,requestPassword);
        }catch (NoSuchAlgorithmException e){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(e);
        }catch (IllegalArgumentException e){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(e);
        }
        if(id != null){
            return new ResponseMessage(tokenProvider.createToken(id, requestEmail));
        }
        res.setStatus(HttpStatus.ACCEPTED.value());
        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "로그인 불가", new Date());
    }

    @GetMapping("/api/login/oauth/kakao")
    public ResponseMessage kakaoLogin(HttpServletResponse res,
                                      @RequestParam String code){

        String kakaoEmail = kakaoOauthService.findKakaoEmail(kakaoOauthService.getKakaoAccessToken(code,"http://" + ip + "/api/login/oauth/kakao"));
        Long id;
        if(kakaoEmail == null){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "제대로 정보 동의하세요",new Date());
        }
        try {
            id = loginService.kakaoLogin(kakaoEmail);
        }catch (IllegalArgumentException e){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(e);
        }
        return new ResponseMessage(tokenProvider.createToken(id, kakaoEmail));
    }

    @GetMapping("/api/login/oauth/naver")
    public ResponseMessage naverLogin(HttpServletResponse res,
                                      @RequestParam String code){

        String naverEmail = naverOauthService.findNaverEmail(naverOauthService.getNaverAccessToken(code,"http://" + ip + "/api/login/oauth/kakao"));
        Long id;
        if(naverEmail == null){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "제대로 정보 동의하세요", new Date());
        }
        try {
            id = loginService.naverLogin(naverEmail);
        }catch(IllegalArgumentException e){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(e);
        }
        return new ResponseMessage(tokenProvider.createToken(id, naverEmail));
    }
}
