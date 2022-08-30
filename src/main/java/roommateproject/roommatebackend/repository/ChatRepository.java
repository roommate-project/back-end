package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;
import roommateproject.roommatebackend.entity.Chat;
import roommateproject.roommatebackend.entity.ChatRoom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class ChatRepository {
    @PersistenceContext
    private EntityManager em;

    public List<ChatRoom> findAllChatRoom(){
        return em.createQuery("select cr from ChatRoom cr",ChatRoom.class).getResultList();
    }

    public ChatRoom findRoom(Long roomId) {
        return em.find(ChatRoom.class, roomId);
    }

    public void store(Chat chat) {
        em.persist(chat);
    }

}
