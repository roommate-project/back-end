package roommateproject.roommatebackend.dto;

import lombok.Data;

@Data
public class MatchingDto {

    private Long userId;
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

    public MatchingDto(String representImage, String nickName, String info, String location, String gender, int age, Long homeId, Long userId, Long userImageId){
        this.representImage = representImage;
        this.nickName = nickName;
        this.info = info;
        this.location = location;
        this.gender = gender;
        this.age = age;
        this.homeId = homeId;
        this.userId = userId;
        this.userImageId = userImageId;
    }

}
