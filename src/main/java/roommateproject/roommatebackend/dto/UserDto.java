package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.User;

@Data
public class UserDto {

    private String nickName;
    private String name;
    private int age;
    private String gender;

    public UserDto(User findUser) {
        this.name = findUser.getName();
        this.nickName = findUser.getNickName();
        this.age = findUser.getAge();
        this.gender = findUser.getGender();
    }
}
