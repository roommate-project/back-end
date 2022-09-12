package roommateproject.roommatebackend.entity;

import lombok.Data;

import javax.persistence.*;

@Entity @Data
public class UserToken {
    @Id
    @GeneratedValue
    @Column(name = "token_id")
    private Long id;

    private Long userId;
    private String accessToken;
    private String refreshToken;

    public UserToken(Long id, String accessToken, String refreshToken){
        this.userId = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public UserToken() {

    }
}
