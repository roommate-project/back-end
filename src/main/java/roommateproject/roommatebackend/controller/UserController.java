package roommateproject.roommatebackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import roommateproject.roommatebackend.dto.EmailValidateDto;
import roommateproject.roommatebackend.dto.UserAddForm;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;
import roommateproject.roommatebackend.file.RepresentImageStore;
import roommateproject.roommatebackend.file.SocialImageStore;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@RestController
@AllArgsConstructor @Slf4j
public class UserController {

    @Value("${spring.ip}")
    private String ip;

    private final UserService userService;
    private final SendMailService mailSender;
    private final KakaoOauthService kakaoOauthService;
    private final NaverOauthService naverOauthService;
    private final RepresentImageStore representImageStore;
    private final SocialImageStore socialImageStore;
    private static final Map<String, String> emailCheck = new ConcurrentHashMap<>();
    private static final Map<String, Date> checkTime = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> emailCodeComplete = new ConcurrentHashMap<>();

    @GetMapping("/api/user")
    public ResponseMessage emailValidate(@RequestBody Map<String, String> request){
        return new ResponseMessage(userService.validateEmail(request.get("email")));
    }

    @GetMapping("/api/user/validate")
    public ResponseMessage checkEmail(@RequestParam(value = "email") @Email String requestEmail){
        String randomNumber = mailSender.sendEmail(requestEmail);
        if(randomNumber.equals("fail")){
            return new ResponseMessage(new EmailValidateDto(false,"이메일 보내기 불가",HttpStatus.BAD_REQUEST));
        }
        emailCheck.put(requestEmail,randomNumber);
        checkTime.put(requestEmail,new Date());
        return new ResponseMessage(new EmailValidateDto(true,"이메일 보내기 성공",HttpStatus.OK));
    }

    @PostMapping("/api/user/validate")
    public ResponseMessage checkEmailCode(@RequestParam(value = "email") @Email String requestEmail,
                                          @RequestBody HashMap<String, Object> request){

        String getCode = (String) request.get("emailCode");
        if(checkTime == null || emailCheck == null || checkTime.get(requestEmail) == null || emailCheck.get(requestEmail) == null){
            return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 잘못된 접근",HttpStatus.BAD_REQUEST));
        }
        long now = new Date().getTime();
        long sendTime = checkTime.get(requestEmail).getTime();
        if(now - sendTime > 10 * 1000 * 60){
            return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 10분이내 입력", HttpStatus.UNAUTHORIZED));
        }
        if(emailCheck.get(requestEmail).equals(getCode)){
            emailCodeComplete.put(requestEmail, true);
            emailCheck.remove(requestEmail);
            checkTime.remove(requestEmail);
            return new ResponseMessage(new EmailValidateDto(true,"이메일 인증 성공",HttpStatus.OK));
        }
        return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 인증코드 불일치",HttpStatus.BAD_REQUEST));
    }

    @PostMapping(value = "/api/user/add",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseMessage addUser(@RequestParam(value = "email") @Email String requestEmail,
                                   @RequestPart(value = "userAddForm") @Valid UserAddForm userAddForm,
                                   @RequestPart @NotBlank MultipartFile representFile,
                                   @RequestPart @Nullable List<MultipartFile> restFiles) throws IOException {

 /*       if(emailCodeComplete.get(requestEmail) == null){
            return new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), false, "이메일 검정 먼저 하세요",new Date());
        }

  */       //이메일 검정 안해도 되도록 해 놓음 -> 나중엔 주석 해제할것
        if(userAddForm.getPassword().length() < 8 || userAddForm.getPassword().length() > 20){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),false,"비밀번호는 8자 이상 20자 이하",new Date());
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");

        if(!pattern.matcher(userAddForm.getPassword()).matches()){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),false,"비밀번호는 숫자와 영문자로 구성",new Date());
        }
        userAddForm.setRegister("email");
        User user = new User(requestEmail,userAddForm);

        EmailValidateDto emailValidateDto= userService.validateEmail(requestEmail);
        if(emailValidateDto.isValidate()) {
            try {
                if(representFile.isEmpty() || representFile == null){
                    return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "대표사진 전송하세요", new Date());
                }
                UserImage userImage = representImageStore.storeFile(user,representFile);
                userService.join(user,userImage);
       //         emailCodeComplete.remove(requestEmail);
            }catch(NoSuchAlgorithmException e){
                return new ResponseMessage(e);
            }
        }else{
            return new ResponseMessage(emailValidateDto);
        }
        return new ResponseMessage("회원가입 성공");
    }

    @GetMapping("/api/user/add/oauth/kakao")
    public ResponseMessage kakaoAddUser(@RequestParam String code){
        //https://kauth.kakao.com/oauth/authorize?client_id=26fc82315434c1ec0e23b5a0aa5076e5&redirect_uri=http://localhost:8080/api/user/add/oauth/kakao&response_type=code

        Map<String, Object> kakaoUser = kakaoOauthService.createKakaoUser(kakaoOauthService.getKakaoAccessToken(code,"http://" + ip + "/api/user/add/oauth/kakao"));
        if(kakaoUser == null){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "제대로 정보 동의하세요",new Date());
        }
        try {
            User user = (User) kakaoUser.get("user");
            String path = (String) kakaoUser.get("image");
            Optional<User> findUser = userService.findByEmail(user.getEmail());
            if(!findUser.isEmpty()){
                return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "이미 회원가입함", new Date());
            }
            UserImage userImage = socialImageStore.storeFile(user, path, "KAKAO");
            userService.join(user,userImage);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ResponseMessage(HttpStatus.OK.value(), true, "카카오 회원가입 완료", new Date());
    }

    @GetMapping("/api/user/add/oauth/naver")
    public ResponseMessage naverAddUser(@RequestParam String code){

        String token = naverOauthService.getNaverAccessToken(code,ip + "/api/user/add/oauth/naver");
        Map<String, Object> naverUser = naverOauthService.createNaverUser(token);
        if(naverUser == null){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "제대로 정보 동의하세요",new Date());
        }
        try {
            User user = (User) naverUser.get("user");
            String path = (String) naverUser.get("image");
            Optional<User> findUser = userService.findByEmail(user.getEmail());
            if(!findUser.isEmpty()){
                return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "이미 회원가입함", new Date());
            }
            if(user.getGender().equals("M")){
                user.setGender("male");
            }else{
                user.setGender("female");
            }
            UserImage userImage = socialImageStore.storeFile(user, path, "NAVER");
            userService.join(user,userImage);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return new ResponseMessage(HttpStatus.OK.value(), true, "네이버 회원가입 완료", new Date());
    }

}
