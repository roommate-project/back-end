package roommateproject.roommatebackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class LikeIt {

    @Id
    @GeneratedValue
    @Column(name = "like_it_id")
    private Long id;

    @JoinColumn(name = "sender")
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver")
    private User receiver;

    public LikeIt(){}

    public LikeIt(User sender, User receiver){
        this.sender = sender;
        this.receiver = receiver;
    }

}
