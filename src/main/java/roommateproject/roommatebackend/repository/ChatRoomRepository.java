package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;
import roommateproject.roommatebackend.entity.ChatRoom;
import roommateproject.roommatebackend.entity.Message;
import roommateproject.roommatebackend.service.ChatService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class ChatRoomRepository {
    @PersistenceContext
    private EntityManager em;

    private final Set<WebSocketSession> sessions = new HashSet<>();

    public List<ChatRoom> findAllChatRoom(){
        return em.createQuery("select cr from ChatRoom cr",ChatRoom.class).getResultList();
    }

    public ChatRoom findRoom(Long roomId) {
        return em.find(ChatRoom.class, roomId);
    }

    public void store(ChatRoom chatRoom) {
        em.persist(chatRoom);
    }

}
