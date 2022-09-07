package roommateproject.roommatebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import roommateproject.roommatebackend.argumentresolver.Login;
import roommateproject.roommatebackend.dto.*;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@CrossOrigin
public class MyPageController {

    @Value("${spring.image.represent}")
    private String representDir;

    @Value("${spring.image.rest}")
    private String restDir;
    private final UserService userService;
    private final HomeService homeService;
    private final ImageRepository imageRepository;
    private final RestImageStore restImageStore;
    private final RepresentImageStore representImageStore;
    private final LikeService likeService;

    public MyPageController(UserService userService, HomeService homeService, ImageRepository imageRepository, RestImageStore restImageStore, RepresentImageStore representImageStore, LikeService likeService) {
        this.userService = userService;
        this.homeService = homeService;
        this.imageRepository = imageRepository;
        this.restImageStore = restImageStore;
        this.representImageStore = representImageStore;
        this.likeService = likeService;
    }

    @GetMapping("/api/mypage/{pageNumber}")
    public MypageDto userInfo(@Login User loginUser,
                              @PathVariable("pageNumber") int pageNumber){
        UserImage userImage = imageRepository.getRepresentImage(loginUser);
        List<UserImage> restImages = imageRepository.getRestImage(loginUser);
        UserDto info =  new UserDto(loginUser,userImage);
        info.setRepresentImage(representDir + info.getRepresentImage());
        return new MypageDto(info, new UserHomeDto(loginUser.getHome(), restImages, restDir), likeService.getLikeList(loginUser, pageNumber));
    }
/*
    @GetMapping("/api/mypage/info")
    public UserHomeDto getUserMatchInfo(@Login User loginUser){

        return new UserHomeDto(loginUser.getHome());
    }
    @GetMapping("/api/mypage/like/{pageNumber}")
    public List<LikeReturnDto> getLikeList(@Login User loginUser,
                                           @PathVariable("pageNumber") int pageNumber){

        return likeService.getLikeList(loginUser, pageNumber);
    }
 */
    @PutMapping("/api/mypage")
    public ResponseMessage editUserInfo(@Login User loginUser,
                                        HttpServletResponse res,
                                        @RequestBody Map<String, String> requestBody){
        User user = null;
        try{
            user = userService.change(loginUser.getId(),requestBody);
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
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
    public HomeDto getHomeInfo(@Login User loginUser){
        List<String> images = imageRepository.getRestImage(loginUser)
                .stream().map(i -> restDir + i.getStoreFileName())
                .collect(Collectors.toList());
        return new HomeDto(loginUser.getHome(),images);
    }


    @PostMapping("/api/mypage/info")
    public ResponseMessage saveUserHomeInfo(@Login User loginUser,
                                            @RequestBody UserHomeDto request){

        Home home = new Home(loginUser, request);
        homeService.save(home);
        return new ResponseMessage(HttpStatus.OK.value(), true, "주거 정보 저장 완료", new Date());
    }

    @PostMapping("/api/mypage/question")
    public ResponseMessage saveQuestions(@Login User loginUser,
                                         @RequestBody Map<String, List<Boolean>> questions){
        List<Boolean> question = questions.get("question");
        if(question.size() != 6){
            return new ResponseMessage(HttpStatus.NO_CONTENT.value(), false, "주거성향테스트 개수 오류", new Date());
        }
        homeService.saveQuestions(loginUser, question);
        return new ResponseMessage(HttpStatus.OK.value(), true, "주거성향테스트 저장 완료", new Date());
    }

    @PutMapping("/api/mypage/info")
    public ResponseMessage editUserHomeInfo(@Login User loginUser,
                                            @RequestBody Map<String, Object> request){

        Home home = homeService.find(loginUser);
        homeService.change(home, request);
        return new ResponseMessage(HttpStatus.OK.value(), true, "주거 정보 수정 완료", new Date());
    }

    @PostMapping(value = "/api/mypage/image/rest",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseMessage addRestImage(@Login User loginUser,
                                        @RequestPart @NotBlank List<MultipartFile> restImages) throws IOException {

        restImageStore.storeFiles(loginUser,restImages).forEach(i -> imageRepository.save(i));

        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 사진 저장 완료", new Date());
    }

    @PutMapping(value = "/api/mypage/image/represent",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseMessage editRepresentImage(@Login User loginUser,
                                        @RequestPart @NotBlank MultipartFile representImage) throws IOException {

        UserImage userImage = representImageStore.storeFile(loginUser, representImage);
        imageRepository.change(loginUser, userImage);

        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 대표 사진 수정 완료", new Date());
    }

    @PutMapping(value = "/api/mypage/image/rest",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseMessage editRestImage(@Login User loginUser,
                                        @RequestPart @NotBlank ImageEditDto imageEditDto,
                                        @RequestPart @NotBlank List<MultipartFile> restImages) throws IOException {

   //     List<UserImage> imageList = imageRepository.getRestImage(loginUser);
        for(Long id : imageEditDto.getImageId()) {
            imageRepository.remove(id);
        }
        for (MultipartFile restImage : restImages) {
            UserImage userImage = restImageStore.storeFile(loginUser, restImage);
            imageRepository.save(userImage);
        }
        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 나머지 사진 수정 완료", new Date());
    }

    @DeleteMapping("/api/mypage/image/rest")
    public ResponseMessage removeRestImage(@RequestBody Map<String,Map<String,List<Long>>> imageEditDto) {

   //     List<UserImage> imageList = imageRepository.getRestImage(loginUser);
        Map<String, List<Long>> imageId = imageEditDto.get("imageEditDto");
        for(Long id : imageId.get("imageId")) {
            imageRepository.remove(id);
        }
        return new ResponseMessage(HttpStatus.OK.value(), true, "회원 나머지 사진 삭제 완료", new Date());
    }
}
