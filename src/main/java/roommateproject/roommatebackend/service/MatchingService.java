package roommateproject.roommatebackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roommateproject.roommatebackend.dto.MatchingDto;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;
import roommateproject.roommatebackend.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
public class MatchingService {

    private final LikeRepository likeRepository;
    private final HomeRepository homeRepository;
    private final MatchingQueryRepository matchingQueryRepository;
    private final ImageRepository imageRepository;

    public MatchingService(LikeRepository likeRepository, HomeRepository homeRepository, MatchingQueryRepository matchingQueryRepository, ImageRepository imageRepository) {
        this.likeRepository = likeRepository;
        this.homeRepository = homeRepository;
        this.matchingQueryRepository = matchingQueryRepository;
        this.imageRepository = imageRepository;
    }

    public List<MatchingDto> findAllUserPagination(User user, int pageNumber){
        List<User> all = matchingQueryRepository.findAllUserPagination(user, pageNumber);
        List<MatchingDto> allMatch = new ArrayList<>();
        all.forEach((u) -> {
            MatchingDto matchingDto = new MatchingDto(imageRepository.getRepresentImage(u).getStoreFileName(),u.getNickName(),homeRepository.find(u).getInfo(),u.getHome().getLocation(),u.getGender(),u.getAge(),u.getHome().getId(),u,imageRepository.getRepresentImage(u).getId());
            matchingDto.setLikeNumber(likeRepository.getReceiverCount(matchingDto.getUser()));
            matchingDto.setQuestionCount(homeRepository.getQuestionCount(matchingDto.getHomeId(),user));
            allMatch.add(matchingDto);
        });
        return allMatch;
    }

    public List<MatchingDto> findFilterUser(User loginUser, int pageNumber, int rate, String gender, int wantLongMax, int wantLongMin, int ageMax, int ageMin, int costMax, int costMin, int roomMax, int roomMin) {
        List<MatchingDto> retFilterUser = new ArrayList<>();

        while(retFilterUser.size() < 10){
            List<User> getInfo = matchingQueryRepository.findFilter(loginUser, pageNumber,gender,wantLongMax,wantLongMin,ageMax,ageMin,costMax,costMin,roomMax,roomMin);
            if(getInfo == null || getInfo.size() == 0){
                return retFilterUser;
            }
            getInfo.forEach((u) -> {
                int cnt = 0;
                if(u.getHome().getQuestion1() && u.getHome().getQuestion1())cnt++;
                if(u.getHome().getQuestion2() && u.getHome().getQuestion2())cnt++;
                if(u.getHome().getQuestion3() && u.getHome().getQuestion3())cnt++;
                if(u.getHome().getQuestion4() && u.getHome().getQuestion4())cnt++;
                if(u.getHome().getQuestion5() && u.getHome().getQuestion5())cnt++;
                if(u.getHome().getQuestion6() && u.getHome().getQuestion6())cnt++;

                if(cnt > rate){
                    UserImage userImage = imageRepository.getRepresentImage(u);
                    retFilterUser.add(new MatchingDto(u,u.getHome(),userImage));
                }
            });
            pageNumber++;
        }


        return retFilterUser;
    }
}
