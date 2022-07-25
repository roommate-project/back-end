package roommateproject.roommatebackend.entity;

import lombok.Getter;
import lombok.Setter;

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

    private String loginId;
    private String name;
    private String password;
    private String nick_name;

    public User(){}

    public User(String loginId, String name, String password, String nick_name){
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.nick_name = nick_name;
    }
}
