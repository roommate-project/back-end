package roommateproject.roommatebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import roommateproject.roommatebackend.file.RestImageStore;
import roommateproject.roommatebackend.file.SocialImageStore;
import roommateproject.roommatebackend.repository.ImageRepository;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@RestController
@Slf4j
@CrossOrigin
public class UserController {

    @Value("${spring.ip}")
    private String ip;

    private final UserService userService;
    private final SendMailService mailSender;
    private final KakaoOauthService kakaoOauthService;
    private final NaverOauthService naverOauthService;
    private final RepresentImageStore representImageStore;
    private final RestImageStore restImageStore;
    private final ImageRepository imageRepository;
    private final SocialImageStore socialImageStore;
    private static final Map<String, String> emailCheck = new ConcurrentHashMap<>();
    private static final Map<String, Date> checkTime = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> emailCodeComplete = new ConcurrentHashMap<>();

    public UserController(UserService userService, SendMailService mailSender, KakaoOauthService kakaoOauthService, NaverOauthService naverOauthService, RepresentImageStore representImageStore, RestImageStore restImageStore, ImageRepository imageRepository, SocialImageStore socialImageStore) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.kakaoOauthService = kakaoOauthService;
        this.naverOauthService = naverOauthService;
        this.representImageStore = representImageStore;
        this.restImageStore = restImageStore;
        this.imageRepository = imageRepository;
        this.socialImageStore = socialImageStore;
    }

    @ResponseBody
    @GetMapping(value = "/api/user/{userId}/img/represents", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadRepresentImage(@PathVariable Long userId) throws MalformedURLException {
     //   log.info("image : {}",representImageStore.getFullPath(imageRepository.getRepresentImage(userService.find(userId)).getStoreFileName()));
        return new UrlResource("file:" + representImageStore.getFullPath(imageRepository.getRepresentImage(userService.find(userId)).getStoreFileName()));
    }

    @ResponseBody
    @GetMapping(value = "/api/user/{userId}/img/rest/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadRestImage(@PathVariable Long userId,
                                      @PathVariable Long imageId) throws MalformedURLException {

        return new UrlResource("file:" + restImageStore.getFullPath(imageRepository.getRestImage(userService.find(userId)).stream().filter(i -> i.getId().equals(imageId)).findAny().get().getStoreFileName()));
    }


    @PostMapping("/api/user/emailValidate")
    public ResponseMessage emailValidate(@RequestBody Map<String, String> request){
        return new ResponseMessage(userService.validateEmail(request.get("email")));
    }

    @GetMapping("/api/user/validate")
    public ResponseMessage checkEmail(@RequestParam(value = "email") @Email String requestEmail,
                                      HttpServletResponse res){

        String randomNumber = mailSender.sendEmail(requestEmail);
        if(randomNumber.equals("fail")){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(new EmailValidateDto(false,"이메일 보내기 불가",HttpStatus.BAD_REQUEST));
        }
        emailCheck.put(requestEmail,randomNumber);
        checkTime.put(requestEmail,new Date());
        return new ResponseMessage(new EmailValidateDto(true,"이메일 보내기 성공",HttpStatus.OK));
    }

    @PostMapping("/api/user/validate")
    public ResponseMessage checkEmailCode(@RequestParam(value = "email") @Email String requestEmail,
                                          @RequestBody HashMap<String, Object> request,
                                          HttpServletResponse res){

        String getCode = (String) request.get("emailCode");
        if(checkTime == null || emailCheck == null || checkTime.get(requestEmail) == null || emailCheck.get(requestEmail) == null){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 잘못된 접근",HttpStatus.BAD_REQUEST));
        }
        long now = new Date().getTime();
        long sendTime = checkTime.get(requestEmail).getTime();
        if(now - sendTime > 10 * 1000 * 60){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 10분이내 입력", HttpStatus.UNAUTHORIZED));
        }
        if(emailCheck.get(requestEmail).equals(getCode)){
            emailCodeComplete.put(requestEmail, true);
            emailCheck.remove(requestEmail);
            checkTime.remove(requestEmail);
            return new ResponseMessage(new EmailValidateDto(true,"이메일 인증 성공",HttpStatus.OK));
        }
        res.setStatus(HttpStatus.ACCEPTED.value());
        return new ResponseMessage(new EmailValidateDto(false,"이메일 인증 실패 : 인증코드 불일치",HttpStatus.BAD_REQUEST));
    }

    @PostMapping(value = "/api/user/add",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseMessage addUser(HttpServletResponse res,
                                   @RequestParam(value = "email") @Email String requestEmail,
                                   @RequestPart(value = "userAddForm") @Valid UserAddForm userAddForm,
                                   @RequestPart MultipartFile representFile,
                                   @RequestPart @Nullable List<MultipartFile> restFiles) throws IOException {

        if(emailCodeComplete.get(requestEmail) == null){
            return new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), false, "이메일 검정 먼저 하세요",new Date());
        }

        if(userAddForm.getPassword().length() < 8 || userAddForm.getPassword().length() > 20){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),false,"비밀번호는 8자 이상 20자 이하",new Date());
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");

        if(!pattern.matcher(userAddForm.getPassword()).matches()){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),false,"비밀번호는 숫자와 영문자로 구성",new Date());
        }
        User user = new User(requestEmail,userAddForm, "email");

        EmailValidateDto emailValidateDto= userService.validateEmail(requestEmail);
        if(emailValidateDto.isValidate()) {
            try {
                if(representFile.isEmpty() || representFile == null){
                    res.setStatus(HttpStatus.ACCEPTED.value());
                    return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "대표사진 전송하세요", new Date());
                }
                UserImage userImage = representImageStore.storeFile(user,representFile);
                userService.join(user,userImage);
                emailCodeComplete.remove(requestEmail);   //이메일 검정 안해도 되도록 해 놓음 -> 나중엔 주석 해제할것
            }catch(NoSuchAlgorithmException e){
                res.setStatus(HttpStatus.ACCEPTED.value());
                return new ResponseMessage(e);
            }
        }else{
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(emailValidateDto);
        }
        return new ResponseMessage("회원가입 성공", user.getId());
    }

    @GetMapping("/api/user/add/oauth/kakao")
    public ResponseMessage kakaoAddUser(@RequestParam String code,
                                        HttpServletResponse res){

        Map<String, Object> kakaoUser = kakaoOauthService.createKakaoUser(kakaoOauthService.getKakaoAccessToken(code,"http://" + ip + "/api/user/add/oauth/kakao"));
        if(kakaoUser == null){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "제대로 정보 동의하세요",new Date());
        }
        try {
            User user = (User) kakaoUser.get("user");
            String path = (String) kakaoUser.get("image");
            Optional<User> findUser = userService.findByEmail(user.getEmail());
            if(!findUser.isEmpty()){
                res.setStatus(HttpStatus.ACCEPTED.value());
                return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "이미 회원가입함", new Date());
            }
            UserImage userImage = socialImageStore.storeFile(user, path, "KAKAO");
            userService.join(user,userImage);
        } catch (NoSuchAlgorithmException e) {
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(e);
        }catch (IOException e){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(e);
        }
        return new ResponseMessage(HttpStatus.OK.value(), true, "카카오 회원가입 완료", new Date());
    }

    @GetMapping("/api/user/add/oauth/naver")
    public ResponseMessage naverAddUser(@RequestParam String code,
                                        HttpServletResponse res){

        String token = naverOauthService.getNaverAccessToken(code,"http:/" + ip + "/api/user/add/oauth/naver");
        Map<String, Object> naverUser = naverOauthService.createNaverUser(token);
        if(naverUser == null){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), false, "제대로 정보 동의하세요",new Date());
        }
        try {
            User user = (User) naverUser.get("user");
            String path = (String) naverUser.get("image");
            Optional<User> findUser = userService.findByEmail(user.getEmail());
            if(!findUser.isEmpty()){
                res.setStatus(HttpStatus.ACCEPTED.value());
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
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(e);
        }catch (IOException e){
            res.setStatus(HttpStatus.ACCEPTED.value());
            return new ResponseMessage(e);
        }

        return new ResponseMessage(HttpStatus.OK.value(), true, "네이버 회원가입 완료", new Date());
    }

}
