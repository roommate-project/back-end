package roommateproject.roommatebackend.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long ChatId;
    private String message;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat")
    private ChatRoom chatRoom;
    private Date sendTime;
    private Boolean fileCheck;

    public Chat(String message, ChatRoom chatRoom, Date sendTime, Boolean fileCheck){
        this.message = message;
        this.chatRoom = chatRoom;
        this.sendTime = sendTime;
        this.fileCheck = fileCheck;
    }
}
