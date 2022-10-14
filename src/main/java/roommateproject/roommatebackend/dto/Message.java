package roommateproject.roommatebackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        CHAT, IMAGE
    }

    private MessageType type; // 메시지 타입
    private Long roomId; // 방번호
    private Long senderId; // 메시지 보낸 사람 id
    private String message; // 메시지
    private String image; // 이미지

}