package roommateproject.roommatebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MatchingDto {
    private String representImage;
    private String nickName;
    private String info;
    private String location;
    private String gender;
    private int likeNumber;
    private int questionCount;
    private Long homeId;

    public MatchingDto(String representImage, String nickName, String info, String location, String gender, Long homeId){
        this.representImage = representImage;
        this.nickName = nickName;
        this.info = info;
        this.location = location;
        this.gender = gender;
        this.homeId = homeId;
    }
}
