package roommateproject.roommatebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roommateproject.roommatebackend.dto.MatchingDto;
import roommateproject.roommatebackend.entity.LikeIt;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.repository.MatchingQueryRepository;
import roommateproject.roommatebackend.response.ResponseMessage;
import roommateproject.roommatebackend.service.LikeService;
import roommateproject.roommatebackend.service.UserService;
import roommateproject.roommatebackend.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class MatchingController {

    @Value("${spring.image.represent}")
    private String dir;

    private final MatchingQueryRepository matchingQueryRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final LikeService likeService;

    public MatchingController(MatchingQueryRepository matchingQueryRepository, UserService userService, JwtTokenProvider jwtTokenProvider, LikeService likeService) {
        this.matchingQueryRepository = matchingQueryRepository;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.likeService = likeService;
    }

    @GetMapping("/api/match")
    public List<MatchingDto> getAllUsers(){
        List<MatchingDto> findAllUsers =  matchingQueryRepository.findAllUser();
        findAllUsers.forEach(u -> u.setRepresentImage(dir + u.getRepresentImage()));

        return findAllUsers;
    }

    @PostMapping("/api/match/like")
    public ResponseMessage changeLike(HttpServletRequest requestServlet,
                                      @RequestBody Map<String, Object> req){
        String[] requestToken = requestServlet.getHeader("authorization").split(" ");
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
}
