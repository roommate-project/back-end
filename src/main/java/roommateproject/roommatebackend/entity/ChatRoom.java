package roommateproject.roommatebackend.entity;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;
import roommateproject.roommatebackend.service.ChatService;

import java.util.HashSet;
import java.util.Set;

@Data
public class ChatRoom {
    private Long roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();
    public ChatRoom(Long roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
    public void handlerActions(WebSocketSession session, Message message, ChatService chatService) {
        if (message.getType().equals(Message.MessageType.ENTER)) {
            sessions.add(session);
            message.setMessage(message.getSender() + "님이 입장했습니다.");
        }
        sendMessage(message, chatService);

    }

    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }
}
