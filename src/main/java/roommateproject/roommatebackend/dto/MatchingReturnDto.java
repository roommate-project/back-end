package roommateproject.roommatebackend.dto;

import lombok.Data;

@Data
public class MatchingReturnDto {
    private String representImage;
    private String nickName;
    private String info;
    private String location;
    private String gender;
    private int age;
    private int likeNumber;
    private int questionCount;
    private Long userId;

    public MatchingReturnDto(MatchingDto matchingDto){
        this.representImage = matchingDto.getRepresentImage();
        this.nickName = matchingDto.getNickName();
        this.info = matchingDto.getInfo();
        this.location = matchingDto.getLocation();
        this.gender = matchingDto.getGender();
        this.age = matchingDto.getAge();
        this.likeNumber = matchingDto.getLikeNumber();
        this.questionCount = matchingDto.getQuestionCount();
        this.userId = matchingDto.getUserId();
    }

}
