package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.Home;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;

import java.util.List;

@Data
public class UserHomeImage {
    private User user;
    private Home home;
    private UserImage userImage;

    public UserHomeImage(User user, Home home, List<UserImage> userImage){
        this.user = user;
        this.home = home;
        this.userImage = userImage.get(0);
    }
    public UserHomeImage(User user, Home home, UserImage userImage){
        this.user = user;
        this.home = home;
        this.userImage = userImage;
    }
}
