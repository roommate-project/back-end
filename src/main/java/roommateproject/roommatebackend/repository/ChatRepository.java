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

    public List<Chat> findChatListById(ChatRoom room) {
        return em.createQuery(
                        "select c from Chat c where c.chatRoom = :room",
                        Chat.class)
                .setParameter("room", room).getResultList();
    }

    public Chat findLastMessage(ChatRoom room) {
        List<Chat> chatList = em.createQuery(
                "select c from Chat c where c.chatRoom = :room order by c.sendTime desc",
                Chat.class)
                .setParameter("room", room)
                .getResultList();
        if (chatList.isEmpty()) {
            return null;
        }
        return chatList.get(0);
    }

    public void store(Chat chat) {
        em.persist(chat);
    }

}
