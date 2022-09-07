package roommateproject.roommatebackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
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
    private Boolean isErased;

    public UserImage(User user, Boolean represent, String uploadFileName, String storeFileName, Boolean isErased) {
        this.user = user;
        this.represent = represent;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.isErased = isErased;
    }

    public UserImage() {

    }
}
