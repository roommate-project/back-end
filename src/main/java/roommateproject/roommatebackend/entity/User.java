package roommateproject.roommatebackend.entity;

import lombok.Getter;
import lombok.Setter;
import roommateproject.roommatebackend.dto.UserAddForm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String name;
    private String password;
    private String nickName;

    public User(){}

    public User(String email, String name, String password, String nickName){
        this.email = email;
        this.name = name;
        this.password = password;
        this.nickName = nickName;
    }

    public User(String requestEmail, UserAddForm userAddForm) {
        this.email = requestEmail;
        this.name = userAddForm.getName();
        this.nickName = userAddForm.getNickName();
        this.password = userAddForm.getPassword();
    }

}