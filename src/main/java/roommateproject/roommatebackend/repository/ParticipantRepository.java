package roommateproject.roommatebackend.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.entity.ChatRoom;
import roommateproject.roommatebackend.entity.Participant;
import roommateproject.roommatebackend.entity.User;

@Repository
public class ParticipantRepository {

    @PersistenceContext
    private EntityManager em;

    public void store(Participant participant) {
        em.persist(participant);
    }

    public List<ChatRoom> findAllChatRoom(User user) {
        TypedQuery<ChatRoom> query = em.createQuery(
                "select p.room from Participant p where p.user = :user ", ChatRoom.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    public User findReceiver(User user, ChatRoom room) {
        TypedQuery<User> query = em.createQuery(
                "select p.user from Participant p where p.room = :room and p.user <> :user",
                User.class);
        query.setParameter("room", room);
        query.setParameter("user", user);
        return query.getSingleResult();
    }

}
