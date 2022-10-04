package roommateproject.roommatebackend.repository;

import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.entity.Chat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import roommateproject.roommatebackend.entity.ChatRoom;

@Repository
public class ChatRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Chat> findChatListById(ChatRoom room){
        TypedQuery<Chat> query  = em.createQuery("select c from Chat c where c.chatRoom = :room ",Chat.class);
        query.setParameter("room",room);
        return query.getResultList();
    }

    public Chat findLastMessage(ChatRoom room){
        TypedQuery<Chat> query  = em.createQuery("select c from Chat c where c.chatRoom = :room order by c.sendTime desc",Chat.class);
        query.setParameter("room",room);
        List<Chat> chatList = query.getResultList();
        if (chatList.isEmpty()) {
            return null;
        }
        return chatList.get(0);
    }

    public void store(Chat chat) {
        em.persist(chat);
    }

}
