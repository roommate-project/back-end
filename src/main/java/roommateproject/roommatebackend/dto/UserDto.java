package roommateproject.roommatebackend.dto;

import lombok.Data;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;

@Data
public class UserDto {

    private String nickName;
    private String name;
    private int age;
    private String gender;

    private String representImage;

    public UserDto(User findUser, UserImage userImage) {
        this.name = findUser.getName();
        this.nickName = findUser.getNickName();
        this.age = findUser.getAge();
        this.gender = findUser.getGender();
        this.representImage = "C:/Users/koscom/back-end/image/represent/" + userImage.getStoreFileName();
    }
}
