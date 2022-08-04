package roommateproject.roommatebackend.service;

import org.springframework.stereotype.Service;
import roommateproject.roommatebackend.dto.MatchingDto;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.repository.HomeRepository;
import roommateproject.roommatebackend.repository.LikeRepository;
import roommateproject.roommatebackend.repository.MatchingQueryRepository;
import roommateproject.roommatebackend.repository.UserRepository;

import java.util.List;

@Service
public class MatchingService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final HomeRepository homeRepository;
    private final MatchingQueryRepository matchingQueryRepository;

    public MatchingService(UserRepository userRepository, LikeRepository likeRepository, HomeRepository homeRepository, MatchingQueryRepository matchingQueryRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.homeRepository = homeRepository;
        this.matchingQueryRepository = matchingQueryRepository;
    }

    public List<MatchingDto> findAllUser(Long id, int pageNumber){
        List<MatchingDto> all = matchingQueryRepository.findAllUser(id, pageNumber);
        all.forEach((m) -> {
            m.setLikeNumber(likeRepository.getReceiverCount(userRepository.find(m.getUserId())));
            m.setQuestionCount(homeRepository.getQuestionCount(m.getHomeId(),userRepository.find(id)));
        });
        return all;
    }
}
