package roommateproject.roommatebackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.dto.LikeDto;
import roommateproject.roommatebackend.dto.LikeReturnDto;
import roommateproject.roommatebackend.entity.LikeIt;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.repository.HomeRepository;
import roommateproject.roommatebackend.repository.LikeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LikeService {

    @Value("${spring.image.represent}")
    private String dir;
    private final LikeRepository likeRepository;
    private final HomeRepository homeRepository;

    public LikeService(LikeRepository likeRepository, HomeRepository homeRepository) {
        this.likeRepository = likeRepository;
        this.homeRepository = homeRepository;
    }

    @Transactional
    public Boolean save(LikeIt likeIt){
        Boolean ret = false;
        if(likeRepository.exists(likeIt)){
            ret = true;
            likeRepository.erase(likeIt);
        }else {
            likeRepository.save(likeIt);
        }
        return ret;
    }

    public List<LikeReturnDto> getLikeList(User user, int start) {
        List<LikeDto> getLike = likeRepository.getAllLike(user, start);
        getLike.forEach((l) -> {
            l.setRepresentImage(dir + l.getRepresentImage());
            l.setQuestionNumber(homeRepository.getQuestionCount(l.getHomeId(), user));
        });
        return getLike.stream().map(l -> new LikeReturnDto(l))
                .collect(Collectors.toList());
    }
}
