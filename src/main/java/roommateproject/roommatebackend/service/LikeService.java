package roommateproject.roommatebackend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.entity.LikeIt;
import roommateproject.roommatebackend.repository.LikeRepository;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

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
}
