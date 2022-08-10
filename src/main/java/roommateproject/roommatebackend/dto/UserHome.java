package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.Home;
import roommateproject.roommatebackend.entity.User;

@Data
public class UserHome {
    private User user;
    private Home home;

    public UserHome(User user, Home home){
        this.user = user;
        this.home = home;
    }
}
