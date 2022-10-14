package roommateproject.roommatebackend.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
        return em.createQuery(
                "select p.room from Participant p where p.user = :user ", ChatRoom.class)
                .setParameter("user", user)
                .getResultList();
    }

    public User findReceiver(User user, ChatRoom room) {
        return em.createQuery(
                "select p.user from Participant p where p.room = :room and p.user <> :user",
                User.class)
                .setParameter("room", room)
                .setParameter("user", user)
                .getSingleResult();
    }

    @Transactional
    public void remove(ChatRoom room) {
        List<Participant> participants = em.createQuery(
                "select p from Participant p where p.room = :room",
                Participant.class)
                .setParameter("room", room)
                .getResultList();
        for (Participant p : participants) {
            em.remove(p);
        }
    }

}
