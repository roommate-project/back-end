package roommateproject.roommatebackend.entity;

import lombok.Getter;
import lombok.Setter;
import roommateproject.roommatebackend.dto.UserAddForm;

import javax.persistence.*;
import java.util.List;

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
    private String gender;
    private String register;
    private int age;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<UserImage> images;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Home home;

    @OneToMany(mappedBy = "sender",cascade = CascadeType.ALL)
    private List<LikeIt> likeSender;

    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL)
    private List<LikeIt> likeReceiver;


    public User(){}

    public User(String email, String name, String password, String nickName, String gender, String register, int age){
        this.email = email;
        this.name = name;
        this.password = password;
        this.nickName = nickName;
        this.gender = gender;
        this.register = register;
        this.age = age;
    }

    public User(String requestEmail, UserAddForm userAddForm, String register) {
        this.email = requestEmail;
        this.name = userAddForm.getName();
        this.nickName = userAddForm.getNickName();
        this.password = userAddForm.getPassword();
        this.gender = userAddForm.getGender();
        this.register = register;
        this.age = userAddForm.getAge();
    }

}
