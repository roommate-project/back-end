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
    private final LikeService likeService;
    private final ImageRepository imageRepository;

    public MatchingController(MatchingService matchingService, UserService userService, LikeService likeService, ImageRepository imageRepository) {
        this.matchingService = matchingService;
        this.userService = userService;
        this.likeService = likeService;
        this.imageRepository = imageRepository;
    }

    @GetMapping("/api/match/{pageNumber}")
    public List<MatchingReturnDto> getAllUsers(@Login User loginUser,
                                               @PathVariable("pageNumber") int pageNumber){

        List<MatchingDto> findAllUsers =  matchingService.findAllUserPagination(loginUser,pageNumber);
        List<MatchingReturnDto> returnDtos = findAllUsers
                                                .stream().map(m -> new MatchingReturnDto(m))
                                                .collect(Collectors.toList());
        returnDtos.forEach((m) -> {
            if(returnDtos.indexOf(m) == 0){
                m.setIsLast(true);
            }else{
                m.setIsLast(false);
            }
        });

        return returnDtos;
    }

    @GetMapping("/api/match/filter/{pageNumber}")
    public List<MatchingReturnDto> filterUser(@Login User loginUser,
                              @PathVariable("pageNumber") int pageNumber,
                              @RequestParam(value = "rate",defaultValue = "0") int rate,
                              @RequestParam(value = "gender",defaultValue = "all") String gender,
                              @RequestParam(value = "wantLongMax",defaultValue = "100") int wantLongMax,
                              @RequestParam(value = "wantLongMin",defaultValue = "0") int wantLongMin,
                              @RequestParam(value = "ageMax",defaultValue = "100") int ageMax,
                              @RequestParam(value = "ageMin",defaultValue = "0") int ageMin,
                              @RequestParam(value = "costMax",defaultValue = "1000000000") int costMax,
                              @RequestParam(value = "costMin",defaultValue = "0") int costMin,
                              @RequestParam(value = "room0", defaultValue = "true") Boolean room0,
                              @RequestParam(value = "room1", defaultValue = "true") Boolean room1,
                              @RequestParam(value = "room2", defaultValue = "true") Boolean room2,
                              @RequestParam(value = "room3", defaultValue = "true") Boolean room3,
                              @RequestParam(value = "room4", defaultValue = "true") Boolean room4){

        List<MatchingDto> findAllUsers =  matchingService.findFilterUser(loginUser,pageNumber,rate * 6 / 100,gender,wantLongMax,wantLongMin,ageMax,ageMin,costMax,costMin,room0,room1,room2,room3,room4);
        findAllUsers.forEach(m -> m.setIsLiked(likeService.isLiked(loginUser, m.getUser())));

        List<MatchingReturnDto> returnDtos = findAllUsers.stream()
                                .map(m -> new MatchingReturnDto(m)).collect(Collectors.toList());
        returnDtos.forEach((m) -> {
            if(returnDtos.indexOf(m) == 0){
                m.setIsLast(true);
            }else{
                m.setIsLast(false);
            }
        });

        return returnDtos;
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
        DetailUserInfo detailUserInfo = new DetailUserInfo(findUser.getName(), findUser.getNickName(), findUser.getAge(), findUser.getHome().getLocation(), findUser.getGender(), findUser.getHome().getExperience(), findUser.getHome().getInfo());

        UserImage representImage = imageRepository.getRepresentImage(findUser);
        List<UserImage> restImages = imageRepository.getRestImage(findUser);

        List<Long> allImages = new ArrayList<>();
        allImages.add(representImage.getId());
        restImages.forEach(i -> allImages.add(i.getId()));
        DetailHouseInfo detailHouseInfo = new DetailHouseInfo(loginUser.getHome().getRoom(), loginUser.getHome().getCost(), loginUser.getHome().getHouseInfo(), allImages);

        return new DetailReturnInfoDto(detailUserInfo,findUser,detailHouseInfo,loginUser);
    }
}
