package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.LikeIt;
import roommateproject.roommatebackend.entity.User;

@Data
public class LikeDto {

    private String representImage;
    private User user;
    private Long homeId;
    private String location;
    private int questionNumber;

    public LikeDto(String representImage, User user, Long homeId, String location){
        this.representImage = representImage;
        this.location = location;
        this.homeId = homeId;
        this.user = user;
    }
}
