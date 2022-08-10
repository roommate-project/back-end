package roommateproject.roommatebackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roommateproject.roommatebackend.dto.MatchingDto;
import roommateproject.roommatebackend.dto.UserHome;
import roommateproject.roommatebackend.dto.UserHomeImage;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;
import roommateproject.roommatebackend.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
public class MatchingService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final HomeRepository homeRepository;
    private final MatchingQueryRepository matchingQueryRepository;
    private final ImageRepository imageRepository;

    public MatchingService(UserRepository userRepository, LikeRepository likeRepository, HomeRepository homeRepository, MatchingQueryRepository matchingQueryRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.homeRepository = homeRepository;
        this.matchingQueryRepository = matchingQueryRepository;
        this.imageRepository = imageRepository;
    }

    public List<MatchingDto> findAllUserPagination(User user, int pageNumber){
        List<MatchingDto> all = matchingQueryRepository.findAllUserPagination(user, pageNumber);
        all.forEach((m) -> {
            m.setLikeNumber(likeRepository.getReceiverCount(m.getUser()));
            m.setQuestionCount(homeRepository.getQuestionCount(m.getHomeId(),user));
        });
        return all;
    }

    public List<MatchingDto> findFilterUser(UserHome userHome, int pageNumber, int rate, String gender, int experienceMax, int experienceMin, int ageMax, int ageMin) {
        List<MatchingDto> retFilterUser = new ArrayList<>();
        log.info("user : {}",userHome.getUser().getEmail());
        log.info("home : {}",userHome.getHome().getId());

        while(retFilterUser.size() < 10){
            List<User> getInfo = matchingQueryRepository.findFilter(userHome, pageNumber,gender,experienceMax,experienceMin,ageMax,ageMin);
              //조건 확인 및 리턴값에 추가
            if(getInfo == null || getInfo.size() == 0){
                return retFilterUser;
            }
            getInfo.forEach((u) -> {
                int cnt = 0;
                log.info("user : {}",u.getEmail());
                log.info("home : {}",u.getHome().getId());
                if(u.getHome().getQuestion1() && u.getHome().getQuestion1())cnt++;
                if(u.getHome().getQuestion2() && u.getHome().getQuestion2())cnt++;
                if(u.getHome().getQuestion3() && u.getHome().getQuestion3())cnt++;
                if(u.getHome().getQuestion4() && u.getHome().getQuestion4())cnt++;
                if(u.getHome().getQuestion5() && u.getHome().getQuestion5())cnt++;
                if(u.getHome().getQuestion6() && u.getHome().getQuestion6())cnt++;
                log.info("{} : {}",rate,cnt);
                if(cnt > rate){
                    UserImage userImage = imageRepository.getRepresentImage(u);
                    retFilterUser.add(new MatchingDto(new UserHomeImage(u,u.getHome(),userImage)));
                }
            });
            pageNumber++;
        }


        return retFilterUser;
    }
}
