package roommateproject.roommatebackend.dto;

import lombok.Data;

@Data
public class LikeReturnDto {

    private String location;
    private int want_long;
    private double questionPercent;
    private Long userId;
 //   private Long firstHomeImageId;
    private Long userImageId;
    private Boolean isLast;

    public LikeReturnDto(LikeDto likeDto){
        this.location = likeDto.getLocation();
        this.want_long = likeDto.getUser().getHome().getWant_long();
        this.questionPercent = Double.parseDouble(String.format("%.2f",(double) likeDto.getQuestionNumber() * 100.0 / 6.0));
        this.userId = likeDto.getUser().getId();
    }
}
