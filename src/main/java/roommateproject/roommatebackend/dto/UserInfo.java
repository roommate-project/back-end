package roommateproject.roommatebackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {

    private String userName;
    private Long userId;
    private int age;
    private String location;

    public UserInfo(String userName, Long userId, int age, String location) {
        this.userName = userName;
        this.userId = userId;
        this.age = age;
        this.location = location;
    }
}
