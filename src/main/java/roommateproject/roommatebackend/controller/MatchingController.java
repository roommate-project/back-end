package roommateproject.roommatebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roommateproject.roommatebackend.dto.MatchingDto;
import roommateproject.roommatebackend.dto.MatchingReturnDto;
import roommateproject.roommatebackend.dto.UserHome;
import roommateproject.roommatebackend.dto.UserHomeDto;
import roommateproject.roommatebackend.entity.Home;
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
    public List<MatchingReturnDto> getAllUsers(HttpServletRequest httpServletRequest,
                                               @PathVariable("pageNumber") int pageNumber){
        String[] requestToken = httpServletRequest.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        User user = userService.find(id);
        List<MatchingDto> findAllUsers =  matchingService.findAllUserPagination(user,pageNumber);
        findAllUsers.forEach(u -> u.setRepresentImage(dir + u.getRepresentImage()));
        return findAllUsers
                .stream().map(m -> new MatchingReturnDto(m))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/match/filter/{pageNumber}")
    public List<MatchingReturnDto> filterUser(HttpServletRequest httpServletRequest,
                              @PathVariable("pageNumber") int pageNumber,
                              @RequestParam(value = "rate",defaultValue = "0") int rate,
                              @RequestParam(value = "gender",defaultValue = "all") String gender,
                              @RequestParam(value = "experienceMax",defaultValue = "100") int experienceMax,
                              @RequestParam(value = "experienceMin",defaultValue = "0") int experienceMin,
                              @RequestParam(value = "ageMax",defaultValue = "100") int ageMax,
                              @RequestParam(value = "ageMin",defaultValue = "0") int ageMin){

        String[] requestToken = httpServletRequest.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        User user = userService.find(id);
        UserHome userhome = new UserHome(user,homeService.find(user));
        List<MatchingDto> findAllUsers =  matchingService.findFilterUser(userhome,pageNumber,rate * 6 / 100,gender,experienceMax,experienceMin,ageMax,ageMin);
        findAllUsers.forEach(u -> u.setRepresentImage(dir + u.getRepresentImage()));
        return findAllUsers
                .stream().map(m -> new MatchingReturnDto(m))
                .collect(Collectors.toList());
    }

    @PostMapping("/api/match/like")
    public ResponseMessage changeLike(HttpServletRequest httpServletRequest,
                                      @RequestBody Map<String, Object> req){
        String[] requestToken = httpServletRequest.getHeader("authorization").split(" ");
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());
        User reqUser = userService.find(id);
        Long likeId = Long.parseLong((String)req.get("id"));
        User likeUser = userService.find(likeId);
        LikeIt likeIt = new LikeIt(reqUser, likeUser);
        Boolean check = likeService.save(likeIt);

        if(check){
            return new ResponseMessage(HttpStatus.OK.value(), true, "좋아요 취소 완료", new Date());
        }

        return new ResponseMessage(HttpStatus.OK.value(), true, "좋아요 완료", new Date());
    }

    @GetMapping("/api/match/info/{userId}")
    public User getUserInfo(@PathVariable("userId") Long userId){
        return null;
    }
}
