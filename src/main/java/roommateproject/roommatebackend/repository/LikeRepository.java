package roommateproject.roommatebackend.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.entity.LikeIt;
import roommateproject.roommatebackend.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository @Slf4j
public class LikeRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(LikeIt likeIt){
        em.persist(likeIt);
        return likeIt.getId();
    }

    public List<LikeIt> find(User user){
        return em.createQuery("select l from LikeIt l where l.sender=:user",LikeIt.class)
                .setParameter("user", user)
                .getResultList();
    }

    public void erase(LikeIt likeIt) {
        LikeIt findLike = em.createQuery("select l from LikeIt l where l.sender=:sender and l.receiver=:receiver",LikeIt.class)
                .setParameter("sender",likeIt.getSender())
                .setParameter("receiver",likeIt.getReceiver())
                .getSingleResult();
        em.remove(findLike);
    }

    public boolean exists(LikeIt likeIt) {
        return !em.createQuery("select l from LikeIt l where l.sender=:sender and l.receiver=:receiver",LikeIt.class)
                    .setParameter("sender",likeIt.getSender())
                    .setParameter("receiver",likeIt.getReceiver())
                    .getResultList().isEmpty();
    }

    public int getReceiverCount(User user) {
        return em.createQuery("select l from LikeIt l where l.receiver=:user")
                .setParameter("user",user)
                .getResultList().size();
    }

    public List<LikeIt> getAllLike(User user, int start) {
        return em.createQuery("select l from User u join u.likeSender l where u.id=:user")
                .setParameter("user",user.getId())
                .setFirstResult((start - 1) * 10)
                .setMaxResults(10)
                .getResultList();
    }

    public Optional<LikeIt> checkLike(User user, User u) {
        return em.createQuery("select l from LikeIt l where l.sender.id = :sender and l.receiver.id = :receiver",LikeIt.class)
                .setParameter("sender", user.getId())
                .setParameter("receiver", u.getId())
                .getResultList().stream().findAny();
    }

    public Optional<LikeIt> findByUser(User loginUser, User user) {
        return em.createQuery("select l from LikeIt l where l.sender.id = :sender and l.receiver.id = :receiver",LikeIt.class)
                .setParameter("sender",loginUser.getId())
                .setParameter("receiver",user.getId())
                .getResultList().stream().findAny();
    }
}
