package roommateproject.roommatebackend.entity;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;
import roommateproject.roommatebackend.service.ChatService;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue
    @Column(name = "chat_room_id")
    private Long roomId;
    private String name;


    public ChatRoom(String name) {
        this.name = name;
    }
    public ChatRoom() {

    }
}
