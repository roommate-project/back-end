package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.Home;

@Data
public class UserHomeDto {

    private int experience;
    private int want_long;
    private int room;
    private int cost;
    private String info;

    private Boolean question1;
    private Boolean question2;
    private Boolean question3;
    private Boolean question4;
    private Boolean question5;
    private Boolean question6;

    public UserHomeDto(){}
    public UserHomeDto(Home home){
        this.experience = home.getExperience();
        this.want_long = home.getWant_long();
        this.room = home.getRoom();
        this.cost = home.getCost();
        this.info = home.getInfo();
        this.question1 = home.getQuestion1();
        this.question2 = home.getQuestion2();
        this.question3 = home.getQuestion3();
        this.question4 = home.getQuestion4();
        this.question5 = home.getQuestion5();
        this.question6 = home.getQuestion6();
    }
}
