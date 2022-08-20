package roommateproject.roommatebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import roommateproject.roommatebackend.argumentresolver.Login;
import roommateproject.roommatebackend.dto.LikeReturnDto;
import roommateproject.roommatebackend.dto.UserDto;
import roommateproject.roommatebackend.dto.UserHomeDto;
import roommateproject.roommatebackend.entity.Home;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;
import roommateproject.roommatebackend.file.RepresentImageStore;
import roommateproject.roommatebackend.file.RestImageStore;
import roommateproject.roommatebackend.repository.ImageRepository;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.HomeService;
import roommateproject.roommatebackend.service.LikeService;
import roommateproject.roommatebackend.service.UserService;
import roommateproject.roommatebackend.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MyPageController {

    @Value("${spring.image.represent}")
    private String dir;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final HomeService homeService;
    private final ImageRepository imageRepository;
    private final RestImageStore restImageStore;
    private final RepresentImageStore representImageStore;
    private final LikeService likeService;

    public MyPageController(UserService userService, JwtTokenProvider jwtTokenProvider, HomeService homeService, ImageRepository imageRepository, RestImageStore restImageStore, RepresentImageStore representImageStore, LikeService likeService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.homeService = homeService;
        this.imageRepository = imageRepository;
        this.restImageStore = restImageStore;
        this.representImageStore = representImageStore;
        this.likeService = likeService;
    }

    @GetMapping("/api/mypage")
    public UserDto userInfo(@Login User loginUser){
        UserImage userImage = imageRepository.getRepresentImage(loginUser);
        UserDto info =  new UserDto(loginUser,userImage);
        info.setRepresentImage(dir + info.getRepresentImage());
        return info;
    }

    @PutMapping("/api/mypage")
    public ResponseMessage editUserInfo(@Login User loginUser,
                                        @RequestBody Map<String, String> requestBody){
        User user = null;
        try{
            user = userService.change(loginUser.getId(),requestBody);
        }catch(NullPointerException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return new ResponseMessage(user);
    }

    @GetMapping("/api/mypage/drop")
    public ResponseMessage eraseUser(@Login User loginUser){
        userService.erase(loginUser);
        return new ResponseMessage(HttpStatus.OK.value(), true, "회원탈퇴 완료", new Date());
    }

    @GetMapping("/api/mypage/home")
    public User get

    @GetMapping("/api/mypage/info")
    public UserHomeDto getUserHomeInfo(@Login User loginUser){
        return new UserHomeDto(loginUser.getHome());
    }

    @PostMapping("/api/mypage/info")
    public ResponseMessage saveUserHomeInfo(@Login User loginUser,
                                            @RequestBody UserHomeDto request){

        Home home = new Home(loginUser, request);
        homeService.save(home);
        return new ResponseMessage(HttpStatus.OK.value(), true, "주거 정보 저장 완료", new Date());
    }

    @PutMapping("/api/mypage/info")
    public ResponseMessage editUserHomeInfo(@Login User loginUser,
                                            @RequestBody UserHomeDto request){
        Home home = homeService.find(loginUser);
        homeService.change(loginUser, home, request);
        return new ResponseMessage(HttpStatus.OK.value(), true, "주거 정보 수정 완료", new Date());
    }

    @PostMapping("/api/mypage/image/rest")
    public ResponseMessage addRestImage(@Login User loginUser,
                                        @RequestPart @NotBlank List<MultipartFile> restImages) throws IOException {

        restImageStore.storeFiles(loginUser,restImages).forEach(i -> imageRepository.save(i));

        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 사진 저장 완료", new Date());
    }

    @PutMapping("/api/mypage/image/represent")
    public ResponseMessage editRepresentImage(@Login User loginUser,
                                        @RequestPart @NotBlank MultipartFile representImage) throws IOException {

        UserImage userImage = representImageStore.storeFile(loginUser, representImage);
        imageRepository.change(loginUser, userImage);

        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 대표 사진 수정 완료", new Date());
    }

    @PutMapping("/api/mypage/image/rest")
    public ResponseMessage editRestImage(@Login User loginUser,
                                        @RequestPart @NotBlank List<MultipartFile> restImages) throws IOException {

        List<UserImage> imageList = imageRepository.getRestImage(loginUser);
        if(imageList != null) {
            imageRepository.remove(imageList);
        }
        for (MultipartFile restImage : restImages) {
            UserImage userImage = restImageStore.storeFile(loginUser, restImage);
            imageRepository.save(userImage);
        }
        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 나머지 사진 수정 완료", new Date());
    }

    @DeleteMapping("/api/mypage/image/rest")
    public ResponseMessage removeRestImage(@Login User loginUser) {

        List<UserImage> imageList = imageRepository.getRestImage(loginUser);
        if(imageList != null) {
            imageRepository.remove(imageList);
        }
        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 나머지 사진 삭제 완료", new Date());
    }

    @GetMapping("/api/mypage/like/{pageNumber}")
    public List<LikeReturnDto> getLikeList(@Login User loginUser,
                                           @PathVariable("pageNumber") int pageNumber){

        return likeService.getLikeList(loginUser, pageNumber);
    }

}
