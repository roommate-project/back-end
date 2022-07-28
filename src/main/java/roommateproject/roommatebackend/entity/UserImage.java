package roommateproject.roommatebackend.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserImage {

    @Id
    @GeneratedValue
    @Column(name = "user_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    private Boolean represent;
    private String uploadFileName;
    private String storeFileName;

    public UserImage(User user, Boolean represent, String uploadFileName, String storeFileName) {
        this.user = user;
        this.represent = represent;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

    public UserImage() {

    }
}
