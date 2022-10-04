package roommateproject.roommatebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Blob;
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

    private Long senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private ChatRoom chatRoom;

    private Date sendTime;

    private Boolean isImage;

    @Lob
    private Blob image;


    public Chat(Long chatId, String message, Long senderId,
            ChatRoom chatRoom, Date sendTime) {
        this.chatId = chatId;
        this.message = message;
        this.senderId = senderId;
        this.chatRoom = chatRoom;
        this.sendTime = sendTime;
    }

    public Chat(Long chatId, String message, Long senderId,
            ChatRoom chatRoom, Date sendTime, Boolean isImage, Blob image) {
        this.chatId = chatId;
        this.message = message;
        this.senderId = senderId;
        this.chatRoom = chatRoom;
        this.sendTime = sendTime;
        this.isImage = isImage;
        this.image = image;
    }

    public Chat() {

    }
}
