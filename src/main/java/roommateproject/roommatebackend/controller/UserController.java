package roommateproject.roommatebackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roommateproject.roommatebackend.dto.EmailValidateDto;
import roommateproject.roommatebackend.dto.UserAddForm;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.SendMailService;
import roommateproject.roommatebackend.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@AllArgsConstructor @Slf4j
public class UserController {

    private final UserService userService;
    private final SendMailService mailSender;
    private static final Map<String, String> emailCheck = new ConcurrentHashMap<>();
    private static final Map<String, Date> checkTime = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> emailCodeComplete = new ConcurrentHashMap<>();

    @GetMapping("/api/user/email/validate")
    public ResponseMessage emailValidate(@RequestParam(value = "email") @Email String requestEmail){
        return new ResponseMessage(userService.validateEmail(requestEmail));
    }

    @GetMapping("/api/user/code/validate")
    public ResponseMessage checkEmail(@RequestParam(value = "email") @Email String requestEmail){
        String randomNumber = mailSender.sendEmail(requestEmail);
        if(randomNumber.equals("fail")){
            return new ResponseMessage(new EmailValidateDto(false,"이메일 보내기 불가",HttpStatus.BAD_REQUEST));
        }
        emailCheck.put(requestEmail,randomNumber);
        checkTime.put(requestEmail,new Date());
        return new ResponseMessage(new EmailValidateDto(true,"이메일 보내기 성공",HttpStatus.OK));
    }

    @PostMapping("/api/user/code/validate")
    public ResponseMessage checkEmailCode(@RequestParam(value = "email") @Email String requestEmail,
                                          @RequestBody HashMap<String, Object> request){

        String getCode = (String) request.get("emailCode");
        if(checkTime == null || emailCheck == null || checkTime.get(requestEmail) == null || emailCheck.get(requestEmail) == null){
            return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 잘못된 접근",HttpStatus.BAD_REQUEST));
        }
        long now = new Date().getTime();
        long sendTime = checkTime.get(requestEmail).getTime();
        if(now - sendTime >  3 * 1000 * 60){
            return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 3분이내 입력", HttpStatus.UNAUTHORIZED));
        }
        if(emailCheck.get(requestEmail).equals(getCode)){
            emailCodeComplete.put(requestEmail, true);
            return new ResponseMessage(new EmailValidateDto(true,"이메일 인증 성공",HttpStatus.OK));
        }
        return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 인증코드 불일치",HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/api/user/add")
    public ResponseMessage addUser(@RequestParam(value = "email") @Email String requestEmail,
                                   @RequestBody @Valid UserAddForm userAddForm){

 /*       if(emailCodeComplete.get(requestEmail) == null){
            return new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), false, "이메일 검정 먼저 하세요",new Date());
        }

  */       //나중엔 주석 해제할것
        User user = new User(requestEmail,userAddForm);
        EmailValidateDto emailValidateDto= userService.validateEmail(requestEmail);
        if(emailValidateDto.isValidate()) {
            try {
                userService.join(user);
            }catch(NoSuchAlgorithmException e){
                return new ResponseMessage(e);
            }
        }else{
            return new ResponseMessage(emailValidateDto);
        }
        return new ResponseMessage(user);
    }
}
