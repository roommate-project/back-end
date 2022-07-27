package roommateproject.roommatebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.LoginService;
import roommateproject.roommatebackend.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

@RestController @Slf4j
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenProvider tokenProvider;

    public LoginController(LoginService loginService, JwtTokenProvider tokenProvider) {
        this.loginService = loginService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/api/login")
    public String login(@RequestBody Map<String, String> request){

        String requestEmail = request.get("email");
        String requestPassword = request.get("password");
        boolean check;
        try{
            check = loginService.login(requestEmail,requestPassword);
        }catch (NoSuchAlgorithmException e){
            ResponseMessage responseMessage = new ResponseMessage(e);
            return responseMessage.toString();
        }catch (IllegalArgumentException e){
            ResponseMessage responseMessage = new ResponseMessage(e);
            return responseMessage.toString();
        }
        if(check){
            return tokenProvider.createToken(requestEmail);
        }
        ResponseMessage responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "로그인 불가", new Date());
        return responseMessage.toString();
    }

    @GetMapping("/api/login")
    public String a(HttpServletRequest request){
        Enumeration<String> paramKeys = request.getHeaderNames();
        while (paramKeys.hasMoreElements()) {
            String key = paramKeys.nextElement();
            log.info("{} -> {}",key,request.getHeader(key));
        }
 //       req.forEach((key, value) -> log.info("{} : {}",key,value));
        return "A";
    }
}
