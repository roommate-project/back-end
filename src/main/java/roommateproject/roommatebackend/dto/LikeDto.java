package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.LikeIt;

@Data
public class LikeDto {

    private String representImage;
    private Long userId;
    private Long homeId;
    private String location;
    private int questionNumber;

    public LikeDto(String representImage, Long userId, Long homeId, String location){
        this.representImage = representImage;
        this.location = location;
        this.homeId = homeId;
        this.userId = userId;
    }
}
