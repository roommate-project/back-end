package roommateproject.roommatebackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.entity.Home;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.repository.HomeRepository;
import roommateproject.roommatebackend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class HomeService {

    private final HomeRepository homeRepository;
    private final UserRepository userRepository;

    public HomeService(HomeRepository homeRepository, UserRepository userRepository) {
        this.homeRepository = homeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Long save(Home home){
        return homeRepository.save(home);
    }

    @Transactional
    public void change(User loginUser, Home home, Map<String, Object> req){
        if(req.get("experience") != null){
            homeRepository.changeExperience(home, (String) req.get("experience"));
        }
        if(req.get("want_long") != null){
            homeRepository.changeWantLong(home, (String) req.get("want_long"));
        }
        if(req.get("room") != null){
            homeRepository.changeRoom(home, (String) req.get("room"));
        }
        if(req.get("cost") != null){
            homeRepository.changeCost(home, (String) req.get("cost"));
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
        if(req.get("age") != null){
            userRepository.changeAge(loginUser, (String) req.get("age"));
        }
        if(req.get("gender") != null){
            userRepository.changeGender(loginUser, (String) req.get("gender"));
        }
        if(req.get("question") != null){
            List<String> question = (List<String>) req.get("question");
            List<Boolean> booleanQuestion = new ArrayList<>();
            for (String q : question){
                if(q.equals("true")){
                    booleanQuestion.add(true);
                }else{
                    booleanQuestion.add(false);
                }
            }
            homeRepository.saveQuestions(loginUser, booleanQuestion);
        }
        if(req.get("nickName") != null){
            userRepository.changeNickName(loginUser, (String) req.get("nickName"));
        }
        if(req.get("name") != null){
            userRepository.changeName(loginUser, (String) req.get("name"));
        }
        if(req.get("password") != null){
            userRepository.changePassword(loginUser, (String) req.get("password"));
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
