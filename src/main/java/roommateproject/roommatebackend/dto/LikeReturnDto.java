package roommateproject.roommatebackend.dto;

import lombok.Data;

@Data
public class LikeReturnDto {

    private String representImage;
    private String location;
    private int questionNumber;
    private Long userId;

    public LikeReturnDto(LikeDto likeDto){
        this.representImage = likeDto.getRepresentImage();
        this.location = likeDto.getLocation();
        this.questionNumber = likeDto.getQuestionNumber();
        this.userId = likeDto.getUserId();
    }
}
