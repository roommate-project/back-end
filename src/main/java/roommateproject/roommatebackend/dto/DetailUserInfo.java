package roommateproject.roommatebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailUserInfo {
    private String name;
    private String nickName;
    private int age;
    private String address;
    private String gender;
    private int experience;
    private String info;
}
