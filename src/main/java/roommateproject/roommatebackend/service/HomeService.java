package roommateproject.roommatebackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.dto.UserHomeDto;
import roommateproject.roommatebackend.entity.Home;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.repository.HomeRepository;

import java.util.List;
import java.util.Map;

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
    public void change(Home home, Map<String, Object> req){
        if(req.get("experience") != null){
            homeRepository.changeExperience(home, (Integer) req.get("experience"));
        }
        if(req.get("want_long") != null){
            homeRepository.changeWantLong(home, (Integer) req.get("want_long"));
        }
        if(req.get("room") != null){
            homeRepository.changeRoom(home, (Integer) req.get("room"));
        }
        if(req.get("cost") != null){
            homeRepository.changeCost(home, (Integer) req.get("cost"));
        }
        if(req.get("info") != null){
            homeRepository.changeInfo(home, (String) req.get("info"));
        }
        if(req.get("houseInfo") != null){
            homeRepository.changeHouseInfo(home, (String) req.get("houseInfo"));
        }
        if(req.get("location") != null){
            homeRepository.changeLocation(home, (String) req.get("location"));
        }
        if(req.get("dormitory") != null){
            homeRepository.changeDormitory(home, (String) req.get("dormitory"));
        }
    }

    public Home find(User user) {
        return homeRepository.find(user);
    }

    @Transactional
    public void saveQuestions(User loginUser, List<Boolean> question) {
        homeRepository.saveQuestions(loginUser, question);
    }
}
