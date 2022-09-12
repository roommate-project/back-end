package roommateproject.roommatebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserToken;
import roommateproject.roommatebackend.repository.UserTokenRepository;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.KakaoOauthService;
import roommateproject.roommatebackend.service.LoginService;
import roommateproject.roommatebackend.service.NaverOauthService;
import roommateproject.roommatebackend.token.JwtTokenProvider;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController @Slf4j
@CrossOrigin
public class LoginController {

    @Value("${spring.ip}")
    private String ip;

    private final LoginService loginService;
    private final JwtTokenProvider tokenProvider;
    private final KakaoOauthService kakaoOauthService;
    private final NaverOauthService naverOauthService;
    private final UserTokenRepository userTokenRepository;

    public LoginController(LoginService loginService, JwtTokenProvider tokenProvider, KakaoOauthService kakaoOauthService, NaverOauthService naverOauthService, UserTokenRepository userTokenRepository) {
        this.loginService = loginService;
        this.tokenProvider = tokenProvider;
        this.kakaoOauthService = kakaoOauthService;
        this.naverOauthService = naverOauthService;
        this.userTokenRepository = userTokenRepository;
    }

    @PostMapping("/api/login")
    public ResponseMessage login(HttpServletResponse res,
                                 @RequestBody Map<String, String> request){

        String requestEmail = request.get("email");
        String requestPassword = request.get("password");
        User user = null;
        try{
            user = loginService.login(requestEmail,requestPassword);
        }catch (NoSuchAlgorithmException e){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(e);
        }catch (IllegalArgumentException e){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(e);
        }
        if(user != null){
            String token = tokenProvider.createToken(user.getId());
            String[] splitToken = token.split(" ");
            Optional<UserToken> userToken = userTokenRepository.find(user);

            if(userToken.isEmpty()){
                userTokenRepository.save(new UserToken(user.getId(), splitToken[0], splitToken[1]));
            }else{
                userTokenRepository.update(user.getId(), splitToken);
            }

            return new ResponseMessage(userToken.get().getAccessToken() + " " + userToken.get().getRefreshToken(), user.getId());
        }
        res.setStatus(HttpStatus.ACCEPTED.value());
        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "로그인 불가", new Date());
    }

    @PostMapping("/api/refresh")
    public ResponseMessage getAccessTokenByRefreshToken(@RequestBody Map<String, String> request){
        String accessToken = request.get("accessToken");
        String refreshToken = request.get("refreshToken");
        try{
            Optional<UserToken> userToken = userTokenRepository.findByToken(accessToken, refreshToken);
            boolean checkRefreshToken = tokenProvider.validateRefreshToken(refreshToken);
            if(checkRefreshToken){
                String token = tokenProvider.createToken(userToken.get().getUserId());
                String[] splitToken = token.split(" ");
                userTokenRepository.update(userTokenRepository.findByToken(accessToken, refreshToken).get().getUserId(), splitToken);
                return new ResponseMessage(HttpStatus.OK.value(), true, token , new Date());
            }else{
                return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "refresh token 만료, 재로그인하세요", new Date());
            }
        }catch (NoResultException | NoSuchElementException e){
            userTokenRepository.remove(accessToken, refreshToken);
        }
        return new ResponseMessage(HttpStatus.NO_CONTENT.value(), false, "토큰 불일치", new Date());
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
        return new ResponseMessage(tokenProvider.createToken(id), id);
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
        return new ResponseMessage(tokenProvider.createToken(id), id);
    }
}
