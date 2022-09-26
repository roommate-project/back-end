package roommateproject.roommatebackend.entity;

import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
public class Chat {

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long chatId;
    private String message;
    private String sender; //TODO: 유저 아이디? 닉네임?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat")
    private ChatRoom chatRoom;
    private Date sendTime;
    private Boolean fileCheck;

    public Chat() {

    }

    public Chat(Long chatId, String message, String sender,
            ChatRoom chatRoom, Date sendTime, Boolean fileCheck) {
        this.chatId = chatId;
        this.message = message;
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.sendTime = sendTime;
        this.fileCheck = fileCheck;
    }
}
