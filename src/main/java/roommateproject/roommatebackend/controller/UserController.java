package roommateproject.roommatebackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import roommateproject.roommatebackend.dto.EmailValidateDto;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.SendMailService;
import roommateproject.roommatebackend.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor @Slf4j
public class UserController {

    private final UserService userService;
    private final SendMailService mailSender;
    private static final Map<String, String> uuidCheck = new HashMap<>();
    private static final Map<String, Date> uuidTime = new HashMap<>();

    @GetMapping("/api/user/email/validate")
    public ResponseMessage emailValidate(@RequestParam(value = "email") String requestEmail){
        return new ResponseMessage(userService.validateEmail(requestEmail));
    }

    @GetMapping("/api/user/code/validate")
    public ResponseMessage checkEmail(@RequestParam(value = "email") String requestEmail){
        String uuid = mailSender.sendEmail(requestEmail);
        if(uuid.equals("fail")){
            return new ResponseMessage(new EmailValidateDto(false,"이메일 보내기 불가",HttpStatus.BAD_REQUEST));
        }
        uuidCheck.put(requestEmail,uuid);
        uuidTime.put(requestEmail,new Date());
        return new ResponseMessage(new EmailValidateDto(true,"이메일 보내기 성공",HttpStatus.OK));
    }

    @PostMapping("/api/user/code/validate")
    public ResponseMessage checkEmailCode(@RequestParam(value = "email") String requestEmail,
                                          @RequestBody HashMap<String, Object> request){

        String getCode = (String) request.get("emailCode");
        long now = new Date().getTime();
        long sendTime = uuidTime.get(requestEmail).getTime();
        if(now - sendTime >  3 * 1000 * 60){
            return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 3분이내 입력", HttpStatus.UNAUTHORIZED));
        }
        if(uuidCheck.get(requestEmail).equals(getCode)){
            return new ResponseMessage(new EmailValidateDto(true,"이메일 인증 성공",HttpStatus.OK));
        }
        return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 인증코드 불일치",HttpStatus.BAD_REQUEST));
    }
}
