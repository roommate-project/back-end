package roommateproject.roommatebackend.entity;

import lombok.Getter;
import lombok.Setter;
import roommateproject.roommatebackend.dto.UserHomeDto;

import javax.persistence.*;

@Entity @Getter @Setter
public class Home {

    @Id
    @GeneratedValue
    @Column(name = "home_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user")
    private User user;

    private Integer experience;
    private Integer want_long;
    private Integer room;
    private Integer cost;
    private String info;
    private String houseInfo;
    private String location;
    private String dormitory;

    private Boolean question1;
    private Boolean question2;
    private Boolean question3;
    private Boolean question4;
    private Boolean question5;
    private Boolean question6;

    public Home(){}

    public Home(User user, UserHomeDto userHomeDto){
        this.user = user;
        this.experience = userHomeDto.getExperience();
        this.want_long = userHomeDto.getWant_long();
        this.room = userHomeDto.getRoom();
        this.cost = userHomeDto.getCost();
        this.info = userHomeDto.getInfo();
        this.houseInfo = userHomeDto.getHouseInfo();
        this.dormitory = userHomeDto.getDormitory();
        this.location = userHomeDto.getLocation();
        this.question1 = userHomeDto.getQuestion().get(0);
        this.question2 = userHomeDto.getQuestion().get(1);
        this.question3 = userHomeDto.getQuestion().get(2);
        this.question4 = userHomeDto.getQuestion().get(3);
        this.question5 = userHomeDto.getQuestion().get(4);
        this.question6 = userHomeDto.getQuestion().get(5);
 /*       this.question1 = userHomeDto.getQuestion1();
        this.question2 = userHomeDto.getQuestion2();
        this.question3 = userHomeDto.getQuestion3();
        this.question4 = userHomeDto.getQuestion4();
        this.question5 = userHomeDto.getQuestion5();
        this.question6 = userHomeDto.getQuestion6();

  */
    }
}
