package roommateproject.roommatebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue
    @Column(name = "chat_room_id")
    private Long roomId;

    @OneToMany(
            mappedBy = "chatRoom",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    List<Chat> chatList;

    @OneToMany(
            mappedBy = "room",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    List<Participant> participants;

}
