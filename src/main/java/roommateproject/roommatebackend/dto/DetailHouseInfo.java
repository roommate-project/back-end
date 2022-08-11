package roommateproject.roommatebackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class DetailHouseInfo {

    private int room;
    private Integer cost;
    private String houseInfo;
    private List<String> photoUrls;

    public DetailHouseInfo(Integer room, Integer cost, String houseInfo, List<String> photoUrls) {
        this.room = room;
        if(room == 0){
            this.cost = null;
            this.houseInfo = null;
        } else {
            this.cost = cost;
            this.houseInfo = houseInfo;
        }
        this.photoUrls = photoUrls;
    }
}
