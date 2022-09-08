package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.Home;

import java.util.List;

@Data
public class HomeDto {

    private String dormitory;
    private Integer cost;
    private Integer room;
    private String houseInfo;
    private List<Long> images;

    public HomeDto(){}

    public HomeDto(Home home, List<Long> images) {
        this.dormitory = home.getDormitory();
        this.cost = home.getCost();
        this.room = home.getRoom();
        this.houseInfo = home.getHouseInfo();
        this.images = images;
    }
}
