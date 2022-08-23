package roommateproject.roommatebackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class MypageDto {
    private UserDto userBasicInfo;
    private UserHomeDto matchingInfo;
    private List<LikeReturnDto> likeListData;

    public MypageDto(UserDto info, UserHomeDto userHomeDto, List<LikeReturnDto> likeList) {
        this.userBasicInfo = info;
        this.matchingInfo = userHomeDto;
        this.likeListData = likeList;
    }
}
