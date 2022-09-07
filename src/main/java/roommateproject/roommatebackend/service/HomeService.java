package roommateproject.roommatebackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.dto.UserHomeDto;
import roommateproject.roommatebackend.entity.Home;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.repository.HomeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HomeService {

    private final HomeRepository homeRepository;

    public HomeService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @Transactional
    public Long save(Home home){
        return homeRepository.save(home);
    }

    @Transactional
    public Long change(User user, Home home, UserHomeDto userHomeDto){
        return homeRepository.change(user, home, userHomeDto);
    }

    public Home find(User user) {
        return homeRepository.find(user);
    }

    @Transactional
    public void saveQuestions(User loginUser, List<Boolean> question) {
        homeRepository.saveQuestions(loginUser, question);
    }
}
