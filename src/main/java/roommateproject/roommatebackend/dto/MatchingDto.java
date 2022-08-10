package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.User;

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

    public MatchingDto(UserHomeImage userHomeImage) {
        this.representImage = userHomeImage.getUserImage().getStoreFileName();
        this.nickName = userHomeImage.getUser().getNickName();
        this.info = userHomeImage.getHome().getInfo();
        this.location = userHomeImage.getHome().getLocation();
        this.gender = userHomeImage.getUser().getGender();
        this.age = userHomeImage.getUser().getAge();
        this.homeId = userHomeImage.getHome().getId();
        this.user = userHomeImage.getUser();
        this.userImageId = userHomeImage.getUserImage().getId();
    }
}
