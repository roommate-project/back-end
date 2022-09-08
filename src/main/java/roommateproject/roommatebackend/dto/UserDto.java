package roommateproject.roommatebackend.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;

@Data
public class UserDto {

    private Long userId;
    private String nickName;
    private String name;
    private int age;
    private String gender;

    private Long representImageId;

    public UserDto(User findUser, UserImage userImage) {
        this.userId = findUser.getId();
        this.name = findUser.getName();
        this.nickName = findUser.getNickName();
        this.age = findUser.getAge();
        this.gender = findUser.getGender();
        this.representImageId = userImage.getId();
    }
}
