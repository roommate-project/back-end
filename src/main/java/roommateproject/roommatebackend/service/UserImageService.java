package roommateproject.roommatebackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.entity.UserImage;
import roommateproject.roommatebackend.repository.UserImageRepository;

@Service
@Transactional(readOnly = true)
public class UserImageService {

    private final UserImageRepository userImageRepository;

    public UserImageService(UserImageRepository userImageRepository) {
        this.userImageRepository = userImageRepository;
    }

    @Transactional
    public Long join(UserImage userImage){
        UserImage saveUserImage = userImageRepository.save(userImage);
        return saveUserImage.getId();
    }
}
