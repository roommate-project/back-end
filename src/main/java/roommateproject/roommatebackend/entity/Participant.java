package roommateproject.roommatebackend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Participant {

    @Id
    @GeneratedValue
    @Column(name = "participant_id")
    private Long participantId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    public Participant(User user, ChatRoom room) {
        this.user = user;
        this.room = room;
    }

    public Participant() {

    }

    public Participant(Long participantId, User user, ChatRoom room) {
        this.participantId = participantId;
        this.user = user;
        this.room = room;
    }
}
