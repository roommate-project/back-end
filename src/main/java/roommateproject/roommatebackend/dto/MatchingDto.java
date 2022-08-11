package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.Home;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;

@Data
public class MatchingDto {

    private User user;
    private Long userImageId;
    private String representImage;
    private String nickName;
    private String info;
    private String location;
    private String gender;
    private int age;
    private int likeNumber;
    private int questionCount;
    private Long homeId;

    public MatchingDto(String representImage, String nickName, String info, String location, String gender, int age, Long homeId, User user, Long userImageId){
        this.representImage = representImage;
        this.nickName = nickName;
        this.info = info;
        this.location = location;
        this.gender = gender;
        this.age = age;
        this.homeId = homeId;
        this.user = user;
        this.userImageId = userImageId;
    }

    public MatchingDto(User user, Home home, UserImage userImage) {
        this.representImage = userImage.getStoreFileName();
        this.nickName = user.getNickName();
        this.info = home.getInfo();
        this.location = home.getLocation();
        this.gender = user.getGender();
        this.age = user.getAge();
        this.homeId = home.getId();
        this.user = user;
        this.userImageId = userImage.getId();
    }
}
