package roommateproject.roommatebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roommateproject.roommatebackend.argumentresolver.Login;
import roommateproject.roommatebackend.dto.MatchingDto;
import roommateproject.roommatebackend.dto.MatchingReturnDto;
import roommateproject.roommatebackend.dto.UserHome;
import roommateproject.roommatebackend.entity.LikeIt;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.HomeService;
import roommateproject.roommatebackend.service.LikeService;
import roommateproject.roommatebackend.service.MatchingService;
import roommateproject.roommatebackend.service.UserService;
import roommateproject.roommatebackend.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class MatchingController {

    @Value("${spring.image.represent}")
    private String dir;

    private final MatchingService matchingService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final LikeService likeService;
    private final HomeService homeService;

    public MatchingController(MatchingService matchingService, UserService userService, JwtTokenProvider jwtTokenProvider, LikeService likeService, HomeService homeService) {
        this.matchingService = matchingService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.likeService = likeService;
        this.homeService = homeService;
    }

    @GetMapping("/api/match/{pageNumber}")
    public List<MatchingReturnDto> getAllUsers(@Login User loginUser,
                                               @PathVariable("pageNumber") int pageNumber){

        List<MatchingDto> findAllUsers =  matchingService.findAllUserPagination(loginUser,pageNumber);
        findAllUsers.forEach(u -> u.setRepresentImage(dir + u.getRepresentImage()));
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

        UserHome userhome = new UserHome(loginUser,homeService.find(loginUser));
        List<MatchingDto> findAllUsers =  matchingService.findFilterUser(userhome,pageNumber,rate * 6 / 100,gender,experienceMax,experienceMin,ageMax,ageMin);
        findAllUsers.forEach(u -> u.setRepresentImage(dir + u.getRepresentImage()));
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
    public User getUserInfo(@PathVariable("userId") Long userId,@Login User loginUser){
        return null;
    }
}
