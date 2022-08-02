package roommateproject.roommatebackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
@AllArgsConstructor
public class MyPageController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final HomeService homeService;
    private final ImageRepository imageRepository;
    private final RestImageStore restImageStore;
    private final RepresentImageStore representImageStore;

    @GetMapping("/api/mypage")
    public UserDto userInfo(HttpServletRequest request){
        String[] requestToken = request.getHeader("authorization").split(" ");
        User findUser = userService.find(Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString()));
        UserImage userImage = imageRepository.getRepresentImage(findUser);
        return new UserDto(findUser,userImage);
    }

    @PutMapping("/api/mypage")
    public ResponseMessage editUserInfo(HttpServletRequest requestServlet,
                                        @RequestBody Map<String, String> requestBody){
        User user = null;
        try{
            String[] requestToken = requestServlet.getHeader("authorization").split(" ");
            user = userService.change(Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString()),requestBody);
        }catch(NullPointerException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        log.info("수정 : {}",user.getName());
        return new ResponseMessage(user);
    }

    @GetMapping("/api/mypage/drop")
    public ResponseMessage eraseUser(HttpServletRequest requestServlet){
        String[] requestToken = requestServlet.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        userService.erase(userService.find(id));
        return new ResponseMessage(HttpStatus.OK.value(), true, "회원탈퇴 완료", new Date());
    }

    @GetMapping("/api/mypage/info")
    public UserHomeDto getUserHomeInfo(HttpServletRequest requestServlet){
        String[] requestToken = requestServlet.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        return new UserHomeDto(homeService.find(userService.find(id)));
    }

    @PostMapping("/api/mypage/info")
    public ResponseMessage saveUserHomeInfo(HttpServletRequest requestServlet,
                                            @RequestBody UserHomeDto request){
        String[] requestToken = requestServlet.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        User user = userService.find(id);
        Home home = new Home(user, request);
        homeService.save(home);
        return new ResponseMessage(HttpStatus.OK.value(), true, "주거 정보 저장 완료", new Date());
    }

    @PutMapping("/api/mypage/info")
    public ResponseMessage editUserHomeInfo(HttpServletRequest requestServlet,
                                            @RequestBody UserHomeDto request){
        String[] requestToken = requestServlet.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        User user = userService.find(id);
        Home home = homeService.find(user);
        homeService.change(user, home, request);
        return new ResponseMessage(HttpStatus.OK.value(), true, "주거 정보 수정 완료", new Date());
    }

    @PostMapping("/api/mypage/image/rest")
    public ResponseMessage addRestImage(HttpServletRequest requestServlet,
                                        @RequestPart @NotBlank List<MultipartFile> restFiles) throws IOException {

        String[] requestToken = requestServlet.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        User user = userService.find(id);
        for (MultipartFile restFile : restFiles) {
            UserImage userImage = restImageStore.storeFile(user, restFile);
            imageRepository.save(userImage);
        }

        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 사진 저장 완료", new Date());
    }

    @PutMapping("/api/mypage/image/represent")
    public ResponseMessage editRepresentImage(HttpServletRequest requestServlet,
                                        @RequestPart @NotBlank MultipartFile representFile) throws IOException {

        String[] requestToken = requestServlet.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        User user = userService.find(id);
        UserImage userImage = representImageStore.storeFile(user, representFile);
        imageRepository.change(user, userImage);

        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 대표 사진 수정 완료", new Date());
    }

    @PutMapping("/api/mypage/image/rest")
    public ResponseMessage editRestImage(HttpServletRequest requestServlet,
                                        @RequestPart @NotBlank List<MultipartFile> restFiles) throws IOException {

        String[] requestToken = requestServlet.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        User user = userService.find(id);
        List<UserImage> imageList = imageRepository.getRestImage(user);
        if(imageList != null) {
            imageRepository.remove(imageList);
        }
        for (MultipartFile restFile : restFiles) {
            UserImage userImage = restImageStore.storeFile(user, restFile);
            imageRepository.save(userImage);
        }
        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 나머지 사진 수정 완료", new Date());
    }

    @DeleteMapping("/api/mypage/image/rest")
    public ResponseMessage removeRestImage(HttpServletRequest requestServlet) {

        String[] requestToken = requestServlet.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        User user = userService.find(id);
        List<UserImage> imageList = imageRepository.getRestImage(user);
        if(imageList != null) {
            imageRepository.remove(imageList);
        }
        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 나머지 사진 삭제 완료", new Date());
    }
}
