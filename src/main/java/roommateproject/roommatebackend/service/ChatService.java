package roommateproject.roommatebackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import roommateproject.roommatebackend.entity.Chat;
import roommateproject.roommatebackend.entity.ChatRoom;
import roommateproject.roommatebackend.entity.Message;
import roommateproject.roommatebackend.repository.ChatRepository;
import roommateproject.roommatebackend.repository.ChatRoomRepository;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final Set<WebSocketSession> sessions;

    public ChatService(ObjectMapper objectMapper, ChatRoomRepository chatRoomRepository, ChatRepository chatRepository, Set<WebSocketSession> sessions) {
        this.objectMapper = objectMapper;
        this.chatRoomRepository = chatRoomRepository;
        this.chatRepository = chatRepository;
        this.sessions = sessions;
    }

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAllChatRoom();
    }

    public ChatRoom findRoomById(Long roomId) {
        return chatRoomRepository.findRoom(roomId);//chatRooms.get(roomId);
    }

    @Transactional
    public ChatRoom createRoom(String name) {
    //    Long randomId = Long.parseLong(UUID.randomUUID().toString());
        ChatRoom chatRoom = new ChatRoom(name);
        chatRoomRepository.store(chatRoom);
   //     chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    @Transactional
    public <T> void send(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Transactional
    public void handlerActions(WebSocketSession session, Message message, ChatRoom chatRoom) {
        String sendMessage = null;
        if (message.getType().equals(Message.MessageType.ENTER)) {
            sendMessage = message.getSender() + "님이 입장했습니다.";
            sessions.add(session);
            message.setMessage(sendMessage);
        }else{
            sendMessage = message.getMessage();
        }
        sendMessage(message);
        chatRepository.store(new Chat(sendMessage,chatRoom,new Date(),false));
    }

    private <T> void sendMessage(T message) {
        sessions.parallelStream()
                .forEach(session -> send(session, message));
    }
}
