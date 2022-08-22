package roommateproject.roommatebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roommateproject.roommatebackend.argumentresolver.Login;
import roommateproject.roommatebackend.dto.*;
import roommateproject.roommatebackend.entity.LikeIt;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;
import roommateproject.roommatebackend.repository.ImageRepository;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.HomeService;
import roommateproject.roommatebackend.service.LikeService;
import roommateproject.roommatebackend.service.MatchingService;
import roommateproject.roommatebackend.service.UserService;
import roommateproject.roommatebackend.token.JwtTokenProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin
public class MatchingController {

    @Value("${spring.image.represent}")
    private String representDir;
    @Value("${spring.image.rest}")
    private String restDir;

    private final MatchingService matchingService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final LikeService likeService;
    private final HomeService homeService;
    private final ImageRepository imageRepository;

    public MatchingController(MatchingService matchingService, UserService userService, JwtTokenProvider jwtTokenProvider, LikeService likeService, HomeService homeService, ImageRepository imageRepository) {
        this.matchingService = matchingService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.likeService = likeService;
        this.homeService = homeService;
        this.imageRepository = imageRepository;
    }

    @GetMapping("/api/match/{pageNumber}")
    public List<MatchingReturnDto> getAllUsers(@Login User loginUser,
                                               @PathVariable("pageNumber") int pageNumber){

        List<MatchingDto> findAllUsers =  matchingService.findAllUserPagination(loginUser,pageNumber);
        findAllUsers.forEach(u -> u.setRepresentImage(representDir + u.getRepresentImage()));
        return findAllUsers
                .stream().map(m -> new MatchingReturnDto(m))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/match/filter/{pageNumber}")
    public List<MatchingReturnDto> filterUser(@Login User loginUser,
                              @PathVariable("pageNumber") int pageNumber,
                              @RequestParam(value = "rate",defaultValue = "0") int rate,
                              @RequestParam(value = "gender",defaultValue = "all") String gender,
                              @RequestParam(value = "experienceMax",defaultValue = "100") int experienceMax,
                              @RequestParam(value = "experienceMin",defaultValue = "0") int experienceMin,
                              @RequestParam(value = "ageMax",defaultValue = "100") int ageMax,
                              @RequestParam(value = "ageMin",defaultValue = "0") int ageMin){

        List<MatchingDto> findAllUsers =  matchingService.findFilterUser(loginUser,pageNumber,rate * 6 / 100,gender,experienceMax,experienceMin,ageMax,ageMin);
        findAllUsers.forEach(u -> u.setRepresentImage(representDir + u.getRepresentImage()));
        return findAllUsers
                .stream().map(m -> new MatchingReturnDto(m))
                .collect(Collectors.toList());
    }

    @PostMapping("/api/match/like")
    public ResponseMessage changeLike(@Login User loginUser,
                                      @RequestBody Map<String, Object> req){
        Long likeId = Long.parseLong((String)req.get("id"));
        User likeUser = userService.find(likeId);
        LikeIt likeIt = new LikeIt(loginUser, likeUser);
        Boolean check = likeService.save(likeIt);

        if(check){
            return new ResponseMessage(HttpStatus.OK.value(), true, "좋아요 취소 완료", new Date());
        }

        return new ResponseMessage(HttpStatus.OK.value(), true, "좋아요 완료", new Date());
    }

    @GetMapping("/api/match/info/{userId}")
    public DetailReturnInfoDto getDetailInfo(@PathVariable("userId") Long userId, @Login User loginUser){
        User findUser = userService.find(userId);
        DetailUserInfo detailUserInfo = new DetailUserInfo(findUser.getName(), findUser.getNickName(), findUser.getAge(), findUser.getHome().getLocation(), findUser.getGender(), findUser.getHome().getExperience());

        UserImage representImage = imageRepository.getRepresentImage(loginUser);
        List<UserImage> restImages = imageRepository.getRestImage(loginUser);

        List<String> allImages = new ArrayList<>();
        allImages.add(representDir + representImage.getStoreFileName());
        if(restImages != null && restImages.size() != 0) {
            restImages.forEach(i -> allImages.add(restDir + i.getStoreFileName()));
        }
        DetailHouseInfo detailHouseInfo = new DetailHouseInfo(loginUser.getHome().getRoom(), loginUser.getHome().getCost(), loginUser.getHome().getHouseInfo(), allImages);

        return new DetailReturnInfoDto(detailUserInfo,findUser,detailHouseInfo,loginUser);
    }
}
