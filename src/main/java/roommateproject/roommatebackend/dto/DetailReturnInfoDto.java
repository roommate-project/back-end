package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.User;

import java.util.ArrayList;
import java.util.List;

@Data
public class DetailReturnInfoDto {
    private DetailUserInfo detailUserInfo;
    private int want_long;
    private String info;
    private List<Boolean> testList;
    private DetailHouseInfo detailHouseInfo;
    private List<Boolean> userTestList;

    public DetailReturnInfoDto(DetailUserInfo detailUserInfo, User findUser, DetailHouseInfo detailHouseInfo, User loginUser) {
        this.detailHouseInfo = detailHouseInfo;
        this.detailUserInfo = detailUserInfo;
        this.want_long = findUser.getHome().getWant_long();
        this.info = findUser.getHome().getInfo();
        List<Boolean> list1 = new ArrayList<>();
        list1.add(findUser.getHome().getQuestion1());
        list1.add(findUser.getHome().getQuestion2());
        list1.add(findUser.getHome().getQuestion3());
        list1.add(findUser.getHome().getQuestion4());
        list1.add(findUser.getHome().getQuestion5());
        list1.add(findUser.getHome().getQuestion6());

        List<Boolean> list2 = new ArrayList<>();
        list2.add(loginUser.getHome().getQuestion1());
        list2.add(loginUser.getHome().getQuestion2());
        list2.add(loginUser.getHome().getQuestion3());
        list2.add(loginUser.getHome().getQuestion4());
        list2.add(loginUser.getHome().getQuestion5());
        list2.add(loginUser.getHome().getQuestion6());

        this.testList = list1;
        this.userTestList = list2;
    }
}
