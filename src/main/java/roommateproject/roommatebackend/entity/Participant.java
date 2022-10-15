package roommateproject.roommatebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Participant {

    @Id
    @GeneratedValue
    @Column(name = "participant_id")
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    @JsonIgnore
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
