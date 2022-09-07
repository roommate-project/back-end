package roommateproject.roommatebackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.dto.LikeDto;
import roommateproject.roommatebackend.dto.LikeReturnDto;
import roommateproject.roommatebackend.entity.LikeIt;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;
import roommateproject.roommatebackend.repository.HomeRepository;
import roommateproject.roommatebackend.repository.ImageRepository;
import roommateproject.roommatebackend.repository.LikeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class LikeService {

    @Value("${spring.image.represent}")
    private String dir;
    private final LikeRepository likeRepository;
    private final ImageRepository imageRepository;
    private final HomeRepository homeRepository;

    public LikeService(LikeRepository likeRepository, ImageRepository imageRepository, HomeRepository homeRepository) {
        this.likeRepository = likeRepository;
        this.imageRepository = imageRepository;
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
        List<LikeIt> getLike = likeRepository.getAllLike(user, start);
        List<LikeDto> likeDtos = new ArrayList<>();
        getLike.forEach((l) -> {
            UserImage ui = imageRepository.getRepresentImage(l.getReceiver());
            likeDtos.add(new LikeDto(ui.getStoreFileName(),l.getReceiver(),l.getReceiver().getHome().getId(),l.getReceiver().getHome().getLocation()));
        });
        likeDtos.forEach((l) -> {
            l.setRepresentImage(dir + l.getRepresentImage());
            l.setQuestionNumber(homeRepository.getQuestionCount(l.getHomeId(), l.getUser()));
        });
        return likeDtos.stream().map(l -> new LikeReturnDto(l))
                .collect(Collectors.toList());
    }
}
