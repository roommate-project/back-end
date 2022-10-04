package roommateproject.roommatebackend.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.*;
import roommateproject.roommatebackend.entity.ChatRoom;

@Repository
public class ChatRoomRepository {
    @PersistenceContext
    private EntityManager em;

    public List<ChatRoom> findAllChatRoom(){
        return em.createQuery("select cr from ChatRoom cr",ChatRoom.class).getResultList();
    }

    public ChatRoom findRoom(Long roomId) {
        return em.find(ChatRoom.class, roomId);
    }

    public void store(ChatRoom chatRoom) {
        em.persist(chatRoom);
    }

    public void remove(ChatRoom chatRoom) {
        em.remove(chatRoom);
    }

}
